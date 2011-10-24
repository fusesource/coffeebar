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
  "models/whoami" 
], (WhoAmI) ->
  
  AppModel = CoffeeBar.Model.extend
    defaults: 
      menu: []
      loading: false
      username: ""
      flash: null
      page: null
      poll_interval: 0
        
  Router = Backbone.Router.extend({})
  model = new AppModel
  app =
    router: new Router()
    whoami: new WhoAmI()
    model: model
    flash: model.property("flash")
    page: model.property("page")
    menu: model.property("menu")
  
  update_menu = ->
    menu = []
    menu.push
      href: "#/example"
      label: "Example"
      
    app.menu menu

  app.model.set({url: window.location.hash})
  $(window).bind('hashchange', (url)->
    app.model.set({url: window.location.hash})
  )


  # Update the username when the whoami info changes..
  app.whoami.bind "change", ->
    app.model.set username: app.whoami.get("username")

  app.handle_ajax_error = (resp, next)->
    if resp.status == 401
      unless _.isEmpty(app.model.get("username"))
        app.flash 
          kind: "error"
          title: "Unauthorized! "
          message: "You are not authorized to do perform that action.  Perhaps you need to sign in under a different username."
          actions: "<a href='#/signin' class='btn'>Sign In</a>"
      else
        app.router.navigate "/signin", true
    else
      if next
        next(resp)
      else
        app.flash 
          kind: "error"
          title: "Server Error"
          message: "The server is expirencing some problems right now.  Try again later."
  
  original_sync = Backbone.sync
  Backbone.sync = (method, model, options) ->
    getUrl = (object) ->
      return null  unless (object and object.url)
      (if _.isFunction(object.url) then object.url() else object.url)
    
    params = _.extend({}, options)
    unless params.url
      params.url = getUrl(model) or urlError()
      parts = params.url.split("?", 2)
      parts[0] += ".json"
      params.url = parts.join("?")
      
    params.headers = _.extend({}, params.headers)
    params.headers["AuthPrompt"] = "false"
    
    original_error = params.error
    params.error = (resp) ->
      app.handle_ajax_error(resp, original_error)
    
    original_sync method, model, params
    
  app
