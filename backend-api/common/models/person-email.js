module.exports = function(PersonEmail) {
  /*PersonEmail.beforeRemote('create', function(context, user, next) {
    var req = context.req;
    req.body.creation = Date.now();
    req.body.personId = req.accessToken.userId;
    console.log("Remote hook executed:", req.body);
    next();
  });*/
};
