#!groovy​
podTemplate(label: 'spring-common',
        containers: [
                containerTemplate(name: 'maven', image: 'maven:alpine', ttyEnabled: true, command: 'cat'),
        ],
        volumes: [
                secretVolume(secretName: 'maven-settings', mountPath: '/root/.m2')
        ]
) {

    node('spring-common') {
        stage('Preparation') {
            checkout scm
        }

        stage('Build') {
            container('maven') {
                sh 'mvn clean verify --quiet'
            }
        }

        stage("Results") {
//            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true, allowEmptyArchive: false, onlyIfSuccessful: true;
        }

        stage('Publish') {
            container('maven') {
                sh 'mvn deploy -Dmaven.test.skip=true'
            }
        }
    }
}