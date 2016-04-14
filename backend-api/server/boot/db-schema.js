var async = require('async');
module.exports = function(app) {
  //data sources
  var mongoDs = app.dataSources.mongoDS;
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
    mongoDs.automigrate('Person', function(err) {
      if (err) return cb(err);
      var Person = app.models.Person;
      Person.create([
        {email: 'foo@bar.com', password: 'foobar'},
        {email: 'john@doe.com', password: 'johndoe'},
        {email: 'jane@doe.com', password: 'janedoe'}
      ], cb);
    });
  }
  //create coffee shops
  function createCoffeeShops(cb) {
    mysqlDs.automigrate('Company', function(err) {
      if (err) return cb(err);
      var Company = app.models.Company;
      Company.create([
        {name: 'Bel Cafe', logo: 'Vancouver'},
        {name: 'Three Bees Coffee House', logo: 'San Mateo'},
        {name: 'Caffe Artigiano', logo: 'Vancouver'},
      ], cb);
    });
  }
  //create reviews
  function createReviews(reviewers, coffeeShops, cb) {
    mongoDs.automigrate('PersonEmail', function(err) {
      if (err) return cb(err);
      var PersonEmail = app.models.PersonEmail;
      var DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;
      PersonEmail.create([
        {
          creation: Date.now() - (DAY_IN_MILLISECONDS * 4),
          confirmed: true,
          email: 'bla@bla.com',
          personId: reviewers[0].id,
          companyId: coffeeShops[0].id
        },
        {
          creation: Date.now() - (DAY_IN_MILLISECONDS * 3),
          confirmed: true,
          email: 'ble@ble.com',
          personId: reviewers[1].id,
          companyId: coffeeShops[1].id
        },
        {
          creation: Date.now() - (DAY_IN_MILLISECONDS * 2),
          confirmed: true,
          email: 'bli@bli.com',
          personId: reviewers[1].id,
          companyId: coffeeShops[1].id
        },
        {
          creation: Date.now() - (DAY_IN_MILLISECONDS),
          confirmed: true,
          email: 'blo@blo.com',
          personId: reviewers[2].id,
          companyId: coffeeShops[2].id
        }
      ], cb);
    });
  }
};