
const webpack = require('webpack');
const config = require('./webpack.config.js');

delete config.devtool;
delete config.output.pathinfo;

config.mode = 'production',
config.plugins[0] = new webpack.DefinePlugin({
  PRODUCTION: JSON.stringify(true),
  'process.env':{
	'NODE_ENV': JSON.stringify('production')
  }
});

module.exports = config;
