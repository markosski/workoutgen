package core

trait Workout {
    val time: Int

    /**
      * (num of exercise x sets)
      * @return
      */
    def exerciseNumber: (Int, Int)
}

/**
  * Work in progress!!!
  *
  * AMRAP is a HIIT training where exercises have to be done for time without any rest.
  * @param time
  */
class WorkoutAMRAP(val time: Int) extends Workout {
    lazy val exerciseNumber: (Int, Int) = {
        val config = List(3, 4, 5, 6)
        (Util.getRandomFor(config.size), 0)
    }
}

/**
  * Tabata workout will use fixed 20/10 second work/rest times.
  * @param time
  */
class WorkoutTabata(val time: Int) extends Workout {
    val work = 20
    val rest = 10

    // Exercise configuration based on time of workout
    val config = Map(
        5 -> List((2, 5), (5, 2)),
        10 -> List((4, 5), (5, 4)),
        15 -> List((6, 4), (4, 6)),
        20 -> List((5, 6), (6, 5)),
        25 -> List((5, 5)),
        30 -> List((5, 6), (6, 5), (3, 10))
    )

    assert(config.keys.toList.contains(time), s"Valid workout times are: ${config.keys.mkString(", ")}")

    lazy val exerciseNumber: (Int, Int) = {
        config.get(time) match {
            case Some(x) => x(Util.getRandomFor(x.size))
            case None => (0, 0) // Since we are checking the input this should never happen.
        }
    }
}