module.exports = function(Person) {
  Person.on('resetPasswordRequest', function(info) {
    var url = 'http://garrosh:3000/#/reset-password/';
    var html = 'Click <a href="' + url +
        info.accessToken.id + '">here</a> to reset your password';
    //'here' in above html is linked to : 'http://<host:port>/#/reset-password/<short-lived/temporary access token>'
    Person.app.models.Email.send({
      to: info.email,
      from: info.email,
      subject: 'Password reset',
      html: html
    }, function(err) {
      if (err) return console.error('> error sending password reset email:',err);
      console.log('> sending password reset email to:', info.email);
    });
  });

  Person.app.post('/reset-password', function(req, res, next) {
    if (!req.accessToken) return res.sendStatus(401);

    //verify passwords match
    if (!req.body.password ||
        !req.body.confirmation ||
        req.body.password !== req.body.confirmation) {
      return res.sendStatus(400, new Error('Passwords do not match'));
    }

    Person.findById(req.accessToken.userId, function(err, user) {
      if (err) return res.sendStatus(404);
      user.updateAttribute('password', req.body.password, function(err, user) {
      if (err) return res.sendStatus(404);
        console.log('> password reset processed successfully');
        res.render('response', {
          title: 'Password reset success',
          content: 'Your password has been reset successfully',
          redirectTo: '/',
          redirectToLinkText: 'Log in'
        });
      });
    });
  });
};
};
