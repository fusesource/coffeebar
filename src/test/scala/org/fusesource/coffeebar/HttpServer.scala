package org.fusesource.coffeebar

/**
 * Copyright (C) 2010-2011, FuseSource Corp.  All rights reserved.
 *
 *     http://fusesource.com
 *
 * The software in this package is published under the terms of the
 * CDDL license a copy of which has been included with this distribution
 * in the license.txt file.
 */

import org.eclipse.jetty.server.{Connector, Server}
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.nio.SelectChannelConnector
import java.io.File
import java.lang.String
import java.net.URI

/**
 * <p>
 * </p>
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
object HttpServer {

 def main(args: Array[String]):Unit = {

    // lets set the system properties to dev mode
    System.setProperty("scalate.mode", "dev")

    val bind = args.headOption.getOrElse("http://0.0.0.0:8080/")
    val bind_uri = new URI(bind)
    val prefix = "/"+bind_uri.getPath.stripPrefix("/")
    val host = bind_uri.getHost
    val port = bind_uri.getPort

    var connector = new SelectChannelConnector
    connector.setHost(host)
    connector.setPort(port)

    def webapp:File = {

      // the war might be on the classpath..
      val resource = HttpServer.getClass.getName.replace('.', '/')+".class"

      var url = HttpServer.getClass.getClassLoader.getResource(resource)
      if(url!=null) {
        if( url.getProtocol == "jar") {
          // we are probably being run from a maven build..
          url = new java.net.URL( url.getFile.stripSuffix("!/"+resource) )
          val jar = new File( url.getFile )
          if( jar.isFile ) {
            return new File(jar.getParentFile, (jar.getName.stripSuffix(".jar")+".war"))
          }
        } else if( url.getProtocol == "file") {
          // we are probably being run from an IDE...
          val rc = new File( url.getFile.stripSuffix("/"+resource) )
          if( rc.isDirectory ) {
            return new File(new File(new File(new File(rc, ".."), ".."), "target"), "webapp")
          }
        }
      }
      return null
    }

    def admin_app = {
      var app_context = new WebAppContext
      app_context.setContextPath(prefix)
      app_context.setWar(webapp.getCanonicalPath)
      app_context
    }

    val server = new Server
    server.setHandler(admin_app)
    server.setConnectors(Array[Connector](connector))
    server.start

    while(true) {
      Thread.sleep(1000)
    }
  }

}

