pipeline {
  agent {
    docker {
      image 'registry.gmasil.de/docker/maven-build-container'
      args '-v /maven:/maven -e JAVA_TOOL_OPTIONS=\'-Duser.home=/maven\' -u root:root'
    }
  }
  environment {
    MAVEN_PROJECT_GROUPID_ARTIFACTID = "${sh(script:'mvn -q -Dexec.executable=echo -Dexec.args=\'${project.groupId}:${project.artifactId}\' --non-recursive exec:exec 2>/dev/null | tr -d \'\n\r\'', returnStdout: true)}"
    MAVEN_PROJECT_NAME = "${sh(script:'mvn -q -Dexec.executable=echo -Dexec.args=\'${project.name}\' --non-recursive exec:exec 2>/dev/null | tr -d \'\n\r\'', returnStdout: true)}"
  }
  stages {
    stage('compile') {
      steps {
        sh 'mvn clean package --fail-at-end -DskipTests'
      }
    }
    stage('test') {
      steps {
        sh 'mvn test --fail-at-end'
      }
    }
    stage('integration-test') {
      steps {
        sh 'xvfb-run mvn verify --fail-at-end -P integration-tests-only'
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
}
