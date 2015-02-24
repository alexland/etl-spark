

val df = "/Users/doug/HOF.csv"

val rb = sc.textFile(df)

val hx = rb.take(10)


case class DataRow(player: String,
					position:String,
					numSeasons: Int, 
					hr: Int,
					rbi: Int, 
					ba: Float, 
					slugPct: Float,
					hof: Boolean
)

def intToBool(anyInt:Int) = {
	if (anyInt == 0) false else true
}

val headers = hx(0).split(',')

/** returns case class object (tuple whose field values are accessible 
		by field name)
	pass in one raw data row as a single string
*/
def parseLine(line: String) = {
	val a = line.split(',')
	val player = a(0).toLowerCase
	val position = a(16).toLowerCase
	val numSeasons = a(1).toInt
	val hr = a(8).toInt
	val rbi = a(9).toInt
	val ba = a(12).toFloat
	val slugPct = a(14).toFloat
	val hof = intToBool(a(17).toInt)
	DataRow(player, position, numSeasons, hr, rbi, ba, slugPct, hof)
}


val line_test = hx(5)
val x = parseLine(line_test)

# verify correct number of fields returned:
x.productArity
// returns: Int = 8


/** to remove header line from dataset
*/
def isHeader(line: String): Boolean = {
	line.startsWith("player").toLower
}

val data = rb.filter(!isHeader(_)).map(parseLine(_))

# map parseLine over the entire data set
val data = rb.map(parseLine(_))
// data: org.apache.spark.rdd.RDD[DataRow] = MappedRDD[7] at map at <console>:23


/** a DataRow instance is tuple-like (not a collection) but to access
	individual elements, this works:
*/

// select one DataRow instance (one tuple of values, or one row of data)
val r0 = hx(5)

r0.prouctIterator.foreach(println)


val battingAvg = data.map(x => x.ba)

/** to get summary statistics:
*/

val sm_stats = battingAvg.stats()


/** now re-query for 'battingAvg' to exclude positions w/ low batting avg
*/
def isPitcher(line: DataRow): Boolean = {
	line.position == "pitcher"
}

def isMidInfielder(line: DataRow): Boolean = {
	(line.position == "second base") || (line.position == "shortstop")
}

val battingAvg = data.filter(!isMidInfielder(_)).map(x => x.ba)

battingAvg.stats()
// org.apache.spark.util.StatCounter = (count: 1038, mean: 0.271156, stdev: 0.026365, max: 0.366000, min: 0.161000)






