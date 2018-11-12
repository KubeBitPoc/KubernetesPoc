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

		 try {
					sh "kubectl delete deployment frontend-deployment"
			} catch(error) {}
				
				
				sh "kubectl create -f NginxReverseProxy/frontend-deployment.yaml"
				
				try {
					sh "kubectl delete service frontend-service"
				} catch(error) {}
				
				sh "kubectl create -f NginxReverseProxy/frontend-service.yaml"
	}
	
return this
