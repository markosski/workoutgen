package api

import java.io.ByteArrayOutputStream

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{pathPrefix}

object SpeakRoutes extends JsonSupport with CORSHandler {
  val speakApi = new Speak

  val speak =
    get {
      path(Segment) { _ =>
        parameters('q) { query =>
          val resp = speakApi.talk(query)
          complete(HttpEntity(ContentTypes.`application/octet-stream`, resp.toByteArray))
        }
      }
    }

  val routes = pathPrefix("speak") { speak }
}
