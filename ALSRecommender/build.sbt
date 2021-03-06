
assemblyJarName in assembly :="als-recommender.jar"

// organization := "org.dougybarbo"

version := "0.1"

mainClass in assembly := Some("ALSRecommender")

crossScalaVersions := Seq("2.10.5", "2.11.7")

sourceDirectory := new File(baseDirectory.value, "src")

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

unmanagedBase := baseDirectory.value / "lib"

// assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

libraryDependencies ++= Seq(
	"org.apache.spark"		%%		"spark-core"			% 	"1.4.1"				%		"provided",
	"org.apache.spark"		%%		"spark-mllib"			%	"1.4.1"				%		"provided",
	"org.apache.hadoop"		%			"hadoop-client"		% 	"2.7.1"				%		"provided",
	"org.scalatest" 			%%		"scalatest" 				%	"3.0.0-SNAP5"	 	%		"test",
	"org.scalaz"				%%		"scalaz-core"			%	"7.2.0-M2",
	"org.scalanlp"				%%		"breeze"				%	"0.11.2",
	"com.quantifind"			%% 		"wisp"					%	"0.0.4"
)

resolvers ++= Seq(
	"Akka Repository"         			at "http://repo.akka.io/releases/",
	"Sonatype OSS Snapshots" 	 	at "http://oss.sonatype.org/content/repositories/snapshots/"
)

// resolvers += Resolver.file("my-test-repo", file("test")) transactional()

javacOptions ++= Seq(
	"-Xlint:unchecked",
	"-Xlint:deprecation",
	"-Xmx4096m",
	"-Xms512m",
	"-XX:MaxPerSize=512"
)

assemblyMergeStrategy in assembly := {
	case PathList("javax", "servlet", xs @ _*) 			=>		MergeStrategy.first
	case PathList(ps @ _*) if ps.last endsWith ".html"	=>		MergeStrategy.first
	case "application.conf"								=>		MergeStrategy.concat
	case "unwanted.txt" 									=>		MergeStrategy.discard
	case x 													=>
		val oldStrategy = (assemblyMergeStrategy in assembly).value
		oldStrategy(x)
}

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
	{
		case "application.conf" 								=>		MergeStrategy.concat
		case "reference.conf" 									=> 		MergeStrategy.concat
		case "META-INF/spring.tooling" 						=>		MergeStrategy.concat
		case "overview.html"									=> 		MergeStrategy.rename
		case PathList("javax", "servlet", xs @ _*) 			=>		MergeStrategy.last
		case PathList("org", "apache", xs @ _*) 				=> 		MergeStrategy.last
		case PathList("META-INF", xs @ _*) 					=> 		MergeStrategy.discard
		case PathList("com", "esotericsoftware", xs @ _*) 	=> 		MergeStrategy.last
		case "about.html" 										=> 		MergeStrategy.rename
		case x 													=>		old(x)
	}
}

excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
	cp filter { f =>
		(f.data.getName contains "commons-logging") ||
		(f.data.getName contains "sbt-link")
	}
}

