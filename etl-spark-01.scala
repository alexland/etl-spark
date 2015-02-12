
// start spark 

// on hadoop cluster:
// %> ./bin/spark-shell --master yarn-client

// on local box:
// %> ./bin/spark-shell --master local[*]


// one line of the data:
String = 37951,70084,1,?,0.125,?,1,0,0,1,0,FALSE


val data_dir = "/Users/doug/scala_projects/aa_spark/ch02-intro/linkage"

val rawblocks = sc.textFile(data_dir) 

// get # lines in rawblocks
rawblocks.count()

// 6,552,407

// hx is just a tiny subset of the complete dataset 'head'
// an action returns data, so this returns Array[String]
val hx = rawblocks.take(100)

// get 5000 lines of data without replacement)val 
val hx = rawblocks.takeSample(false, 5000)

// note: take * takeSample are 'actions';
// 'sample' is a 'transformation'

// get one row of data from hx
val line = hx(4)

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
// one argument for each element in the tuple

case class MatchData(id1: Int, id2: Int,
	s: Array[Double], m: Boolean)

// udpate 'parse' fn so it returns instance of MatchData case class
// instead of a tuple

def parse(line: String) = {
	val p = line.split(',')
	val id1 = p(0).toInt
	val id2 = p(1).toInt
	val s = p.slice(2, 11).map(toDouble)
	val m = p(11).toBoolean
	MatchData(id1, id2, s, m)
}

// random grab one line from the mini-dataset 'hx'
val line = hx(100)
val md = parse(line)

// apply the parse fn to all elements in hx except header line:

val mds = hx.filter(x => !isHeader(x)).map(x => parse(x))

// apply parse to all data in the cluster:

val data1 = data.map(line => parse(line))

data1.cache()
// store the RDD the next time it's calculated
// 'cache()' is syntactic sugar for rdd.persist(StorageLevel.MEMORY)
// which stores the RDD as unserialized java objects
// t/4 no serialization overhead
// alternatie mechanism in spark for persisting RDDs 'caching':
// rdd.persist(StorageLevel.MEMORY_SER), allocates large byte buffers
// in RAM & serializes the RDD contents into them 
// disk-based storage levels: MEMORY_AND_DISK, MEMORY_AND_DISK_SER
// http://spark.apache.org/docs/latest/programming-guide.html#rdd-operations


//---------------- aggregations -----------------//

val g1 = mds.groupBy(md => md.m)
// group the 10 lines of data based on the value in the 'm' field (T/F)


val counts = g1.mapValues(x => x.size)
// returns:
// scala.collection.immutable.Map[Boolean,Int] = Map(false -> 2906, true -> 2093)

counts.foreach(println)
// get counts by calling mapValues on g1, which operates only on the values
// returns: 
// (false,2906)
// (true,2093)

