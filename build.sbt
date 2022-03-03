name := "courses"

version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.2.0"

libraryDependencies += "joda-time" % "joda-time" % "2.10.13"

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.9" withSources() withJavadoc()

libraryDependencies += "org.typelevel" %% "kittens" % "2.3.2"

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.9"
libraryDependencies += "co.fs2" %% "fs2-io" % "3.2.2"
libraryDependencies += "org.gnieh" %% "fs2-data-csv" % "1.3.0"
libraryDependencies += "org.gnieh" %% "fs2-data-csv-generic" % "1.3.0"

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)

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



val zioConfigVersion = "1.0.0-RC29"

val configDependencies = List(
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-refined" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "org.apache.httpcomponents" % "httpasyncclient" % "4.1.3"
).map(_ excludeAll
  ExclusionRule("zio")
  )

val zioVersion = "1.0.3"
libraryDependencies += "dev.zio" %% "zio-streams" % zioVersion
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "2.2.0.1"
libraryDependencies += "org.http4s" %% "http4s-circe" % "1.0.0-M8"

// https://mvnrepository.com/artifact/dev.zio/zio-logging
libraryDependencies += "dev.zio" %% "zio-logging" % "0.5.3"
libraryDependencies += "dev.zio" %% "zio-logging-slf4j" % "0.5.3"
libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.30",
  "org.slf4j" % "slf4j-simple" % "1.7.30")
libraryDependencies += "org.apache.httpcomponents" % "httpasyncclient" % "4.1.3"

val http4sVersion = "0.21.12"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

//libraryDependencies += "dev.zio" %% "zio-redis" % "0.0.0+106-b40713b3+20201122-0748-SNAPSHOT"
libraryDependencies ++= Seq(
  "ch.qos.logback"       % "logback-classic"          % "1.2.3",
  "net.logstash.logback" % "logstash-logback-encoder" % "6.4"
)

lazy val circe = {
  val circeVersion = "0.13.0"
  Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-yaml"
  ).map(_ % circeVersion)
}

lazy val sttp = {
  Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-zio",
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server",
  ).map( _ % "0.16.15" ) ++
    Seq(
      "com.softwaremill.sttp.client" %% "core" % "2.2.5",
      "com.softwaremill.sttp.model" %% "core" % "1.1.4"
    )
}

libraryDependencies ++= circe ++ sttp

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test"          % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt"      % zioVersion % "test",
  "dev.zio" %% "zio-test-magnolia" % zioVersion % "test" // optional
)
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
lazy val http = {
  val akkaHttpVersion = "10.2.0"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
    "de.heikoseeberger" %% "akka-http-circe" % "1.28.0",
    "ch.megard" %% "akka-http-cors" % "0.4.3",
  )
}
libraryDependencies ++= configDependencies ++ http ++ circe
libraryDependencies += "dev.zio" %% "zio" % zioVersion

libraryDependencies += "eu.timepit" %% "refined" % "0.9.28"
libraryDependencies += "com.typesafe.play" %% "play" % "2.8.13"
libraryDependencies += "be.venneborg" % "play28-refined_2.13" % "0.6.0"

