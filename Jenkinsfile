pipeline {
    agent any

    stages {
        stage('Check Azure Tools') {
            steps {
                bat '"C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" --version'
                bat '"C:\\Users\\bavis\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" version --client'
            }
        }
    }
}