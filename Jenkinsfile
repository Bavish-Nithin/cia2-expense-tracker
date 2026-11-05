
pipeline {
    agent any

    environment {
        ACR_NAME = 'cia2expenseregistry'
        ACR_LOGIN_SERVER = 'cia2expenseregistry.azurecr.io'
        IMAGE_NAME = 'cia2-expense-tracker'
        RESOURCE_GROUP = 'cia2-expense-rg'
        AKS_CLUSTER = 'cia2-expense-aks'
    }

    stages {
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

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %ACR_LOGIN_SERVER%/%IMAGE_NAME%:build-%BUILD_NUMBER% .'
            }
        }

        stage('Push Image to ACR') {
            steps {
                withCredentials([
                    string(credentialsId: 'AZURE_CLIENT_ID', variable: 'AZURE_CLIENT_ID'),
                    string(credentialsId: 'AZURE_CLIENT_SECRET', variable: 'AZURE_CLIENT_SECRET'),
                    string(credentialsId: 'AZURE_TENANT_ID', variable: 'AZURE_TENANT_ID'),
                    string(credentialsId: 'AZURE_SUBSCRIPTION_ID', variable: 'AZURE_SUBSCRIPTION_ID')
                ]) {
                    bat '''
                        set AZURE_CONFIG_DIR=%WORKSPACE%\\.azure

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" login --service-principal -u "%AZURE_CLIENT_ID%" -p "%AZURE_CLIENT_SECRET%" --tenant "%AZURE_TENANT_ID%"
                        if errorlevel 1 exit /b 1

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" account set --subscription "%AZURE_SUBSCRIPTION_ID%"
                        if errorlevel 1 exit /b 1

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" acr login --name %ACR_NAME%
                        if errorlevel 1 exit /b 1

                        docker push %ACR_LOGIN_SERVER%/%IMAGE_NAME%:build-%BUILD_NUMBER%
                        if errorlevel 1 exit /b 1

                        echo IMAGE PUSHED SUCCESSFULLY
                    '''
                }
            }
        }

        stage('Deploy to AKS') {
            steps {
                withCredentials([
                    string(credentialsId: 'AZURE_CLIENT_ID', variable: 'AZURE_CLIENT_ID'),
                    string(credentialsId: 'AZURE_CLIENT_SECRET', variable: 'AZURE_CLIENT_SECRET'),
                    string(credentialsId: 'AZURE_TENANT_ID', variable: 'AZURE_TENANT_ID'),
                    string(credentialsId: 'AZURE_SUBSCRIPTION_ID', variable: 'AZURE_SUBSCRIPTION_ID')
                ]) {
                    bat '''
                        set AZURE_CONFIG_DIR=%WORKSPACE%\\.azure

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" login --service-principal -u "%AZURE_CLIENT_ID%" -p "%AZURE_CLIENT_SECRET%" --tenant "%AZURE_TENANT_ID%"
                        if errorlevel 1 exit /b 1

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" account set --subscription "%AZURE_SUBSCRIPTION_ID%"
                        if errorlevel 1 exit /b 1

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" aks get-credentials --resource-group %RESOURCE_GROUP% --name %AKS_CLUSTER% --admin --file "%WORKSPACE%\\jenkins-kubeconfig" --overwrite-existing
                        if errorlevel 1 exit /b 1

                        set KUBECONFIG=%WORKSPACE%\\jenkins-kubeconfig

                        "C:\\Users\\bavis\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" set image deployment/expense-tracker expense-tracker=%ACR_LOGIN_SERVER%/%IMAGE_NAME%:build-%BUILD_NUMBER%
                        if errorlevel 1 exit /b 1

                        "C:\\Users\\bavis\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\kubectl.exe" rollout status deployment/expense-tracker --timeout=180s
                        if errorlevel 1 exit /b 1

                        echo AKS DEPLOYMENT SUCCESSFUL
                    '''
                }
            }
        }
    }

    post {
        always {
            bat 'if exist jenkins-kubeconfig del jenkins-kubeconfig'
        }
    }
}
