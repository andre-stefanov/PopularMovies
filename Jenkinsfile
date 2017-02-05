node {
    stage('Checkout') {
        checkout scm
    }

    stage('Setup Environment') {
        env.ANDROID_HOME = "${env.HOME}/android-sdk-linux"
    }

    stage('Build') {
        try {
            withCredentials([[$class: StringBinding, credentialsId: 'tmdb-api-key', variable: 'TMDB_API_KEY']]) {
                sh '''
                    ./gradlew -PtmdbApiKey=$TMDB_API_KEY build
                '''
            }
        } finally {
            step([$class: 'LintPublisher'])
        }
    }
}