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
    //generateNewValue()
    //generateNewValue()
    GRIDS(0)(3) = 16
    GRIDS(1)(3) = 2
    GRIDS(2)(3) = 4
    GRIDS(3)(3) = 8


    grid_dispaly(GRIDS)
    while (GAME_STATE) {
      userInput()
      //generateNewValue()
      grid_dispaly(GRIDS)
    }
  }

  def grid_dispaly(arr: Array[Array[Int]]): Unit = {
    var s: String = ""
    for (i <- arr) {
      s += i.mkString("|", " ", "|\n")
    }
    println(s)
  }

  // Check if user's input is valid, if not then call the same function again until it's valid
  def userInput(): Unit = {
    println("Please, choose a direction to move tiles. ('w' - up, 'a' - left, 's' - down, 'd' - right)")
    val inputs: Array[Char] = Array('w', 'a', 's', 'd', 'W', 'A', 'S', 'D')
    var state: Boolean = true
    var input: Char = '0'
    while (state) {
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

  def counterNonZero(arr: Array[Array[Int]]): Int = {
    var counter: Int = 0
    for (i <- arr(0).indices) {
      for (j <- arr(i).indices) {
        if (arr(i)(j) != 0) {
          counter += 1
        }
      }
    }
    counter
  }
  /**
  def gridMover(direction: Char): Unit = {
    val temp: Array[Array[Int]] = rotateArray(GRIDS)
    var bool: Boolean = true
    var counter: Int = 0
    grid_dispaly(temp)
    for (i <- temp.indices) {
      var chg = 0

      bool = true
      for (j <- temp(i).indices.reverse if bool) {
        counter = counterNonZero(temp(i))
        if (counter == 1 && bool) {
          for (y <- j until -1 by -1) {
            if (temp(i)(y) > 1) {
              chg = temp(i)(y)
              temp(i)(y) = 0
            }
          }
          temp(i)(0) = chg
        }
        bool = false
      }
    }
    grid_dispaly(temp)
  }
  **/
  def gridMover(direction: Char): Unit = {
    val counter: Int = counterNonZero(GRIDS)
    val positions: Array[Array[Int]] = new Array[Array[Int]](counter)
    var position: Int = 0
    for (i <- GRIDS(0).indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) != 0) {
          positions(position) = Array[Int](i, j)
          position += 1
        }
      }
    }
    if (direction == 'w' || direction == 'W') {
      for (i <- positions.indices) {
        //Check if position is not zero
        if (positions(i)(0) != 0) {
          for (y <- 0 to positions(i)(0)) {
            if (GRIDS(y)(positions(i)(1)) == 0) {
              GRIDS(y)(positions(i)(1)) = GRIDS(positions(i)(0))(positions(i)(1))
              GRIDS(positions(i)(0))(positions(i)(1)) = 0
            } else if (GRIDS(y)(positions(i)(1)) == GRIDS(positions(i)(0))(positions(i)(1))) {
              GRIDS(y)(positions(i)(1)) = GRIDS(y)(positions(i)(1)) * 2
              GRIDS(positions(i)(0))(positions(i)(1)) = 0
            }
          }
        }
      }
      grid_dispaly(GRIDS)
    }
  }

  /**
  def rotateArray(arr: Array[Array[Int]]): Array[Array[Int]] = {
    val rotate: Array[Array[Int]] = Array.ofDim(4, 4)
    var c: Int = 0
    for (counter <- arr.indices) {
      rotate(counter) = new Array[Int](4)
      for (i <- arr.indices) {
        for (j <- arr.indices) {
          if (j == counter) {
            rotate(counter)(c) = arr(i)(j)
            c += 1
            if (c == 4) c = 0
          }
        }
      }
    }
    rotate
  }
  **/

  // Check if there are empty grids to generate a new value. If not, then check for possible moves and if there are none, game over.
  def generateNewValue(): Unit = {
    val temp: Array[Array[Int]] = checkForZero()
    val positions: Array[Int] = temp(RANDOM.nextInt(temp.length))
    if (temp.length == 0) {
      possibleMove()
    } else {
      GRIDS(positions(0))(positions(1)) = newTileValue()
    }
  }
}


  object Game extends App {
    val game: Game = new Game
    game.startGame()
  }
