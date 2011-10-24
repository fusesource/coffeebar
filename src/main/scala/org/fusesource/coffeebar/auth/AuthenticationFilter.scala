/**
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 * http://fusesource.com
 *
 * The software in this package is published under the terms of the
 * AGPL license a copy of which has been included with this distribution
 * in the license.txt file.
 */

package org.fusesource.coffeebar.auth

import com.sun.jersey.spi.container.{ContainerRequest, ContainerRequestFilter, ResourceFilter}
import javax.ws.rs.core.Context
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import javax.ws.rs.WebApplicationException
import java.io.UnsupportedEncodingException
import com.sun.jersey.core.util.Base64
import com.sun.jersey.api.core.ResourceContext

class AuthenticationFilter extends ContainerRequestFilter {

  val HEADER_WWW_AUTHENTICATE: String = "WWW-Authenticate"
  val HEADER_AUTHORIZATION: String = "Authorization"
  val AUTHENTICATION_SCHEME_BASIC: String = "Basic"

  @Context
  var http_request: HttpServletRequest = null;

  @Context
  var http_response: HttpServletResponse = null;

  @Context
  var rc: ResourceContext = null;

  val LOGIN_URL = "/auth/login"
  val LOGOUT_URL = "/auth/logout"

  private def decode_base64(value: String): String = {
    var transformed: Array[Byte] = Base64.decode(value)
    try {
      return new String(transformed, "ISO-8859-1")
    } catch {
      case uee: UnsupportedEncodingException => {
        return new String(transformed)
      }
    }
  }

  def filter(request: ContainerRequest): ContainerRequest = {
    val session = http_request.getSession(false)
    val path = http_request.getServletPath()

    if (session != null
      || path == "/"
      || path == "/index.html"
      || path.startsWith("/favicon.")
      || path.startsWith("/img/")
      || path.startsWith("/styles/")
      || path.startsWith("/auth/login")
      || path.startsWith("/auth/logout")
      || path.startsWith("/app/")
    ) {
        return request;
    } else {
      val auth_prompt = http_request.getHeader("AuthPrompt");

      if (session == null && auth_prompt != null && auth_prompt == "false") {
        throw new WebApplicationException(401);
      } else {
        var auth_header = http_request.getHeader(HEADER_AUTHORIZATION)
        if (auth_header != null && auth_header.length > 0) {
          auth_header = auth_header.trim
          var blank = auth_header.indexOf(' ')
          if (blank > 0) {
            var auth_type = auth_header.substring(0, blank)
            var auth_info = auth_header.substring(blank).trim
            if (auth_type.equalsIgnoreCase(AUTHENTICATION_SCHEME_BASIC)) {
              try {
                var srcString = decode_base64(auth_info)
                var i = srcString.indexOf(':')
                var username: String = srcString.substring(0, i)
                var password: String = srcString.substring(i + 1)

                val auth: Authenticator = new SimpleAuthenticator // rc.getResource(classOf[Authenticator]);

                if (auth==null || auth.authenticate(username, password)) {
                   return request
                } else {
                  throw new WebApplicationException(401)
                }

              } catch {
                case e: Exception =>
              }
            }
          }
          throw new WebApplicationException(401)
        } else {
          val http_realm = "CoffeeBar"
          http_response.addHeader(HEADER_WWW_AUTHENTICATE, AUTHENTICATION_SCHEME_BASIC + " realm=\"" + http_realm + "\"")
          throw new WebApplicationException(401);
        }
      }
    }



  }

}