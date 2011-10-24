/**
 * Copyright (C) 2009-2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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