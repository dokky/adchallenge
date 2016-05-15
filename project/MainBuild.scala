import sbt._
import Keys._
import play.sbt._

object MainBuild extends Build {

  import Dependencies._
  import BuildSettings._

  // Configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  lazy val project = Project("ADChallenge", file("."))
    .settings(buildSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
	      Libraries.scalaz,
	      Libraries.playWebServices,
	      Libraries.cassandra,
        Libraries.scalaTestPlusForPlay
    )
   )
    .enablePlugins(PlayScala)
  
}
