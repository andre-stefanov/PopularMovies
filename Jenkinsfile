node {
    stage('Checkout') {
        checkout scm
    }

    stage('Setup Environment') {
        env.ANDROID_HOME = "${env.HOME}/android-sdk-linux"
        if (fileExists(env.ANDROID_HOME)) {
            echo 'Android SDK already exists'
        } else {
            stage 'Setup Android SDK'

            sh 'curl --fail --output android-sdk.zip https://dl.google.com/android/repository/tools_r25.2.3-linux.zip'
            fileUnZipOperation('android-sdk.zip', '.')
            sh 'mv android-sdk-linux "$ANDROID_HOME"'
        }
    }

    stage('Build') {
        try {
            withCredentials([[$class: 'StringBinding', credentialsId: 'tmdb-api-key', variable: 'TMDB_API_KEY']]) {
                sh '''
                    ./gradlew -PtmdbApiKey=$TMDB_API_KEY build
                '''
            }
        } finally {
            step([$class: 'LintPublisher'])
        }
    }
}