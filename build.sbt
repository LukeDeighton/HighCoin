name := """highcoin"""
organization := "com.highgain"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.5.3"

libraryDependencies += "org.bitcoinj" % "bitcoinj-core" % "0.12"
libraryDependencies += "commons-codec" % "commons-codec" % "1.11"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.highgain.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.highgain.binders._"
