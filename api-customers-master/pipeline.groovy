#!/usr/bin/groovy

def Build(){
    sh """
    cd api-customers-master
    echo $pwd"
    mvn clean install
    """
}

def DockerImg(){
     
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'dockercredentials',
          usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          sh """
           
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            echo "We are currently in directory $pwd"
			      docker build -t anilbb/api-customers .
            docker push anilbb/api-customers
            """
        }
}
   
def RunHelm(){
        sh "helm list"
        
          try{
            sh "helm del --purge api-customers-helm"
          }
          catch(error) {
              echo "No previous helm deployments found"
          }
          
          
          echo "Removing the current helm chart package"
       
          try{
                sh "rm -f helm/api-customers-helm-0.1.0.tgz"
          }catch(error) {
              echo "No previous helm package found"
          }
          
          
          echo "Creating the new helm package"
          try {  
            sh "helm package helm/api-customers-helm" 
          } catch(error) {
              echo "created the package"
          }
          sh "cp api-customers-helm-0.1.0.tgz helm/"
          echo "Installing the new helm package"
          
        sh "helm install --name api-customers-helm helm/api-customers-helm-0.1.0.tgz"
        
        echo "Application anilbb/api-customers successfully deployed. Use helm status anilbb/api-customers to check"
 }
 
return this
