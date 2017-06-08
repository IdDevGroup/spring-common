podTemplate(label: 'spring-common', containers: [
        containerTemplate(name: 'maven', image: 'maven:latest', ttyEnabled: true, command: 'cat')
]) {

    node('spring-common') {
        stage("Checkout") {
            checkout scm

//            checkout(
//                    [
//                            $class: 'GitSCM',
//                            branches: [
//                                    [
//                                            name: '*/master'
//                                    ]
//                            ],
//                            doGenerateSubmoduleConfigurations: false,
//                            extensions: [],
//                            submoduleCfg: [],
//                            userRemoteConfigs: [
//                                    [
//                                            credentialsId: 'github-ssh',
//                                            url: 'git@github.com:IdDevGroup/spring-common.git'
//                                    ]
//                            ]
//                    ]
//            )
        }

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