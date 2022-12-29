def call() {
    pipeline {
        agent any
        triggers {
            githubPush()
        }
        stages {
            stage("Mavening") {
                steps {
                    script {
                        author(["clean", "compile", "test"])
                    }
                }
            }
            stage("Resource Library") {
                steps {
                    script {
                        def config = libraryResource("config/build.json")
                        echo config
                    }
                }
            }
        }
    }
}