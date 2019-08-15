# Dropwizard demo

The "widget service" is a Java [Dropwizard](http://www.dropwizard.io/0.9.3/docs/)
REST service that manages an in-memory datastore of "widgets."
Supported features: you can get all widgets, get a particular existing
widget, add (post) a new widget, and delete an existing widget.
Security is http basic authentication with defined Roles.
The Dropwizard YML config file initializes the service
with two users and two widgets.

Build:
~~~~
mvn package
~~~~

Run server:
~~~~
java -jar target/dropwizard-0.9-01-widget-service-1.0.0-SNAPSHOT.jar \
     server src/main/resources/widget-config.yml
~~~~

See the healthcheck:
~~~~
curl localhost:8080/admin/healthcheck
~~~~


Make an authenticated request to get the current list of widgets:
~~~~
curl http://charlie:hello@localhost:8080/widgets
~~~~

Post a new widget:
~~~~
curl http://admin:admin@localhost:8080/widgets -X POST \
    -H 'Content-Type: application/json' \
    -d '{"name":"Small Box of Things", "color":"bluish gray", "size":5}'
~~~~

See the integration test for more feature demonstrations.