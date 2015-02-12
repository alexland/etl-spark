
// start spark 

// on hadoop cluster:
// %> ./bin/spark-shell --master yarn-client

// on local box:
// %> ./bin/spark-shell --master local[*]


val data_dir = "/Users/doug/scala_projects/aa_spark/ch02-intro/linkage"

val rawblocks = sc.textFile(data_dir) 

val hx = rawblocks.take(10)


// remove headers from data (the RDD bound to "hx")

def isHeader(line: String): Boolean = {
	line.contains("id_1")
}

hx.filter(!isHeader(_))

val data = rawblocks.filter(!isHeader(_))


// verify 
data.first()


// separe line into elements by delimiter, group elements int 4-tuple

// to enter this fn in the spark-shell, at prompt:

// :paste
// insert multi-line code block (ctrl-V)
// then ctrl-D

def parse(line: String) = {
	
	val p = line.split(',')
	val id1 = p(0).toInt
	val id2 = p(1).toInt
	val s = p.slice(2, 11).map(toDouble)
	val m = p(11).toBoolean
	(id1, id2, s, m)
}


val t = parse(line)

// tuple methods:

t._1     				// get first item in tuple, t

t.productElement(0)    	// get first item in tuple, t

t.productArity			// get tuple size 



// create a human-readable record tuple, using scala case classes

case class MatchData(id1: Int, id2: Int,
	s: Array[Double], m: Boolean)

// udpate 'parse' fn so it returns instance of MatchData case class

def parse(line: String) = {
	val p = line.split(',')
	val id1 = p(0).toInt
	val id2 = p(1).toInt
	val s = p.slice(2, 11).map(toDouble)
	val m = p(11).toBoolean
	MatchData(id1, id2, s, m)
}


val md = parse(line)


// apply the parse fn to all elements in hx except header line:

val mds = hx.filter(x => !isHeader(x)).map(x => parse(x))


// apply parse to all data in the cluster:

val data1 = data.map(line => parse(line))

data1.cache()
