# A CAS sample application using Spring Boot

## Running

```shell
./gradlew clean build
java -jar build/libs/cas-test-1.0-SNAPSHOT.jar --local.serverName=http://localhost:8888 --cas.serverUrlPrefix=https://test.scaldingspoon.org/cas
```

Make sure to use the correct host names for your environment

Parameter | Notes
--- | ---
server.port | Port to run the application on. default is `8888`
local.serverName | Server name used for CAS. default is `http://localhost:8888`
cas.serverUrlPrefix | Server prefix for the CAS server. Default is `https://test.scaldingspoon.org/cas`
spring.profiles.active | what profile to use. This mostly determines what protocol to use for CAS authentication and validation. Valid options are `saml` and `cas`. Default is `saml`.
