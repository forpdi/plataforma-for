var path = require('path');
module.exports = require("./webpack.config.js");
module.exports.output.path = path.join(__dirname, 'build', 'production');
module.exports.devtool = undefined;
