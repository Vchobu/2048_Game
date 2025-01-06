import scala.util.Random
import scala.io.StdIn

class Game {
  private val GRIDS: Array[Array[Int]] = Array.ofDim[Int](4, 4)
  private val RANDOM: Random = new Random()
  private var SCORE: Int = 0
  private var GAME_STATE: Boolean = false

  def startGame(): Unit = {
    GAME_STATE = true
    while (GAME_STATE) {
      userInput()
      generateNewValue()
      grid_dispaly()
    }
  }

  // TODO: DELETE THIS FUNCTION LATER
  def grid_dispaly(): Unit = {
    var s: String = ""
    for (i <- GRIDS) {
      s += i.mkString("|", " ", "|\n")
    }
    println(s)
  }
  //

  def userInput (): Unit = {
    val input: Char = StdIn.readChar()
  }

  def newTileValue(): Int = {
    val value: Int = (RANDOM.nextDouble() * 10).toInt
    if (value >= 9) {
       4
    } else {
       2
    }
  }

  def checkForZero(): Array[Array[Int]] = {
    var counter: Int = 0
    for (i <- GRIDS.indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) == 0) {
          counter += 1
        }
      }
    }
    val result: Array[Array[Int]] = new Array[Array[Int]](counter)
    var positionChecker: Int = 0
    for (i <- GRIDS.indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) == 0) {

        }
      }
    }
    result
  }


  def generateNewValue(): Unit = {
    var state: Boolean = true
    while(state) {
      val x: Int = RANDOM.nextInt(GRIDS.length)
      val y: Int = RANDOM.nextInt(GRIDS(0).length)
      if (checkForZero().length == 0) {
        state = false
        println(s"Game Over!\nYour score: $SCORE")
        System.exit(-1)
      }
      else if (GRIDS(x)(y) == 0) {
        GRIDS(x)(y) = newTileValue()
        state = false
      }
    }

  }
}

object Game extends App {
  val game: Game = new Game
  game.startGame()
}