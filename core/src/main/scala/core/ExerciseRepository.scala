package core

import scala.io.Source

case class Exercise(id: Int, parent: Int, name: String, bodyGroup: String, intensity: String, movement: String, equipment: String, level: Int)

class ExerciseRepository {
	val filePath = "core/src/main/resources/exercises.csv"

	def listAll: Seq[Exercise] = {
		Source.fromFile(filePath)
			.getLines
			.map(x => {
				val row = x.split(",").toList
				Exercise(row(0).toInt, row(1).toInt, row(2), row(6), row(3), row(7), row(5), row(4).toInt)
			}).toList
	}
}