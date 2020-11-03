lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.4",
    "org.json4s" %% "json4s-jackson" % "3.5.3",
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  )
).enablePlugins(PlayScala).dependsOn(sharedJVM)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.1.0",
    "com.lihaoyi" %%% "upickle" % "1.2.2"
  )
).enablePlugins(ScalaJSWeb).dependsOn(sharedJS)

lazy val shared = (crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure) in file("shared"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.bitcoinj" % "bitcoinj-core" % "0.12",
      "commons-codec" % "commons-codec" % "1.11"
    )
  )

lazy val sharedJS     = shared.js
lazy val sharedJVM    = shared.jvm

lazy val commonSettings = Seq(
  scalaVersion := "2.12.12",
  organization := "com.github.lukedeighton"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
