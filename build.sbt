lazy val router = (project in file("router"))
  .settings(
    name := "Scala.js router",
    scalaVersion := "2.13.3",
    Compile / scalacOptions ++= List("-Ymacro-annotations"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.0.0"
    )
  )
  .enablePlugins(ScalaJSPlugin)
