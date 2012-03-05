# CoffeeBar

CoffeeBar is a CoffeScript based Web Framework for Rapid Application Development of the UI and it 
communicates using REST JSON services implemented in Java via JAXRS.  It assembles 
together bunch of awesome tools such as: 
[CoffeeScript](http://jashkenas.github.com/coffee-script/), 
[Backbone.js](http://documentcloud.github.com/backbone/), 
[Underscore.js](http://documentcloud.github.com/underscore/), 
[CoffeeJade](https://github.com/fusesource/coffeejade),
[jQuery](http://jquery.com/),
and [Require.js](http://requirejs.org/).

# Requirements

* [Maven 3](http://maven.apache.org/download.html)

# Directory Structure

CoffeeBar uses a standard maven layout to build a WAR that can be deployed
to a Java servlet container.  The main directories you need to be familiar with
are:

    src/
      main/
        webapp/
          app/ - Your client side CoffeeScript Application
            controllers/ - Your Application Controlers
            models/ - Your Application Models
            views/ - Your Application views
            frameworks/ - 3rd party CSS and JavaScript
          img/ - image assets
          styles/ - CSS assets
          WEB-INF/ 
        scala/ - Your Server Side REST services

# Development Mode

Compile an continue looking for file to 

Start the embedded HTTP server by running `mvn jetty:run` or by
running the `org.fusesource.coffeebar.HttpServer` class in your IDE.

In another terminal window you should run `mvn brew:compile -Dbrew.watch=true` 
to scan for changes in your .coffee or .jade files and compile them to java script.

Open you browser to http://127.0.0.1:8080 to view the app.  If you are going
to be updating the app make sure you use the latest Chrome beta which supports
disabling the browser cache.  

# Production Mode

Build with `mvn install`.  The resulting war with minified JavaScript and CSS will
be in the `target` directory.

