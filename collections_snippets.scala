

//---------------------------  groupby -------------------------- //

// mock some data
val n = 500
val q = List.fill(n)(Random.nextInt)

def mod(x:Int) = {
	abs(x % 9)
}

val q1 = q.map(mod)
// returns:
// List[Int] = List(6, 0, 8, 1, 4, 4, 6, 1,....
	
	
val q2 = q1.groupby(identity).mapValues(_.size)
// returns:
// scala.collection.immutable.Map[Int,Int] = Map(0 -> 66, 5 -> 39, 1 -> 55, 6 -> 56, 2 -> 54, 7 -> 52, 3 -> 56, 8 -> 62, 4 -> 60)





def toDouble(s: String) = {
	if ("?".equals(s)) Double NaN else s.toDouble
}


def parseLine(line: String) = {
	val a = line.split(',')
	a.slice(2, 11).map(toDouble)
}






def f (arr: List[Int]) = for {
	a <- arr
	s = if (a < 0) -1 else 1} 
	yield sign * a