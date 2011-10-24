package org.fusesource.coffeebar

import javax.ws.rs._
import javax.ws.rs.core._
import org.codehaus.jackson.annotate._
import java.util.Date

@Path("example")
@JsonAutoDetect(Array(JsonMethod.NONE))
@Produces(Array(MediaType.APPLICATION_JSON))
class Example {

  @JsonProperty
  def name = "Hiram"

  @JsonProperty
  def address = Array(
      "777 Clearview Dr.",
      "Wesley Chapel, FL 44545"
    )

  @JsonProperty
  def time = (new Date).toString

  @GET
  def get = this
  
}