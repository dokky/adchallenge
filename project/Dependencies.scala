import sbt._

object Dependencies {

  val resolutionRepos = Seq(
        Resolver.mavenLocal,
        DefaultMavenRepository,
        Resolver.typesafeRepo("releases"),
        Resolver.sonatypeRepo("releases"),
        "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
  )

  object V {
    val play                 = "2.5.3"
    val scalaTestPlusForPlay = "1.5.1"
    val cassandra            = "3.5"
    val scalaz               = "7.1.4"
  }

  object Libraries {
    val playWebServices      =  "com.typesafe.play"         %% "play-ws"                   % V.play
    val cassandra            =  "org.apache.cassandra"      %  "cassandra-all"             % V.cassandra
    val scalaz               =  "org.scalaz"                %% "scalaz-core"               % V.scalaz
    val scalaTestPlusForPlay =	"org.scalatestplus.play"    %% "scalatestplus-play"        % V.scalaTestPlusForPlay      % Test
  }
}