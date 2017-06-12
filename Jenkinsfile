podTemplate(label: 'spring-common', containers: [
        containerTemplate(name: 'maven', image: 'maven:latest', ttyEnabled: true, command: 'cat')
]) {

    node('spring-common') {
        checkout scm

        stage('Package') {
            container('maven') {
                stage('Build a Maven project') {
                    sh 'mvn clean package'
                }

                stage('Build a Maven project') {
                    sh 'mvn clean package'
                }
            }
        }

        stage("Archive") {
            archiveArtifacts 'target/*.jar'
        }
    }
}