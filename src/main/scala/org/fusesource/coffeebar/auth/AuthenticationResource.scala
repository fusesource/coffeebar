/**
 * Copyright (C) 2011, FuseSource Corp.  All rights reserved.
 * http://fusesource.com
 *
 * The software in this package is published under the terms of the
 * AGPL license a copy of which has been included with this distribution
 * in the license.txt file.
 */

package org.fusesource.coffeebar.auth

import javax.ws.rs._
import javax.ws.rs.core._
import javax.servlet.http._
import org.codehaus.jackson.annotate._
import com.sun.jersey.api.core.ResourceContext

class Principal {
  @JsonProperty
  var username = ""
}

@Path("auth")
@JsonAutoDetect(Array(JsonMethod.NONE))
@Produces(Array(MediaType.APPLICATION_JSON))
class AuthenticationResource {

  @Context
  var rc: ResourceContext = null;

  @POST
  @Path("login")
  def login(@Context request: HttpServletRequest, @FormParam("username") username: String, @FormParam("password") password: String): Boolean = {
    val auth: Authenticator = new SimpleAuthenticator // rc.getResource(classOf[Authenticator]);
    if (auth==null || auth.authenticate(username, password)) {
      val session: HttpSession = request.getSession(true);
      session.setAttribute("username", username)
      return true;
    } else {
      return false;
    }
  }

  @GET
  @Path("logout")
  def logout(@Context request: HttpServletRequest): Boolean = {
    val session: HttpSession = request.getSession(false);
    if (session != null) {
      session.invalidate()
    }
    true
  }

  @GET
  @Path("whoami")
  def whoami(@Context request: HttpServletRequest): Principal = {
    val session: HttpSession = request.getSession(false);
    val principal = new Principal()
    if (session != null && session.getAttribute("username") != null) {
      principal.username = session.getAttribute("username").asInstanceOf[String]
    }
    return principal
  }

}