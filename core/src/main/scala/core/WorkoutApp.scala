package core

object WorkoutApp extends App {
    val time =  args(0).toInt
    val equipment = args(1).split(",").toList

    val workout = new WorkoutTabata(time)
    val useCase = new WorkoutUseCase {
        val repository: ExerciseRepository = new ExerciseRepository
    }
    val exercises: List[Exercise] = useCase.getWorkout(equipment, 2, workout) match {
        case Left(m) => throw new Exception(m)
        case Right(v) => v.toList
    }

    println("- HIIT workout -")
    println(s"${workout.exerciseNumber._1} x ${workout.exerciseNumber._2} for ${time} minutes with ${workout.work}s work and ${workout.rest}s rest.")
    exercises.foreach(x => println(s"- ${x.name}"))
}
