pipeline {
    agent any

    tools {
        maven 'MAVEN3'
    }

    environment {
        IMAGE_NAME = "divyach2002/blog-app"
        IMAGE_TAG = "latest"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/dwork0779/BlogSphere.git'
            }
        }

        stage('Build Jar') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %IMAGE_NAME%:%IMAGE_TAG% ."
            }
        }

        stage('Push To Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-hub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    bat '''
                    docker logout
                    docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                    docker push %IMAGE_NAME%:%IMAGE_TAG%
                    '''
                }
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }
    }
}
