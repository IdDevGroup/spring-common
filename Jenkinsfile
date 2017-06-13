#!groovy​
podTemplate(label: 'spring-common', containers: [
        containerTemplate(name: 'maven', image: 'maven:latest', ttyEnabled: true, command: 'cat')
]) {

    node('spring-common') {
        stage('Preparation') {
            checkout scm
        }

        stage('Build') {
            container('maven') {
                sh 'mvn clean package'
            }
        }

        stage("Results") {
//            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true, allowEmptyArchive: false, onlyIfSuccessful: true;
        }

        stage('Publish') {

        }
    }
}