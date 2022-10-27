pipeline {
  agent {
    label 'windows'
  }
  environment {
    MAVEN_ARTIFACT = sh(script: "mvn -q -Dexec.executable=echo -Dexec.args='\${project.groupId}:\${project.artifactId}' --non-recursive exec:exec", returnStdout: true).trim()
    MAVEN_PROJECT_NAME = sh(script: "mvn -q -Dexec.executable=echo -Dexec.args='\${project.name}' --non-recursive exec:exec", returnStdout: true).trim()
  }
  stages {
    stage('compile') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }
    stage('analyze') {
      environment {
        SONAR_TOKEN = credentials('SONAR_TOKEN')
      }
      steps {
        script {
          def safeBranch = env.GIT_BRANCH.replaceAll("[^a-zA-Z0-9_:{\\.-]+", "-")
          sh("mvn sonar:sonar --no-transfer-progress -Dsonar.host.url=https://sonar.gmasil.de -Dsonar.login=\$SONAR_TOKEN -Dsonar.projectKey=${env.MAVEN_ARTIFACT}:${safeBranch} \"-Dsonar.projectName=${env.MAVEN_PROJECT_NAME} (${env.GIT_BRANCH})\"")
        }
      }
    }
  }
  post {
    always {
      archiveArtifacts artifacts: 'target/MHW-CTC-Editor.exe', fingerprint: true
      cleanWs()
      dir("${env.WORKSPACE}@tmp") {
        deleteDir()
      }
    }
  }
}
