plugins {
    application
}

repositories {
    mavenCentral()
}

tasks.withType<JavaCompile> {
    val compilerArgs = options.compilerArgs
    //NOTE: This is required for @P
    compilerArgs.addAll(listOf("-parameters"))
}
application {
    mainClass.set("com.splunk.example.HttpHeaderAttributesMain")
    applicationDefaultJvmArgs = listOf(
        "-javaagent:splunk-otel-javaagent-1.18.0.jar",
        "-Dotel.javaagent.debug=true",
        "-Dotel.resource.attributes=deployment.environment=http-testenv",
        "-Dotel.service.name=HttpHeaderAttributes",
        "-Dotel.instrumentation.http.capture-headers.client.request=demeanor",
        "-Dotel.instrumentation.http.capture-headers.client.response=originator",
        "-Dotel.instrumentation.http.capture-headers.server.request=demeanor,user-agent",
        "-Dotel.instrumentation.http.capture-headers.server.response=originator",
    )
}

dependencies {
    implementation("com.sparkjava:spark-core:2.9.4")
}
