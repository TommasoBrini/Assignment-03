plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor-typed
    implementation("com.typesafe.akka:akka-actor-typed_2.12:2.8.4")
    implementation("com.rabbitmq:amqp-client:5.17.1")
}

tasks.test {
    useJUnitPlatform()
}