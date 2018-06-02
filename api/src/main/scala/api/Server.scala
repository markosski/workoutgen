package api

import akka.actor.ActorSystem
import akka.http.scaladsl.server._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, _}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import play.api.libs.json.Json

import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by marcin1 on 4/2/17.
  */

object Server extends App {
    import Directives._

    implicit val system = ActorSystem("LookerBackend")
    implicit val materializer = ActorMaterializer()  // TODO: what is that?

    implicit def myRejectionHandler =
        RejectionHandler.default.mapRejectionResponse {
            case res @ HttpResponse(_, _, ent: HttpEntity.Strict, _) =>
                // since all Akka default rejection responses are Strict this will handle all rejections
                val message = ent.data.utf8String.replaceAll("\"", """\"""")

                // we copy the response in order to keep all headers and status code, wrapping the message as hand rolled JSON
                // you could the entity using your favourite marshalling library (e.g. spray json or anything else)
                res.copy(entity = HttpEntity(ContentTypes.`application/json`, Json.stringify(Json.toJson(Map("error" -> message)))))

            case x => x
        }

    def index = {
        path("") {
            get {
                complete(HttpEntity(ContentTypes.`application/json`, """{"success": "hiitsmash back-end API"}""""))
            }
        }
    }

    val host = "0.0.0.0"
    val port = 8090

    val bindingFuture = Http().bindAndHandle(
        index ~ WorkoutRoutes.routes,
        host,
        port)


    println(s"Server online at http://$host:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done
}
