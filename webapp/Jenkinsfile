podTemplate(label: 'Jenkins', containers: [
  containerTemplate(name: 'jnlp', image: 'lachlanevenson/jnlp-slave:3.10-1-alpine', args: '${computer.jnlpmac} ${computer.name}', workingDir: '/home/jenkins', resourceRequestCpu: '200m', resourceLimitCpu: '300m', resourceRequestMemory: '256Mi', resourceLimitMemory: '512Mi'),
  containerTemplate(name: 'kubectl', image: 'pahud/eks-kubectl-docker', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:v2.8.2', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker:1.12.0', command: 'cat', ttyEnabled: true)  
],
volumes: [
   hostPathVolume(mountPath: '/root/.m2/', hostPath: '/tmp/jenkins/.m2'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node('Jenkins') {
    
   checkout scm
   def rootDir = pwd()
   println("Current Directory: " + rootDir)

   // point to exact source file
   def pipeline = load "${rootDir}/library.groovy"
   
   //ECR as docker registry
   /* stage('Create Docker images ECR') {
      container('docker') {
       
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'dockercredentialsECR',
          usernameVariable: 'ECR_USER',
          passwordVariable: 'ECR_PASSWORD']]) {
          sh """           
            docker login -u ${ECR_USER} -p ${ECR_PASSWORD} https://863826605418.dkr.ecr.us-east-1.amazonaws.com
            echo "Build docker image"
			      docker build -t discoverpoc/webapp-smt .
                  
             echo "Tag the image"
             docker tag  kubernetes-demo 863826605418.dkr.ecr.us-east-1.amazonaws.com/discoverpoc:webapp-smt
 
			      echo "Push image to aws ECR ${ECR_USER} https://863826605418.dkr.ecr.us-east-1.amazonaws.com"
                                        
            docker push 863826605418.dkr.ecr.us-east-1.amazonaws.com/discoverpoc:webapp-smt
            """
        }
      }
    }*/
    
       
     stage('Create Docker images') {
      container('docker') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
         credentialsId: 'dockercredentials',
         usernameVariable: 'DOCKER_HUB_USER',
          passwordVariable: 'DOCKER_HUB_PASSWORD']]) {
           
          sh """
            docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD}
            docker build -t anilbb/webapp-smt:latest .
            docker push anilbb/webapp-smt:latest
            """
        }
      }
    }
    
    //stage test kubectl
     stage('Test kubectl') {
      container('kubectl') {
        pipeline.kubectlTest()
      }
    }
    
    
    //stage run helm
    stage('Run Helm') {
     container('helm') {
      sh " helm list "
       
       echo "Removing the old helm deployment"
       try {
            sh "helm del --purge web-app"
       }
       catch(error) {
        echo "No previous helm deployments"  
       }
       
       echo "Remvoving the old helm package"
       try {
            sh "rm -f helm/web-app-0.1.0.tgz"
       }
       catch(error) {
         echo "No previous helm packages found"
       }
       
       try{
         echo "Creating the new helm package for deployment"
         sh "helm package helm/web-app"
       }catch(error)
       {
         echo "helm package created"
       }
       
       sh "cp  web-app-0.1.0.tgz  helm"
       echo "Installing the new helm deployment package"
       sh "helm install --name web-app helm/web-app-0.1.0.tgz"
       echo "Application web-app successfully deployed"
      }
    }
   
  }
}
 
