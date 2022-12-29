def call(List commands) {
    for (command in commands) {
        sh("./mvnw ${command}")
    }
}

def nama(String nama) {
    echo nama
}

def person(Map person) {
    echo "Hello ${person.firstName} ${person.lastName}"
}