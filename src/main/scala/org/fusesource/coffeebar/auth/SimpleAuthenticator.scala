package org.fusesource.coffeebar.auth

import com.sun.jersey.spi.resource.Singleton

class SimpleAuthenticator extends Authenticator {

  def authenticate(username: String, password: String): Boolean = {
    username == password
  }

}