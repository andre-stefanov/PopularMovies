node {
    stage('Checkout') {
        checkout scm
    }

    stage('Setup Environment') {
        env.ANDROID_HOME = "${env.HOME}/android-sdk-linux"
    }

    stage('Build') {
        try {
            sh "./gradlew -PtmdbApiKey=$System.env.TMDB_API_KEY build"
        } finally {
            step([$class: 'LintPublisher'])
        }
    }
}