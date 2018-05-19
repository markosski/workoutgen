import scala.collection.mutable.HashMap
import scala.math.random

class MarkovChains {
	val data = HashMap[String, HashMap[String, (Int, Double)]]()
	val counters = HashMap[String, Int]()

	def add(a: String, b: String): Unit = {
		data.get(a) match {
			case Some(elemA) => {
				elemA.get(b) match {
					case Some(elemB) => elemA.put(b, (elemB._1 + 1, 0.0))
					case None		 => elemA.put(b, (1, 0.0))
				}
			}
			case None => data.put(a, HashMap(b -> (1, 0.0)))
		}
		counters.put(a, counters.getOrElse(a, 0) + 1)
		update(a)
	}

	/**
	 * Get item using probability
	 */
	def get(a: String): Option[String] = {
		data.get(a) match {
			case Some(elemA) => {
				val els = elemA.toList.sortBy(_._2._2)
				val rand = random
				
				var result = ""
				var prev = 0.0
				for (el <- els) {
					if (rand >= prev && rand <= prev + el._2._2) result = el._1 
					prev = prev + el._2._2
				}

				Some(result)
			}
			case None => None
		}
	}

	/**
	 * Get item with highest probability value
	 */
	def getTop(a: String, count: Int): List[String] = {
		data.get(a) match {
			case Some(elemA) => {
				elemA.toList.sortBy(_._2._2).reverse.take(count).map(x => x._1)
			}
			case None => List[String]()
		}
	}

	def update(a: String): Unit = {
		val elemA = data.getOrElse(a, throw new Exception("Element does not exist"))

		elemA.keys.foreach { k =>
			elemA.get(k) match {
				case Some(x) 	=> elemA.put(k, (x._1, x._1 / counters.get(a).get.toDouble))
				case None 		=> throw new Exception("Element does not exist")
			}
		}
	}
}