package api

import java.io.{ByteArrayOutputStream, InputStream, OutputStream}

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.polly.{AmazonPollyClient, AmazonPollyClientBuilder}
import com.amazonaws.services.polly.model._
import com.typesafe.scalalogging.Logger
import core.WorkoutUseCase

import scala.collection.mutable

class Speak {
  private val logger = Logger(classOf[WorkoutUseCase])
  private val cache = mutable.Map[Int, ByteArrayOutputStream]()

  private val client = AmazonPollyClientBuilder.standard()
    .withRegion("us-east-1")
    .withCredentials(new ProfileCredentialsProvider())
    .build()

  /**
    * Synthesize text to mp3 bytes.
    * Store synthesized data in a Map and attempt to lookup same message instead of calling external API.
    * @param msg
    * @return
    */
  def talk(msg: String): ByteArrayOutputStream = {
    if (cache.contains(msg.hashCode)) {
      logger.info(s"Reading previously synthesized text: $msg")

      cache.getOrElse(msg.hashCode, new ByteArrayOutputStream())
    } else {
      logger.info(s"Synthesizing text: $msg")

      val request = new SynthesizeSpeechRequest()
        .withOutputFormat(OutputFormat.Mp3)
        .withVoiceId("Joanna")

      val resp: SynthesizeSpeechResult = client.synthesizeSpeech(request.withText(msg))
      val is: InputStream = resp.getAudioStream

      val bos = new ByteArrayOutputStream

      val bytes = new Array[Byte](1024)
      var len = 0

      while ( {len = is.read(bytes); len } > 0)
        bos.write(bytes, 0, len)
      is.close()

      // Add output stream to the cache
      cache += (msg.hashCode -> bos)
      bos
    }
  }
}
