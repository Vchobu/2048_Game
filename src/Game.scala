import scala.util.Random
import scala.io.StdIn

class Game {
  private var GRIDS: Array[Array[Int]] = Array.ofDim[Int](4, 4)
  private val RANDOM: Random = new Random()
  private var SCORE: Int = 0
  private var GAME_STATE: Boolean = false

  def startGame(): Unit = {
    GAME_STATE = true
    GRIDS = Array.ofDim[Int](4, 4)
    SCORE = 0
    generateNewValue()
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

  // Check if user's input is valid, if not then call the same function again until it's valid
  def userInput (): Unit = {
    println("Please, choose a direction to move tiles. ('w' - up, 'a' - left, 's' - down, 'd' - right)")
    val input: Char = StdIn.readChar()
    if (input != 'w' && input != 'a' && input != 's' && input != 'd' && input != 'W' && input != 'A' && input != 'S' && input != 'D') {
      userInput()
    }
  }

  // Generate a new value of 2 with 90% chance or 4 with 10% chance
  def newTileValue(): Int = {
    val value: Int = (RANDOM.nextDouble() * 10).toInt
    if (value >= 9) {
       4
    } else {
       2
    }
  }

  // Check if there are any empty positions (zeroes) inside our GRIDS
  def checkForZero(): Array[Array[Int]] = {
    var counter: Int = 0
    for (i <- GRIDS(0).indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) == 0) {
          counter += 1
        }
      }
    }
    val result: Array[Array[Int]] = new Array[Array[Int]](counter)
    var position: Int = 0
    for (i <- GRIDS(0).indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) == 0) {
          result(position) = Array[Int](i, j)
          position += 1
        }
      }
    }
    result
  }


  def generateNewValue(): Unit = {

  }
}

object Game extends App {
  val game: Game = new Game
  game.startGame()
}