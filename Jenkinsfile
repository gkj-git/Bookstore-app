pipeline {
    agent { label 'Jenkins-Agent' }
    tools {
        jdk 'Java21'
        maven 'Maven3'
    }
    stages {
        stage('cleanup workspace') {
      steps {
        cleanWS()
      }
        }
        stage('Checkout from SCM') {
      steps {
        git branch: 'main', credentialsId: 'github', url: 'https://github.com/gkj-git/Bookstore-app.git'
      }
        }
        stage('Build Application') {
      steps {
        sh 'mvn clean package'
      }
        }
        stage('Test Application') {
      steps {
        sh 'mvn test'
      }
        }
    }
}

