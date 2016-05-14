import sbt._
import Keys._

object BuildSettings {

  // Basic settings for our app
  lazy val basicSettings = Seq[Setting[_]](
    organization          :=  "Hop Hey Lala Ley",
    version               :=  "0.0.1",
    description           :=  "ADChallenge",
    scalaVersion          :=  "2.11.7",
    scalacOptions         :=  Seq("-deprecation", "-encoding", "utf8",
                                  "-feature", "-target:jvm-1.8"),
    resolvers             ++= Dependencies.resolutionRepos
  )

  lazy val buildSettings = basicSettings
}