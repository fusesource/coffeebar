#
# Copyright (C) 2009-2011 the original author or authors.
# See the notice.md file distributed with this work for additional
# information regarding copyright ownership.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

define [
  "models/app"
  "views/jade"
], (app, jade) ->
  SigninController = CoffeeBar.TemplateController.extend

    initialize: ->
      @template = jade["signin/index.jade"]
      @template_data = -> {
        username: "admin"
      }

      @bind "render", =>
        @$("form").submit ->
            username = $(this).find("input[name=\"username\"]").val()
            password = $(this).find("input[name=\"password\"]").val()

            $.ajax
              url: "auth/login"
              dataType: "json"
              type: "POST"
              data:
                username: username
                password: password

              success: (data) ->
                if data
                   app.whoami.fetch()
                   app.router.navigate("#/", true)
                else
                  app.flash
                    kind: "error"
                    title: "Invalid username or password"
                  app.router.navigate "#/signin", false

              error: (data) ->
                app.flash
                  kind: "error"
                  title: "Error communicating with the server."
                app.router.navigate "#/signin", true

            false


  app.router.route "/signin", "signin", (tab, test) ->
    app.menu []      
    app.page new SigninController

  app.router.route "/signout", "signout", (tab, test) ->
    $.ajax
      url: "auth/logout.json"
      dataType: "json"
      success: (data) ->
        app.whoami.set({username:""})
        app.router.navigate "/signin", true
        app.flash
            kind: "info"
            title: "Logged out! "
            message: "Your session has been closed."

      error: (data) ->
        app.whoami.set({username:""})
        app.flash
          kind: "error"
          title: "Error communicating with the server."

  SigninController