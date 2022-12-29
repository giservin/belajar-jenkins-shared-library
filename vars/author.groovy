def call(List commands) {
    for (command in commands) {
        sh("./mvnw ${command}")
    }
}