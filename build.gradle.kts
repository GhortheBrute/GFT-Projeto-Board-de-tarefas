plugins {
    id("java")
    id("org.liquibase.gradle") version "2.2.0"
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.liquibase/liquibase-core
    implementation("org.liquibase:liquibase-core:4.33.0")

    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:8.4.0")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    implementation("org.projectlombok:lombok:1.18.38")

    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.yml",
            "url" to "jdbc:mysql://localhost/board",
            "username" to (System.getenv("DB_MYSQL_USER") ?: "default_user"),
            "password" to (System.getenv("DB_MYSQL_PASSWORD") ?: "default_password"),
            "driver" to "com.mysql.cj.jdbc.Driver"
        )
    }

    runList = "main"
}



tasks.test {
    useJUnitPlatform()
}