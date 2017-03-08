var path = require('path');
var node_modules_dir = path.resolve(__dirname, 'node_modules');
var Webpack = require("webpack");

module.exports = {
	context: __dirname,
    devtool: "cheap-module-source-map",
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
        extensions: ["", ".webpack.js", ".web.js", ".js", ".jsx"],
    	alias: {
    		"forpdi": __dirname,
            "jquery.ui.widget": "./vendor/jquery.ui.widget.js",
            "jquery-ui/ui/widget": "./vendor/jquery.ui.widget.js"
    	}
    },
    plugins: [
              new Webpack.ProvidePlugin({
                  $: "jquery",
                  jQuery: "jquery"
              }),
              new Webpack.DefinePlugin({
                  'process.env':{
                      'NODE_ENV': JSON.stringify(process.env.NODE_ENV)
                  }
              })
    ],
    module: {
        noParse: /node_modules\/quill\/dist\/quill.js/,        
        loaders: [{
            test: /\.jsx$/,
            exclude: [node_modules_dir],
            loader: "babel-loader",
            query: {
                presets: ['es2015','react']
            }
        },{
            test: /\.json$/,
            loader: "json-loader"
        },{
            test: /theme-[a-z]+\.scss$/,
            loader: "file?name=[name].css!sass-loader"
        },{
            test: /_[a-z\-]+\.scss$/,
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
        	loader: "file-loader?name=images/[name].[ext]"
        }]
    }
};
