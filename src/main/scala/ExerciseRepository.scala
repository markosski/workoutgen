
case class Exercise(name: String, intensity: String, equipment: String, level: Int)

class ExerciseRepository {
	def listAll(num: Int, offset: Int = 0): Seq[Exercise] = ???
}