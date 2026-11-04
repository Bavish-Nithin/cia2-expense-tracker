
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out Expense Tracker project'
            }
        }

        stage('Security Scan - Gitleaks') {
            steps {
                bat 'gitleaks git .'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }
    }
}
