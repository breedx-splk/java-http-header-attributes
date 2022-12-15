# java-http-header-attributes

We often get asked how to copy http request/response headers into 
OpenTelemetry span attributes. The java instrumentation agent does not 
do this by default, primarily because it risks leaking sensitive data.
Fortunately, there is a way to instruct the java agent to collect http headers. 

This project is quick demonstrative example of capturing http headers as 
span attributes.

The main application is `HttpHeaderAttributesMain`. This application spins up a small
HTTP server on port 8182, and a client that sends a request to the server once a second.

This is an unusual ab/use of HTTP and is only intended to demonstrate how to tell
the java instrumentation to copy a header into an attribute.

### Client request

The client sends a `POST` request with a `demeanor` HTTP header and the name
of the person in the request body.

```
POST /greeting HTTP/1.1
Content-Length: 8
Host: localhost:8182
User-Agent: Java-http-client/11.0.9.1
Content-Type: text/plain
demeanor: TIRED

Benjamin
```

### Server responses:

The server responds with an `originator` header whose value is the uppercase
version of the person's name. The body contains the person's name and an 
emoji corresponding to their demeanor.

```
HTTP/1.1 200 OK
Date: Thu, 15 Dec 2022 21:39:27 GMT
Content-Type: text/plain
originator: BENJAMIN
Transfer-Encoding: chunked
Server: Jetty(9.4.48.v20220622)

Benjamin => 🥱
```

## Running

To build and run the example, simply run the gradle build target `run`. 
You must have java 11+ installed.

```
./gradlew run
```

## Configuration

Configuration is provided to the java agent via system properties passed
on the commandline. No manual code changes required!

If you look in the [`build.gradle.kts`](build.gradle.kts) file, you will 
notice several JVM commandline args. These are used to wire up the java agent,
and to tell it to 
Add these system properties:
```
-javaagent:splunk-otel-javaagent-1.18.0.jar
-Dotel.javaagent.debug=true
-Dotel.resource.attributes=deployment.environment=http-testenv
-Dotel.service.name=HttpHeaderAttributes
-Dotel.instrumentation.http.capture-headers.client.request=demeanor
-Dotel.instrumentation.http.capture-headers.client.response=originator
-Dotel.instrumentation.http.capture-headers.server.request=demeanor,user-agent
-Dotel.instrumentation.http.capture-headers.server.response=originator
```

The last 4 are the interesting ones! These tell the agent which headers to capture. 
Specifically, we are asking the java agent to capture these 4 headers:

* client instrumentation - outgoing request - `demeanor`
* client instrumentation - incoming response - `originator`
* server instrumentation - incoming request `demeanor` and `user-agent`
* server instrumentation - outgoign response - `originator`

If your application is purely a client or purely a server, you would probably only need
one half of this, but we've provided both directions on both sides as a demonstration.

## Results

In the APM trace view, we have generated a simple trace with one client span and one server span:

<img width="943" alt="image" src="https://user-images.githubusercontent.com/75337021/207979286-d9285759-671f-400c-91b5-a73ec7aae369.png">

If we expand the topmost client span and expanding the client request span:

<img width="600" alt="client-span" src="https://user-images.githubusercontent.com/75337021/207980022-6d085818-795b-4d65-93b6-7faa5c83a0a3.png">

And then expand the server span:
<img width="600" alt="server-span" src="https://user-images.githubusercontent.com/75337021/207980829-33a1a514-288b-4f7b-bc97-4fd0dbf67d8d.png">
