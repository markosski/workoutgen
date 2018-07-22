package api

import java.io.InputStream

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.polly.{AmazonPollyClient, AmazonPollyClientBuilder}
import com.amazonaws.services.polly.model._
import java.io.ByteArrayOutputStream

import com.typesafe.scalalogging.Logger
import core.WorkoutUseCase

class Speak {
  val logger = Logger(classOf[WorkoutUseCase])

  private val client = AmazonPollyClientBuilder.standard()
    .withRegion("us-east-1")
    .withCredentials(new ProfileCredentialsProvider())
    .build()

  private val reqest = new SynthesizeSpeechRequest()
    .withOutputFormat(OutputFormat.Mp3)
    .withVoiceId("Joanna")


  def talk(msg: String): ByteArrayOutputStream = {
    logger.info(s"Synthesizing text $msg")

    val resp: SynthesizeSpeechResult = client.synthesizeSpeech(reqest.withText(msg))
    val is: InputStream = resp.getAudioStream

    val bos = new ByteArrayOutputStream

    val bytes = new Array[Byte](1024)
    var len = 0

    while ( {len = is.read(bytes); len } > 0)
      bos.write(bytes, 0, len)
    is.close()

    bos
  }
}
