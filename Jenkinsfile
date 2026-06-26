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
                bat "docker tag %IMAGE_NAME%:%IMAGE_TAG% %IMAGE_NAME%:latest"
            }
        }

       stage('Push To Docker Hub') {
            steps {
                bat '''
                docker push %IMAGE_NAME%:%IMAGE_TAG%
                docker push %IMAGE_NAME%:latest
                '''
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }
    }
}
