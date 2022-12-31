# config Map for Kubernetes Pipeline:
in k8s Pipeline, config map key:
- type : 'frontend' / 'backend'
- image_name : String
- image_tag : String
- doBuild : boolean (true/false)
- dockerCredentials : credentialsId of Dockerhub
- deployment : 'frontend-react' / 'backend-express'