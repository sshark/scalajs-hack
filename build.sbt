enablePlugins(ScalaJSBundlerPlugin)

name := "Scala.JS Hack"

scalaVersion := "2.12.6"

npmDependencies in Compile += "jquery" -> "3.3.1"
npmDependencies in Compile += "jquery-modal" -> "0.9.1"

npmDevDependencies in Compile += "file-loader" -> "1.1.11"
npmDevDependencies in Compile += "style-loader" -> "0.20.3"
npmDevDependencies in Compile += "css-loader" -> "0.28.11"
npmDevDependencies in Compile += "expose-loader" -> "0.7.5"
npmDevDependencies in Compile += "imports-loader" -> "0.8.0"
npmDevDependencies in Compile += "html-webpack-plugin" -> "3.2.0"
npmDevDependencies in Compile += "copy-webpack-plugin" -> "4.5.1"
npmDevDependencies in Compile += "webpack-merge" -> "4.1.2"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.5"
libraryDependencies += "com.lihaoyi" %%% "utest" % "0.6.3" % "test"
libraryDependencies += "org.querki" %%% "jquery-facade" % "1.2"

testFrameworks += new TestFramework("utest.runner.Framework")

scalaJSUseMainModuleInitializer := true

version in webpack := "4.5.0"
version in startWebpackDevServer := "3.1.3"

webpackResources := baseDirectory.value / "webpack" * "*"

webpackConfigFile in fastOptJS := Some(baseDirectory.value / "webpack" / "webpack-fastopt.config.js")
webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack" / "webpack-opt.config.js")
webpackConfigFile in Test := Some(baseDirectory.value / "webpack" / "webpack-core.config.js")

webpackDevServerExtraArgs in fastOptJS := Seq("--inline", "--hot")
webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly()

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv

addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS")
