
// start spark 

// on hadoop cluster:
// %> ./bin/spark-shell --master yarn-client

// on local box:
// %> ./bin/spark-shell --master local[*]


val data_dir = "/Users/doug/scala_projects/aa_spark/ch02-intro/linkage"
val df = "/Users/doug/HOF.csv"

val rawblocks = sc.textFile(data_dir) 

val hx = rawblocks.take(10)


// remove headers from data (the RDD bound to "hx")

def isHeader(line: String): Boolean = {
	line.startsWith("Player")
}

hx.filter(!isHeader(_))

val data = rawblocks.filter(!isHeader(_))


// verify 
data.first()



// write function to handle non-numerical values, eg, "?"

def toDouble_(s: String) = {
	if ("?".equals(s)) Double.NaN else s.toDouble
}

// create a human-readable record tuple, using scala case classes

case class DataRow(id1: Int, id2: Int,
	s: Array[Double], m: Boolean)

// udpate 'parse' fn so it returns instance of MatchData case class


/** pass in single raw data row as string;
*   returns row as instance of DataRow class (tuple-like);
*	(i) splits raw string on a comma delimiter; extracts items 
*	at various offsets, coerces the type, and assigns them 
*	to named fields
*/
def parseLine(line: String) = {
	val p = line.split(',')
	val id1 = p(0).toInt
	val id2 = p(1).toInt
	val s = p.slice(2, 11).map(toDouble_)
	val m = p(11).toBoolean
	DataRow(id1, id2, s, m)
}


val md = parseLine(line)

// test: apply the parse fn to all elements in hx except header line:
val mds = hx.filter(x => !isHeader(x)).map(x => parseLine(x))


// apply parse to all data in the cluster:
val data1 = data.map(line => parseLine(line))
val data1 = data.map(parseLine(_))

data1.cache()







