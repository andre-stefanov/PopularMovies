node {
    stage ('Checkout') {
        checkout scm
    }

    stage ('Setup Environment') {
        env.ANDROID_HOME = "${env.HOME}/android-sdk-linux"
    }

    stage ('Test') {
        sh './gradlew clean verify'
    }

    stage ('Build') {
        try {
            sh './gradlew build'
        } finally {
            step([$class: 'LintPublisher'])
            step(
                    [$class                     : 'CheckStylePublisher', pattern: 'app/build/reports/checkstyle/checkstyle.xml',
                     usePreviousBuildAsReference: true])
        }
    }
}