
pipeline {
    agent any

    stages {
        stage('Test Azure Authentication') {
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

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" account show --query "{subscription:name, state:state}" --output table
                        if errorlevel 1 exit /b 1

                        "C:\\Program Files\\Microsoft SDKs\\Azure\\CLI2\\wbin\\az.cmd" logout
                    '''
                }
            }
        }
    }
}
