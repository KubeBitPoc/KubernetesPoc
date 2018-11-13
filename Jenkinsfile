#!/usr/bin/groovy


podTemplate(label: 'Jenkins', containers: [
   containerTemplate(name: 'jnlp', image: 'lachlanevenson/jnlp-slave:3.10-1-alpine', args: '${computer.jnlpmac} ${computer.name}', workingDir: '/home/jenkins', resourceRequestCpu: '200m', resourceLimitCpu: '300m', resourceRequestMemory: '256Mi', resourceLimitMemory: '512Mi'),
	containerTemplate(name: 'maven', image: 'jenkinsxio/builder-maven:0.1.22', command: 'cat', ttyEnabled: true),
    //containerTemplate(name: 'gradle', image: 'gradle-4.10.2-jdk7', command: 'cat', ttyEnabled: true),
    containerTemplate(name: 'docker', image: 'docker:1.12.0', command: 'cat', ttyEnabled: true),    
	containerTemplate(name: 'kubectl', image: 'pahud/eks-kubectl-docker', command: 'cat', ttyEnabled: true),
	containerTemplate(name: 'helm', image: 'lachlanevenson/k8s-helm:v2.8.2', command: 'cat', ttyEnabled: true)
],volumes:[
	hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
    hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),  
]) {
	node('Jenkins') {
			 
		checkout scm
		echo "$PWD"
		def pipCustomer = load 'api-customers/build.groovy'
		def pipEmployees = load 'api-employees/build.groovy'
		def orders = load 'api-orders/build.groovy'
		def products = load 'api-products/build.groovy'
		def NginxReverseProxy = load 'NginxReverseProxy/build.groovy'
		def webapp = load 'webapp/build.groovy'
		
		//Read configuration 
		def inputFile = readFile('Jenkinsfile.json')
		def config = new groovy.json.JsonSlurperClassic().parseText(inputFile)
			
		
		/*
		//Build code
		if (config.pipeline.build) {
			stage('Build') {
			  container('maven') {
				pipCustomer.Build()
				pipEmployees.Build()
				orders.Build()
				products.Build()		
			  }
			}

		   //build docker image
			stage('Create Docker images') {
			  container('docker') {
				NginxReverseProxy.DockerImg()
				webapp.DockerImg()
				pipCustomer.DockerImg()
				pipEmployees.DockerImg()
				orders.DockerImg()
				products.DockerImg()			
			  }
			}
		}
		  */
	   //Deploy Nginex
	   if (config.pipeline.deployment) {
			 if (config.pipeline.nginexKubectl){
				stage("deploy Nginex"){
				  container('kubectl') {
						NginxReverseProxy.RunKubectl()
				  }
				}
			}
			//run helm	  
		/*	stage('Run helm') {
			  container('helm') {
				if (!config.pipeline.nginexKubectl){
					NginxReverseProxy.RunHelm()
				}
				webapp.RunHelm()
				pipCustomer.RunHelm()
				pipEmployees.RunHelm()
				orders.RunHelm()
				products.RunHelm()			
			  }
			}*/
		}
	}
}

