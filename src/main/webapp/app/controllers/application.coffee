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
  "models/whoami"
], (app, jade, WhoAmI) ->


  LoadingPage = CoffeeBar.TemplateController.extend
    template: jade["application/loading.jade"]
    
  CoffeeBar.Controller.extend
    page_container: $("#page_container")
      
    initialize: ->
      topbar_nav_container = new CoffeeBar.TemplateController
        el: $("#topbar_nav_container")
        template: jade["application/menu.jade"]
        template_data: ->
          model = app.model.toJSON()
          items: model.menu
          active: model.url
          
      app.model.bind "change:menu", => topbar_nav_container.render()
      app.model.bind "change:url", => topbar_nav_container.render()
      
      topbar_nav_container.bind "render", (controller)-> bind_menu_actions controller.el

      topbar_user_container = new CoffeeBar.TemplateController
        el: $("#topbar_user_container")
        template: jade["application/user_menu.jade"]
        template_data: ->
          app.model.toJSON()

      topbar_user_container.bind "render", (controller)-> bind_menu_actions controller.el
      app.model.bind "change:username", => topbar_user_container.render()
      
      flash_container = new (CoffeeBar.TemplateController.extend
        events: 
          "click  .close": "close"
        close: -> 
          app.flash(null)
      )(
        el: $("#flash_container")
        template: jade["application/flash.jade"]
        template_data: -> 
          flash: app.model.get("flash")
      )
      app.model.bind "change:flash", flash_container.render, flash_container
      
      app.model.bind "change:page", => 
        page = app.page()
        app.flash(null)
        @page_container.empty()
        @page_container.append page.render().el if page
      
      app.model.bind "change:poll_interval", =>
        @stop_poll()
        interval = app.model.get("poll_interval")
        if interval 
          @poll_interval = setInterval (=>@poll()), interval
      
      app.whoami.fetch();

    stop_poll: ->
      if @poll_interval
        clearInterval @poll_interval
        @poll_interval = null

    poll: ->
      page = app.page()
      page.poll()  if page and page.poll
