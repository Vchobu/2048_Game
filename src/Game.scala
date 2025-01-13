import scala.util.Random
import scala.io.StdIn

class Game {
  private val RANDOM: Random = new Random()
  private var GRIDS: Array[Array[Int]] = Array.ofDim(4, 4)
  private var SCORE: Int = 0
  private var GAME_STATE: Boolean = false

  def startGame(): Unit = {
    GRIDS = Array.ofDim(4, 4)
    SCORE = 0
    GAME_STATE = true
    generateNewValue()
    generateNewValue()
    displayGrid()
    while (GAME_STATE) {
      userInput()
      generateNewValue()
      displayGrid()
      println("Your SCORE: " + SCORE)
      if (!possibleMove()) {
        GAME_STATE = false
        gameOver()
      }
    }
  }

  def displayGrid(): Unit = {
    var s: String = ""
    for (i <- GRIDS) {
      s += i.mkString("|", " ", "|\n")
    }
    println(s)
  }

  def userInput(): Unit = {
    println("Enter a direction ('w' = up, 'a' = left, 's' = down, 'd' = right):")
    val validInputs = Set('w', 'a', 's', 'd')
    var input: Char = ' '
    do {
      input = StdIn.readChar().toLower
    } while (!validInputs.contains(input))
    move(input)
  }

  def move(direction: Char): Unit = {
    direction match {
      case 'w' => moveUp()
      case 'a' => moveLeft()
      case 's' => moveDown()
      case 'd' => moveRight()
    }
  }

  def moveUp(): Unit = {
    for (col <- 0 until 4) {
      var pointer = 0
      val merged = Array.fill(4)(false) // Track merged cells
      for (row <- 0 until 4) {
        if (GRIDS(row)(col) != 0) {
          if (pointer > 0 && GRIDS(pointer - 1)(col) == GRIDS(row)(col) && !merged(pointer - 1)) {
            GRIDS(pointer - 1)(col) *= 2
            SCORE += GRIDS(pointer - 1)(col)
            GRIDS(row)(col) = 0
            merged(pointer - 1) = true
          } else {
            if (pointer != row) {
              GRIDS(pointer)(col) = GRIDS(row)(col)
              GRIDS(row)(col) = 0
            }
            pointer += 1
          }
        }
      }
    }
  }

  def moveLeft(): Unit = {
    for (row <- GRIDS) {
      var pointer = 0
      val merged = Array.fill(4)(false) // Track merged cells
      for (j <- row.indices) {
        if (row(j) != 0) {
          if (pointer > 0 && row(pointer - 1) == row(j) && !merged(pointer - 1)) {
            row(pointer - 1) *= 2
            SCORE += row(pointer - 1)
            row(j) = 0
            merged(pointer - 1) = true
          } else {
            if (pointer != j) {
              row(pointer) = row(j)
              row(j) = 0
            }
            pointer += 1
          }
        }
      }
    }
  }

  def moveDown(): Unit = {
    for (col <- 0 until 4) {
      var pointer = 3
      val merged = Array.fill(4)(false) // Track merged cells
      for (row <- (0 until 4).reverse) {
        if (GRIDS(row)(col) != 0) {
          if (pointer < 3 && GRIDS(pointer + 1)(col) == GRIDS(row)(col) && !merged(pointer + 1)) {
            GRIDS(pointer + 1)(col) *= 2
            SCORE += GRIDS(pointer + 1)(col)
            GRIDS(row)(col) = 0
            merged(pointer + 1) = true
          } else {
            if (pointer != row) {
              GRIDS(pointer)(col) = GRIDS(row)(col)
              GRIDS(row)(col) = 0
            }
            pointer -= 1
          }
        }
      }
    }
  }

  def moveRight(): Unit = {
    for (row <- GRIDS) {
      var pointer = row.length - 1
      val merged = Array.fill(4)(false) // Track merged cells
      for (j <- row.indices.reverse) {
        if (row(j) != 0) {
          if (pointer < row.length - 1 && row(pointer + 1) == row(j) && !merged(pointer + 1)) {
            row(pointer + 1) *= 2
            SCORE += row(pointer + 1)
            row(j) = 0
            merged(pointer + 1) = true
          } else {
            if (pointer != j) {
              row(pointer) = row(j)
              row(j) = 0
            }
            pointer -= 1
          }
        }
      }
    }
  }

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

  def generateNewValue(): Unit = {
    val temp: Array[Array[Int]] = checkForZero()
    if (temp.length == 0) {
      if (!possibleMove()) {
        GAME_STATE = false
        gameOver()
      }
    } else {
      val positions: Array[Int] = temp(RANDOM.nextInt(temp.length))
      GRIDS(positions(0))(positions(1)) = if (RANDOM.nextInt(10) == 9) 4 else 2
    }
  }

  def possibleMove(): Boolean = {
    for (i <- 0 until 4) {
      for (j <- 0 until 4) {
        if (GRIDS(i)(j) == 0) return true
        if (i < 3 && GRIDS(i)(j) == GRIDS(i + 1)(j)) return true
        if (j < 3 && GRIDS(i)(j) == GRIDS(i)(j + 1)) return true
      }
    }
    false
  }

  def gameOver(): Unit = {
    println("Game Over! Your final score: " + SCORE)
  }
}

object Game extends App {
  new Game().startGame()
}
