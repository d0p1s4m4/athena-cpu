scalaVersion := "2.13.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-Xcheckinit",
  "-P:chiselplugin:genBundleElements",
  "-Xfatal-warnings",
  "-language:reflectiveCalls",
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

// Chisel 3.5
libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.5.1"
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5.1"

addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.5.1" cross CrossVersion.full)