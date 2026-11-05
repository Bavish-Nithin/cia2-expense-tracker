pipeline {
    agent any

    stages {
        stage('Check Azure Tools') {
            steps {
                bat 'where az'
                bat 'where kubectl'
                bat 'az --version'
                bat 'kubectl version --client'
            }
        }
    }
}