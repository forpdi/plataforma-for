var async = require('async');
module.exports = function(app) {
  var mysqlDs = app.dataSources.mysqlDS;
  //create all models
  async.parallel({
    reviewers: async.apply(createReviewers),
    coffeeShops: async.apply(createCoffeeShops),
  }, function(err, results) {
    if (err) throw err;
    createReviews(results.reviewers, results.coffeeShops, function(err) {
      console.log('> models created sucessfully');
    });
  });
  //create reviewers
  function createReviewers(cb) {
    mysqlDs.automigrate('Person', function(err) {
      if (err) return cb(err);
      var Person = app.models.Person;
      Person.create([
        {email: 'renatorro@comp.ufla.br', password: '12345', username: "01480807664"}
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
  //create reviews
  function createReviews(reviewers, coffeeShops, cb) {
    mysqlDs.automigrate('PersonEmail', function(err) {
      if (err) return cb(err);
      var PersonEmail = app.models.PersonEmail;
      var DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
      PersonEmail.create([
        {
          creation: Date.now() - (DAY_IN_MILLISECONDS * 4),
          confirmed: true,
          email: 'renatorroliveira@gmail.com',
          personId: reviewers[0].id
        }
      ], cb);
    });
  }
};