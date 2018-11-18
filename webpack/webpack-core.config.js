var path = require("path");
var webpack = require("webpack");
var CopyWebpackPlugin = require("copy-webpack-plugin");
var HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
  mode: "development",
  resolve: {
    alias: {
      "resources": path.resolve(__dirname, "../../../../src/main/resources"),
      "js": path.resolve(__dirname, "../../../../src/main/js"),
      "scalajs": path.resolve(__dirname, "./scalajs-entry.js")
    },
    modules: [path.resolve(__dirname, "node_modules")]
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"]
      },
      { // not necessarily unless to expose jQuery to JS in HTML page
        test: require.resolve("jquery"),
        use: [{
          loader: "expose-loader",
          options: "jQuery"
        }, {
          loader: "expose-loader",
          options: "$"
        }]
      },
      // "file" loader for svg
      {
        test: /\.svg$/,
        use: [
          {
            loader: "file-loader",
            query: {
              name: "static/media/[name].[hash:8].[ext]"
            }
          }
        ]
      }
    ]
  },
  plugins: [
    new CopyWebpackPlugin([
      {from: path.resolve(__dirname, "../../../../public")}
    ]),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, "../../../../public/index.html")
    }),
    new webpack.ProvidePlugin({
      $: "jquery",
      jQuery: "jquery",
      "window.jQuery": "jquery"
    })
  ]
}
