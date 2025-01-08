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
    generateNewValue()
    grid_dispaly()
    while (GAME_STATE) {
      userInput()
      //generateNewValue()
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
      val inputs: Array[Char] = Array('w', 'a', 's', 'd', 'W', 'A', 'S', 'D')
      var state: Boolean = true
      var input: Char = '0'
      while(state) {
        input = StdIn.readChar()
        if (inputs.contains(input)) {
          state = false
        } else {
          println("Please, enter a valid choice.")
        }
      }
      gridMover(input)
  }

  // Generate a new value of 2 with 90% chance or 4 with 10% chance
  def newTileValue(): Int = {
    val value: Int = RANDOM.nextInt(10)
    if (value == 9) {
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

  def possibleMove(): Boolean = {
    println("possible move function")
    true
  }

  def gameOver(): Unit = {

  }

  /**
  def gridMover(direction: Char): Unit = {
    val temp: Array[Array[Int]] = Array.ofDim(4, 4)
    var counter: Int = 0
    for (i <- GRIDS.indices) {
      val in: Array[Int] = new Array[Int](4)
      for (j <- GRIDS(i).indices) {
        if (counter == j) {
          in(counter) = GRIDS(i)(j)
        }
      }
      temp(counter) = in
      counter += 1
    }
    for (i <- temp(0).indices) {
      for (j <- temp(i).indices) {
        print(temp(i)(j))
      }
      println()
    }
    println("gridMover function")

  }
  **/

  def gridMover(direction: Char): Unit = {

  }

  // Check if there are empty grids to generate a new value. If not, then check for possible moves and if there are none, game over.
  def generateNewValue(): Unit = {
      val temp: Array[Array[Int]] = checkForZero()
      val positions: Array[Int] = temp(RANDOM.nextInt(temp.length))
      if (temp.length == 0) {
        possibleMove()
      } else {
        //GRIDS(positions(0))(positions(1)) = newTileValue()
        GRIDS(0)(0) = newTileValue()
        GRIDS(0)(1) = newTileValue()
      }
  }
}

object Game extends App {
  val game: Game = new Game
  game.startGame()
}