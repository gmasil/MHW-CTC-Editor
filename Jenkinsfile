pipeline {
  agent {
    label 'windows'
  }
  environment {
    MAVEN_PROJECT_GROUPID_ARTIFACTID = "${sh(script:'mvn -q -Dexec.executable=echo -Dexec.args=\'${project.groupId}:${project.artifactId}\' --non-recursive exec:exec 2>/dev/null | tr -d \'\n\r\'', returnStdout: true)}"
    MAVEN_PROJECT_NAME = "${sh(script:'mvn -q -Dexec.executable=echo -Dexec.args=\'${project.name}\' --non-recursive exec:exec 2>/dev/null | tr -d \'\n\r\'', returnStdout: true)}"
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
        sh 'mvn sonar:sonar -Dsonar.host.url=https://sonar.gmasil.de -Dsonar.login=' + env.SONAR_TOKEN + ' -Dsonar.projectKey=' + env.MAVEN_PROJECT_GROUPID_ARTIFACTID + ':' + env.GIT_BRANCH + ' \\"-Dsonar.projectName=' + MAVEN_PROJECT_NAME + ' \\(' + env.GIT_BRANCH + '\\)'
      }
    }
  }
  post {
    always {
      archiveArtifacts artifacts: 'target/MHW-CTC-Editor.exe', fingerprint: true
    }
  }
}
