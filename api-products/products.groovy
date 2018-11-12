#!/usr/bin/groovy

def Build(){
    sh """
    cd api-products
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
			cd api-products
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            echo "We are currently in directory $pwd"
			      docker build -t anilbb/api-products .
            docker push anilbb/api-products
            """
        }
}
   
def RunHelm(){
        sh "helm list"
        
          try{
            sh "helm del --purge api-products-helm"
          }
          catch(error) {
              echo "No previous helm deployments found"
          }
          
          
          echo "Removing the current helm chart package"
       
          try{
                sh "rm -f api-products/helm/api-products-helm-0.1.0.tgz"
          }catch(error) {
              echo "No previous helm package found"
          }
          
          
          echo "Creating the new helm package"
          try {  
            sh "helm package api-products/helm/api-products-helm" 
          } catch(error) {
              echo "created the package"
          }
          sh "cp api-products-helm-0.1.0.tgz api-products/helm/"
          echo "Installing the new helm package"
          
        sh "helm install --name api-products-helm api-products/helm/api-products-helm-0.1.0.tgz"
        
        echo "Application anilbb/api-products successfully deployed. Use helm status anilbb/api-products to check"
 }
 
return this
