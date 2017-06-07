podTemplate(label: 'spring-common', containers: [
        containerTemplate(name: 'maven', image: 'maven:latest', ttyEnabled: true, command: 'cat')
]) {

    node('spring-common') {
        stage("SCM Checkout") {
            checkout(
                    [
                            $class: 'GitSCM',
                            branches: [
                                    [
                                            name: '*/master'
                                    ]
                            ],
                            doGenerateSubmoduleConfigurations: false,
                            extensions: [],
                            submoduleCfg: [],
                            userRemoteConfigs: [
                                    [
                                            credentialsId: 'github-ssh',
                                            url: 'git@github.com:IdDevGroup/spring-common.git'
                                    ]
                            ]
                    ]
            )
        }

        stage('MVN Install') {
            container('maven') {
                stage('Build a Maven project') {
                    sh 'mvn clean install'
                }
            }
        }

        stage("Archive Artifacts") {
            archiveArtifacts 'target/*.jar'
        }
    }
}