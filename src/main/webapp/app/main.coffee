define [ 
 "models/app" 
 "controllers/application", 
 "controllers/signin",
 "controllers/example",
], (app, Application) ->
  $ ->
    application = new Application()
    app.model.set poll_interval: 1000
    app.router.navigate("#/example", true)  unless Backbone.history.start({})
