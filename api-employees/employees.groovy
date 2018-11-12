#!/usr/bin/groovy

def Build(){
    sh """
    cd api-employees
    echo "We are currently in directory $pwd"
    mvn clean install
    """
}

def DockerImg(){
     
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'dockercredentials',
          usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          sh """
			cd api-employees
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            echo "We are currently in directory $pwd"
			      docker build -t anilbb/api-employees .
            docker push anilbb/api-employees
            """
        }
}
   
def RunHelm(){
        sh "helm list"
        
          try{
            sh "helm del --purge api-employees-helm"
          }
          catch(error) {
              echo "No previous helm deployments found"
          }
          
          
          echo "Removing the current helm chart package"
       
          try{
                sh "rm -f api-employees/helm/api-employees-helm-0.1.0.tgz"
          }catch(error) {
              echo "No previous helm package found"
          }
          
          
          echo "Creating the new helm package"
          try {  
            sh "helm package api-employees/helm/api-employees-helm" 
          } catch(error) {
              echo "created the package"
          }
          sh "cp api-employees-helm-0.1.0.tgz api-employees/helm/"
          echo "Installing the new helm package"
          
        sh "helm install --name api-employees-helm api-employees/helm/api-employees-helm-0.1.0.tgz"
        
        echo "Application anilbb/api-employees successfully deployed. Use helm status anilbb/api-employees to check"
 }
 
return this
