enablePlugins(PackPlugin)

name := "example-metrics"
version := "0.1"
scalaVersion := "2.13.3"
packMain := Map("hello" -> "io.feoktant.App")

val akkaHttpV = "10.1.11"
val akkaV = "2.6.5"
val slickV = "3.3.2"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core"    % "0.13.0",
  "io.circe" %% "circe-generic" % "0.13.0",

  "de.heikoseeberger" %% "akka-http-circe" % "1.32.0",

  "com.typesafe.akka" %% "akka-http"           % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-testkit"   % akkaHttpV % Test,
  "com.typesafe.akka" %% "akka-stream"         % akkaV,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,

  "com.typesafe.slick"  %% "slick"          % slickV,
  "com.typesafe.slick"  %% "slick-hikaricp" % slickV,
  "org.postgresql"       % "postgresql"     % "42.2.14",

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.scalatest"     %% "scalatest"             % "3.1.1"    % Test,
  "org.scalatestplus" %% "scalacheck-1-14"       % "3.1.1.1"  % Test,
  "org.scalatestplus" %% "scalatestplus-mockito" % "1.0.0-M2" % Test,
  "org.scalacheck"    %% "scalacheck"            % "1.14.3"   % Test,
  "org.mockito"        % "mockito-core"          % "3.3.3"    % Test,
  "org.mockito"       %% "mockito-scala"         % "1.14.0"   % Test,
)
