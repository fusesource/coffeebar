package org.fusesource.coffeebar.auth

/**
 * Created by IntelliJ IDEA.
 * User: chirino
 * Date: 10/23/11
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */

trait Authenticator {
  def authenticate(username: String, password: String):Boolean
}