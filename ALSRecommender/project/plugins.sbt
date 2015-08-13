
resolvers += Resolver.url("artifactory", url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

addSbtPlugin("com.eed3si9n"				% 	"sbt-assembly" 		%	"0.13.0")

addSbtPlugin("com.typesafe.sbteclipse"	%	"sbteclipse-plugin" 	% 	"4.0.0")

addSbtPlugin("com.orrsella" 				% "sbt-sublime" 			% "1.1.0")

addSbtPlugin("com.orrsella" 				% "sbt-stats" 				% "1.0.5")


