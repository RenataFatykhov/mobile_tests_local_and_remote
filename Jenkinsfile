pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }
    parameters {
        choice(name: 'PLATFORM', choices: ['android', 'ios'], description: 'Android Wikipedia / iOS UI Elements')
        string(name: 'APP_URL', defaultValue: '', description: 'BrowserStack app URL: bs://... (обязательно)')
    }
    stages {
        stage('Validate configuration') {
            steps {
                script {
                    if (!(params.APP_URL ==~ /bs:\/\/[a-zA-Z0-9_-]+/)) {
                        error('Укажите APP_URL загруженного приложения в формате bs://...')
                    }
                }
            }
        }
        stage('Infrastructure tests') {
            steps { sh 'sh ./gradlew clean unitTest' }
        }
        stage('Mobile tests — BrowserStack') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'browserstack',
                    usernameVariable: 'BROWSERSTACK_USER', passwordVariable: 'BROWSERSTACK_KEY')]) {
                    withEnv(["TEST_PLATFORM=${params.PLATFORM}", "TEST_APP=${params.APP_URL}"]) {
                        sh '''sh ./gradlew test -DdeviceHost=browserstack -Dplatform="$TEST_PLATFORM" \
                            -D"$TEST_PLATFORM.app=$TEST_APP" -Dbrowserstack.build="Jenkins-$BUILD_NUMBER"'''
                    }
                }
            }
        }
    }
    post {
        always {
            junit testResults: 'build/test-results/**/*.xml', allowEmptyResults: true
            allure includeProperties: false, results: [[path: 'build/allure-results']]
        }
    }
}
