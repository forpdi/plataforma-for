
module.exports = function(app) {
  app.dataSources.mysqlDS.automigrate('Company', function(err) {
    if (err) throw err;
 
    app.models.Company.create([
      {name: 'ForPDI', logo: 'example.png', description: null, deleted: false},
      {name: 'UIFAL', logo: 'unifal.png', description: 'Universidade Federal de Alfenas', deleted: false},
      {name: 'UFLA', logo: 'ufla.png', description: 'Universidade Federal de Lavras', deleted: false}
    ], function(err, companies) {
      if (err) throw err;
 
      console.log('Models created: \n', companies);
    });
  });
};
