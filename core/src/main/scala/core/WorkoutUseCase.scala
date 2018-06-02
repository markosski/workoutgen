package core

import com.typesafe.scalalogging.Logger

import scala.annotation.tailrec

trait WorkoutUseCase {
    val logger = Logger(classOf[WorkoutUseCase])

	val repository: ExerciseRepository
    val m = new MarkovChains
    m.load("core/src/main/resources/markov.txt")

	def getFiltered(equipment: List[String], level: Int = 0): Seq[Exercise] = {
		repository.listAll.filter(_.level <= level).filter(x => equipment.contains(x.equipment))
	}

    def getWorkout(equip: List[String], complexity: Int, workout: Workout): Either[String, Seq[Exercise]] = {

        @tailrec
        def tryGetWorkout(tryNum: Int): Seq[Exercise] = {
            val maxSize = workout.exerciseNumber._1 // number of exercises in a set
            var current = m.keys(Util.getRandomFor(m.keys.size))

            val routine: List[(String, String, String)] = (for (i <- 0 until maxSize) yield {
                val next = m.get(current) match {
                    case Some(x) => (x.split("-")(0), x.split("-")(1), x.split("-")(2))
                    case None => throw new Exception(s"...herp derp $current")
                }
                current = next.productIterator.mkString("-")
                next
            }).toList

            logger.info(s"Generated routine - ${routine.toString}")

            var exs: Seq[Exercise] = getFiltered(equip, complexity)

            val all = (for (r <- routine) yield {
                val filtered = exs.filter(x => x.bodyGroup == r._1 && x.intensity == r._2 && x.movement == r._3)
                if (filtered.nonEmpty) {
                    val rand = Util.getRandomFor(filtered.size)
                    val selected = filtered(rand)
                    exs = exs.filterNot(_.parent == selected.parent) // remove from master list
                    Some(selected)
                } else {
                    logger.warn(s"Could not find exercise of type $r")
                    None
                }
            }).flatten

            // Try to get proper number of exercies but give up after 5 times.
            if (tryNum == 5) all
            else if (all.length != maxSize) {
                logger.info(s"Retrying getWorkout: ${tryNum}")
                tryGetWorkout(tryNum + 1)
            } else all
        }

        val all = tryGetWorkout(1)

        if (all.length != workout.exerciseNumber._1)
            Left("Can't generate proper workout.")
        else
            Right(all)
    }
}