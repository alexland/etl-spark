

this app packaged as a jar, like so:

%> sbt package


then executed over the spark cluster like so:

%> $SPARK_HOME/bin/spark-submit \
		--driver-memory 6g \
		--class "SparkGrep" \
		--master "local[*]" \
		./target/scala-2.10/spark-grep_2.10-1.0.jar