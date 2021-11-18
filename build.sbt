name := "courses"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.2.0"

libraryDependencies += "joda-time" % "joda-time" % "2.10.13"

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.9" withSources() withJavadoc()

libraryDependencies += "org.typelevel" %% "kittens" % "2.3.2"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
libraryDependencies += "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.10" % Test
libraryDependencies += "org.scalatest" %% "scalatest-wordspec" % "3.2.10" % Test
libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.10" % Test
libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % "3.2.10" % Test
libraryDependencies += "org.scalatest" %% "scalatest-core" % "3.2.10"