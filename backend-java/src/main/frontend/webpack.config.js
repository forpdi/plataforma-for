var path = require('path');
var node_modules_dir = path.resolve(__dirname, 'node_modules');
var Webpack = require("webpack");

module.exports = {
	context: __dirname,
    entry: {
    	javascript: "./app.js",
    	html: "./index.html",
        icon: "./favicon.ico"
    },
    output: {
        path: path.resolve(__dirname, 'build/development'),
        filename: "app.js"
    },
    resolve: {
    	alias: {
    		"forpdi": __dirname,
            "jquery.ui.widget": "./vendor/jquery.ui.widget.js"
    	}
    },
    plugins: [
              new Webpack.ProvidePlugin({
                  $: "jquery",
                  jQuery: "jquery"
              })
    ],
    module: {
        loaders: [{
            test: /\.jsx$/,
            exclude: [node_modules_dir],
            loader: "babel-loader",
            query: {
                presets: ['es2015','react']
            }
        },{
            test: /\.scss$/,
            loader: "style-loader!css-loader!sass-loader"
        },{
        	test: /\.css$/,
        	loader: "style-loader!css-loader"
        },{
        	test: /\.html$|\.ico$/,
        	loader: "file?name=[name].[ext]"
        },{
        	test: /\.(woff|woff2)$/,
        	loader: "url-loader?limit=10000&mimetype=application/font-woff"
        },{
        	test: /\.ttf$|\.eot$|\.svg$|\.png$|\.jpe?g$|\.gif$/,
        	loader: "file-loader"
        }]
    }
};
