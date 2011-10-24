define [ 
  "frameworks"
], ->
  CoffeeBar.Model.extend
    url: "example"
    defaults:
      "name":null
      "address":[]
      "time":null