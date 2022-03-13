
const Webpack = require('webpack');
const path = require('path');

module.exports = {
  context: __dirname,
  mode: 'development',

  devtool: '#eval-cheap-module-source-map',
  entry: ["./favicon.ico", "./index.html", "./app.js"],
  output: {
    path: path.join(__dirname, 'dist'),
    filename: 'bundle.js',
    publicPath: '/',
    pathinfo: true,
  },

  devServer: {
    historyApiFallback: {
      disableDotRule: true,
    },
    compress: true,
    proxy: {
      '/plataforma-for/**': {
        target: 'http://localhost:8080',
        secure: false,
      },
    },
  },

  resolve: {
    extensions: ['.js', '.jsx', '.json', '.scss', '.css'],
    alias: {
      '@': path.resolve(__dirname, 'jsx'),
	  "forpdi": __dirname,
	  "jquery.ui.widget": "./vendor/jquery.ui.widget.js",
	  "jquery-ui/ui/widget": "./vendor/jquery.ui.widget.js"
	},

  },

  plugins: [
    new Webpack.DefinePlugin({
      PRODUCTION: JSON.stringify(false),
      'process.env': {
		'NODE_ENV': JSON.stringify('development')
	  }
    }),
	new Webpack.ProvidePlugin({
		$: "jquery",
		jQuery: "jquery"
	}),
  ],

  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        loaders: ['babel-loader'],
      }, {
        test: /theme-[a-z]+\.scss$/,
        loaders: [
          'file-loader?name=[name].css',
          'postcss-loader',
          'sass-loader',
        ],
      }, {
        test: /_[a-z\-]+\.scss$/,
        loaders: [
          'style-loader',
          'css-loader',
          'postcss-loader',
          'sass-loader',
        ],
      }, {
        test: /\.pdf$/,
        loaders: ['file-loader?name=documents/[name].[ext]'],
      }, {
        test: /\.css$/,
        loaders: ['style-loader', 'css-loader', 'postcss-loader'],
      }, {
        test: /\.(html|ico)$/,
        loader: 'file-loader?name=[name].[ext]',
      }, {
        test: /\.(jpg|jpeg|png|svg|gif)(\?v=[0-9].[0-9].[0-9])?$/,
        loader: 'file-loader?name=images/[name].[ext]',
      }, {
        test: /\.(woff|woff2|ttf|eot)(\?v=[0-9].[0-9].[0-9])?$/,
        loader: 'file-loader?name=fonts/[name].[ext]',
      },
    ],
  },

};
