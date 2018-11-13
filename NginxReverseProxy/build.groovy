#!/usr/bin/groovy



def DockerImg(){
     
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'dockercredentials',
          usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
          sh """
			cd NginxReverseProxy
            echo "Attempting docker login using ${DOCKER_HUB_USER}"
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            echo "We are currently in directory $pwd"
			docker build -t anilbb/webapp-proxy:latest .
            docker push anilbb/webapp-proxy:latest
			
            """
        }
}
   
def RunHelm(){

		sh "helm list"
        
          try{
            sh "helm del --purge NginxReverseProxy-helm"
          }
          catch(error) {
              echo "No previous helm deployments found"
          }
          
          
          echo "Removing the current helm chart package"
       
          try{
                sh "rm -f NginxReverseProxy/helm/NginxReverseProxy-0.1.0.tgz"
          }catch(error) {
              echo "No previous helm package found"
          }
          
          
          echo "Creating the new helm package"
          try {  
		sh " current dir $PWD"
		sh "helm package NginxReverseProxy/helm/NginxReverseProxy"
		 
          } catch(error) {
              echo "Create package error : $error"
          }
          sh "cp NginxReverseProxy-0.1.0.tgz NginxReverseProxy/helm/"
          echo "Installing the new helm package"
          
        sh "helm install --name NginxReverseProxy-helm NginxReverseProxy/helm/NginxReverseProxy-0.1.0.tgz"
        
        echo "Application anilbb/webapp-proxy successfully deployed. Use helm status anilbb/webapp-proxy to check"
		
		
	}
	
return this
