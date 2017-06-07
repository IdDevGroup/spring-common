podTemplate(label: 'spring-common', containers: [
        containerTemplate(name: 'maven', image: 'maven:latest', ttyEnabled: true, command: 'cat')
]) {

    node('spring-common') {
        stage("Checkout project") {
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

        stage('Get a Maven project') {
            container('maven') {
                stage('Build a Maven project') {
                    sh 'mvn clean install'
                }
            }
        }
        // post {
        //     always {
        //         archive "target/**/*"
        //         junit 'target/surefire-reports/*.xml'
        //     }
        // }
    }
}