
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
                bat '"C:/Users/bavis/AppData/Local/Microsoft/WinGet/Packages/Gitleaks.Gitleaks_Microsoft.Winget.Source_8wekyb3d8bbwe/gitleaks.exe" git .'
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }
    }
}
