def call(Map config){
    pipeline {
        agent none
        triggers {
            githubPush()
        }
        // environment {
        //     IMAGE_NAME = config.image_name
        //     IMAGE_TAG = config.image_tag
        //     DEPLOYMENT = config.deployment
        // }
        stages {
            stage("Build Image") {
                when {
                    expression {
                        // doBuild = boolean type
                        return config.doBuild
                    }
                }
                agent {
                    node {
                        label 'agent-one'
                    }
                }
                steps {
                    script {
                        sh "docker build -t ${config.image_name}:${config.image_tag} ."
                        withCredentials([usernamePassword(
                            credentialsId: config.dockerCredentials, 
                            usernameVariable: "DOCKER_USERNAME",
                            passwordVariable: "DOCKER_PASSWORD")]) {
                                sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                                sh "docker push ${config.image_name}:${config.image_tag}"
                            }
                        if(config.type == 'frontend') {
                            sh "docker image prune --filter label=stage=builder -f"
                        }
                    }
                }
            }
            stage("Deploy") {
                agent {
                    node {
                        label "master"
                    }
                }
                steps {
                    script {
                        sh "kubectl delete deployment ${config.deployment}"
                        sh 'kubectl apply -f k8s/deployment.yaml'
                        sh 'kubectl get svc'
                    }
                }
            }
        }
        post {
            failure {
                node('agent-one') {
                    script {
                        sh "docker image prune -f"
                    }
                }
            }
        }
    }
}