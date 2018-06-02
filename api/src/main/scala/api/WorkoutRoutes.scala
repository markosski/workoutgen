package api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsonFormat}
import core.{Exercise, ExerciseRepository, WorkoutTabata, WorkoutUseCase}
/**
  * Created by marcin1 on 4/10/17.
  */

case class ExerciseItemResp(name: String)
case class WorkoutTabataResp(timeMinutes: Int, workSeconds: Int, restSeconds: Int, sets: Int, exercises: Seq[ExerciseItemResp])

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val exerciseReads: JsonFormat[ExerciseItemResp] = jsonFormat1(ExerciseItemResp)
    implicit val workoutReads: JsonFormat[WorkoutTabataResp] = jsonFormat5(WorkoutTabataResp)
}

object WorkoutRoutes extends JsonSupport with CORSHandler {

    private val workoutUseCase = new WorkoutUseCase {
        val repository: ExerciseRepository = new ExerciseRepository
    }

    val tabata =
        path("tabata" / IntNumber / IntNumber / Segment) { (time: Int, exp: Int, equipment: String) =>
            corsHandler(
                get {
                    val equipList = equipment.split(",").toList
                    val workout = new WorkoutTabata(time)

                    workoutUseCase.getWorkout(equipList, exp, workout) match {
                        case Right(x) => {
                            //                        complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(Json.toJson(x(0)))))
                            val ex = x.map(e => ExerciseItemResp(e.name))
                            complete(Map("success" -> WorkoutTabataResp(time, 20, 10, workout.exerciseNumber._2, ex)))
                        }
                        case Left(m) =>
                            //                        complete(HttpEntity(ContentTypes.`application/json`, Json.stringify(Json.toJson(Map("error" -> m)))))
                            complete(Map("error" -> m))
                    }
                }
            )
        }

    val routes = pathPrefix("workout") { tabata }
}
