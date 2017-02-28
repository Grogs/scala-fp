scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")

name := "scala-fp"

version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.9.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % Test
)

