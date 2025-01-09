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
    GRIDS(0)(0) = 2
    GRIDS(1)(0) = 2
    GRIDS(2)(0) = 4
    GRIDS(3)(0) = 8

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
      try {
        input = StdIn.readChar()
      } catch {
        case e: java.lang.StringIndexOutOfBoundsException => println()
      }
      if (inputs.contains(input)) {
        state = false
      } else {
        println("Please, enter a valid direction.")
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
    var pos: Int = 0
    for (i <- GRIDS(0).indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) == 0) {
          result(pos) = Array[Int](i, j)
          pos += 1
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
    val moveable: Array[Array[Int]] = Array.ofDim(4, 4)
    var pos: Int = 0
    for (i <- GRIDS(0).indices) {
      for (j <- GRIDS(i).indices) {
        if (GRIDS(i)(j) != 0) {
          positions(pos) = Array[Int](i, j)
          pos += 1
        }
      }
    }
    if (direction == 'w' || direction == 'W') {
      for (i <- positions.indices) {
        val row = positions(i)(0)
        val col = positions(i)(1)
        //Check if pos is not zero
        if (row != 0) {
          for (y <- 0 to row) {
            if (GRIDS(y)(col) == 0) {
              GRIDS(y)(col) = GRIDS(row)(col)
              GRIDS(row)(col) = 0
            } else if (GRIDS(y)(col) == GRIDS(row)(col) && moveable(y)(col) != 1) {
              GRIDS(y)(col) *= 2
              GRIDS(row)(col) = 0
              moveable(y)(col) = 1
            }
          }
        }
      }
    } else if (direction == 'a' || direction == 'A') {
      for (i <- positions.indices) {
        val row = positions(i)(0)
        val col = positions(i)(1)
        //Check if pos is not zero
        if (col != 0) {
          for (x <- col to 0 by -1) {
            if (GRIDS(x)(col) == 0) {
              GRIDS(x)(col) = GRIDS(row)(col)
              GRIDS(row)(col) = 0
            } else if (GRIDS(x)(col) == GRIDS(row)(col) && x != row && moveable(x)(col) != 1) {
              GRIDS(x)(col) *= 2
              GRIDS(row)(col) = 0
              moveable(x)(col) = 1
            }
          }
        }
      }
    } else if (direction == 's' || direction == 'S') {
      for (i <- positions.indices) {
        val row = positions(i)(0)
        val col = positions(i)(1)
        //Check if pos is not 3
        if (row != 3) {
          for (y <- 3 to row by -1) {
            if (GRIDS(y)(col) == 0) {
              GRIDS(y)(col) = GRIDS(row)(col)
              GRIDS(row)(col) = 0
            } else if (GRIDS(y)(col) == GRIDS(row)(col) && moveable(y)(col) != 1) {
              GRIDS(y)(col) *= 2
              GRIDS(row)(col) = 0
              moveable(y)(col) = 1
            }
          }
        }
      }
      grid_dispaly(moveable)
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
