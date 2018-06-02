package core

import scala.math.random

object Util {
    def getRandomFor(n: Int): Int = (random * 10).ceil.toInt % n
}
