lazy val publishSettings = Seq(
  publishMavenStyle := true,
  bintrayOrganization := Some("scalacenter"),
  bintrayRepository := "tools",
  bintrayPackageLabels := Seq("scala", "scalacenter", "plugin", "sbt"),
  publishTo := (publishTo in bintray).value,
  publishArtifact in Test := false,
  licenses := Seq(
    // Scala Center license... BSD 3-clause
    "BSD" -> url("http://opensource.org/licenses/BSD-3-Clause")
  ),
  homepage := Some(url("https://github.com/scalacenter/sbt-release-early")),
  autoAPIMappings := true,
  pomExtra :=
    <developers>
      <developer>
        <id>jvican</id>
        <name>Jorge Vicente Cantero</name>
        <url></url>
      </developer>
    </developers>
)
lazy val buildSettings = Seq(
  organization := "ch.epfl.scala",
  resolvers += Resolver.jcenterRepo,
  resolvers += Resolver.bintrayRepo("scalaplatform", "tools"),
  updateOptions := updateOptions.value.withCachedResolution(true)
)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Xlint"
)

lazy val commonSettings = Seq(
  triggeredMessage in ThisBuild := Watched.clearWhenTriggered,
  watchSources += baseDirectory.value / "resources",
  scalacOptions in (Compile, console) := compilerOptions,
  testOptions in Test += Tests.Argument("-oD")
)

lazy val noPublish = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {}
)

lazy val allSettings: Seq[Setting[_]] =
  commonSettings ++ buildSettings ++ publishSettings

lazy val `sbt-release-early` = project
  .in(file("."))
  .settings(allSettings)
  .settings(ScriptedPlugin.scriptedSettings)
  .settings(
    sbtPlugin := true,
    scalaVersion := "2.10.6",
    addSbtPlugin("ch.epfl.scala" % "sbt-release" % "1.0.7"),
    addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0"),
    addSbtPlugin("me.vican.jorge" % "sbt-drone" % "0.1.1"),
    addSbtPlugin("com.dwijnand" % "sbt-dynver" % "1.2.0"),
    scriptedLaunchOpts := Seq(
      "-Dplugin.version=" + version.value,
      "-Xmx1g",
      "-Xss16m"
    ),
    scriptedBufferLog := false,
    fork in Test := true
  )