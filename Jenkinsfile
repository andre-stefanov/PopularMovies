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
            withCredentials([
                    string(credentialsId: 'tmdb-api-key', variable: 'TMDB_API_KEY'),
                    file(credentialsId: 'popular-movies-keystore', variable: 'storeFile'),
                    string(credentialsId: 'popular-movies-keystore-password', variable: 'storePassword'),
                    string(credentialsId: 'popular-movies-keystore-alias', variable: 'keyAlias'),
                    string(credentialsId: 'popular-movies-alias-password', variable: 'keyPassword'),
            ]) {
                sh '''
                    ./gradlew -PtmdbApiKey=$TMDB_API_KEY -PkeystoreFile=$storeFile -PkeystorePassword=$storePassword -PkeystoreKeyAlias=$keyAlias -PkeystoreKeyPassword=$keyPassword clean build
                '''
            }
        } finally {
            step([$class: 'LintPublisher'])
        }
    }

    stage('Artifacts') {
        archiveArtifacts artifacts: 'app/build/outputs/apk/*.apk', fingerprint: true
    }
}