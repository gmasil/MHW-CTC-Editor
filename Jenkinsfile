pipeline {
  agent {
    label 'windows'
  }
  stages {
    stage('compile') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }
  }
  post {
    always {
      archiveArtifacts artifacts: 'target/MHW-CTC-Editor.exe', fingerprint: true
    }
  }
}
