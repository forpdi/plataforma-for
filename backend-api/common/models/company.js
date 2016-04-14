module.exports = function(Company) {
  Company.getName = function(id, cb) {
    Company.findById(id, function (err, instance) {
        var response = "The name is: " + instance.name;
        cb(null, response);
        console.log(response);
    });
  }

  Company.remoteMethod (
        'getName',
        {
          http: {path: '/getname', verb: 'get'},
          accepts: {arg: 'id', type: 'number', http: { source: 'query' } },
          returns: {arg: 'name', type: 'string'}
        }
    );
};