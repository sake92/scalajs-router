inThisBuild(
  List(
    organization := "ba.sake",
    scalaVersion := "2.13.3",
    publish / skip := true, // dont publish root project..
    resolvers += Resolver.sonatypeRepo("releases"),
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    developers += Developer("sake92", "Sakib Hadžiavdić", "sakib@sake.ba", url("https://sake.ba")),
    scmInfo := Some(
      ScmInfo(url("https://github.com/sake92/scalajs-router"), "scm:git:git@github.com:sake92/scalajs-router.git")
    ),
    homepage := Some(url("https://github.com/sake92/scalajs-router"))
  )
)

lazy val router = (project in file("router"))
  .settings(
    name := "scalajs-router",
    publish / skip := false,
    Compile / scalacOptions ++= List("-Ymacro-annotations"),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.0.0"
    )
  )
  .enablePlugins(ScalaJSPlugin)
