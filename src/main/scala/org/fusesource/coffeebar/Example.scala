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