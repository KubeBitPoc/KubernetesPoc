#!/usr/bin/groovy

def Build(){
    sh """
    cd api-orders
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
			cd api-orders
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            echo "We are currently in directory $pwd"
			      docker build -t anilbb/api-orders .
            docker push anilbb/api-orders
            """
        }
}
   
def RunHelm(){
        sh "helm list"
        
          try{
            sh "helm del --purge api-orders-helm"
          }
          catch(error) {
              echo "No previous helm deployments found"
          }
          
          
          echo "Removing the current helm chart package"
       
          try{
                sh "rm -f api-orders/helm/api-orders-helm-0.1.0.tgz"
          }catch(error) {
              echo "No previous helm package found"
          }
          
          
          echo "Creating the new helm package"
          try {  
            sh "helm package api-orders/helm/api-orders-helm" 
          } catch(error) {
              echo "created the package"
          }
          sh "cp api-orders-helm-0.1.0.tgz api-orders/helm/"
          echo "Installing the new helm package"
          
        sh "helm install --name api-orders-helm api-orders/helm/api-orders-helm-0.1.0.tgz"
        
        echo "Application anilbb/api-orders successfully deployed. Use helm status anilbb/api-orders to check"
 }
 
return this
