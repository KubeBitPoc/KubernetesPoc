#!/usr/bin/groovy



def DockerImg(){
     
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'dockercredentials',
          usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          sh """
			cd webapp
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            docker build -t anilbb/webapp-smt:latest .
            docker push anilbb/webapp-smt:latest
			"""
        }
}
   
def RunHelm(){
        sh "helm list"
        
          try{
            sh "helm del --purge webapp-helm"
          }
          catch(error) {
              echo "No previous helm deployments found"
          }
          
          
          echo "Removing the current helm chart package"
       
          try{
                sh "rm -f webapp/helm/webapp-helm-0.1.0.tgz"
          }catch(error) {
              echo "No previous helm package found"
          }
          
          
          echo "Creating the new helm package"
          try {  
            sh "helm package webapp/helm/webapp-helm" 
          } catch(error) {
              echo "created the package"
          }
          sh "cp webapp-helm-0.1.0.tgz webapp/helm/"
          echo "Installing the new helm package"
          
        sh "helm install --name webapp-helm webapp/helm/webapp-helm-0.1.0.tgz"
        
        echo "Application anilbb/webapp successfully deployed. Use helm status anilbb/webapp to check"
 }
 
return this
