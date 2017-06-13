#!groovy​
podTemplate(label: 'spring-common',
        containers: [
                containerTemplate(
                        name: 'maven',
                        image: 'maven:alpine',
                        ttyEnabled: true,
                        command: 'cat',
                        'envVars': [
                                containerEnvVar(key: 'MAVEN_OPTS', value: "-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn")
                        ]),
        ],
        volumes: [
                secretVolume(secretName: 'maven-settings', mountPath: '/root/.m2')
        ]
) {

    node('spring-common') {
        stage('Preparation') {
            checkout scm
        }

        container('maven') {
            stage('Build') {
                sh 'mvn clean verify --quiet'
                sh 'mvn package -Dmaven.test.skip=true --batch-mode'
            }

            stage('Publish') {
                container('maven') {
                    sh 'mvn deploy -Dmaven.test.skip=true --batch-mode'
                }
            }
        }

        stage("Results") {
//            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true, allowEmptyArchive: false, onlyIfSuccessful: true;
        }
    }
}