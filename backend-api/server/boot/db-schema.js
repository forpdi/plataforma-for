var async = require('async');
module.exports = function(app) {
  var mysqlDs = app.dataSources.mysqlDS;
  //create all models
  async.parallel({
    reviewers: async.apply(createReviewers),
    coffeeShops: async.apply(createCoffeeShops),
  }, function(err, results) {
    if (err) throw err;
    mysqlDs.automigrate('AccessToken', function(err) {
      if (err) throw err;
    });
    console.log('> models created sucessfully');
  });
  //create reviewers
  function createReviewers(cb) {
    mysqlDs.automigrate('Person', function(err) {
      if (err) return cb(err);
      var Person = app.models.Person;
      Person.create([
        {
          name: "Renato Oliveira",
          email: 'renatorro@comp.ufla.br',
          password: '12345',
          username: "01480807664",
          cellphone: "+5531998239631",
          birthdate: Date.now()
        }
      ], cb);
    });
  }
  //create coffee shops
  function createCoffeeShops(cb) {
    mysqlDs.automigrate('Company', function(err) {
      if (err) return cb(err);
      var Company = app.models.Company;
      Company.create([
        {name: 'UNIFAL', logo: 'http://www.unifal-mg.edu.br/portal/imagens/logo-vertical.png'}
      ], cb);
    });
  }
};