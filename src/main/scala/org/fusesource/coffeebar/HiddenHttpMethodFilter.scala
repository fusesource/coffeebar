package org.fusesource.coffeebar

import com.sun.jersey.spi.container.{ContainerRequest, ContainerRequestFilter}

/**
 * Request filter which accept both - X-HTTP-Method-Override header and _method post paremter.
 *
 * @author ldywicki
 */
class HiddenHttpMethodFilter extends ContainerRequestFilter {

  def filter(request : ContainerRequest) : ContainerRequest = {
    if (!request.getMethod.equalsIgnoreCase("POST"))
        request

    val headerOver = request.getRequestHeaders.getFirst("X-HTTP-Method-Override")
    val paramOver = request.getFormParameters.getFirst("_method")

    def solve(_method : String) = {
      val method = _method.trim();
      if (!method.isEmpty) {
        request.setMethod(method)
      }
    }

    if (paramOver != null) {
      solve(paramOver)
    }
    if (headerOver != null) {
      solve(headerOver)
    }
    request
  }


}