import scala.util.Random
import hevs.graphics.FunGraphics

import java.awt.event.KeyEvent
import java.awt.{Color, Rectangle}


class Game_Window {
  private val WIDTH: Int = 400
  private val HEIGHT: Int = 450
  private val GRAPHICS: FunGraphics = new FunGraphics(WIDTH, HEIGHT, "2048")
  private val RANDOM: Random = new Random()
  private var GRIDS: Array[Array[Int]] = Array.ofDim(4, 4)
  private var SCORE: Int = 0
  private var GAME_STATE: Boolean = false

  def update(): Unit = {
    GRAPHICS.clear(new Color(187, 173, 160))

    val padding: Int = 20
    val tileSpacing: Int = 5
    val rows: Int = GRIDS.length
    val cols: Int = GRIDS(0).length
    val totalPadding: Int = 2 * padding + (cols - 1) * tileSpacing
    val tileWidth: Int = (WIDTH - totalPadding) / cols
    val tileHeight: Int = (HEIGHT - 50 - totalPadding) / rows

    for (row <- GRIDS.indices) {
      for (col <- GRIDS(row).indices) {
        val x: Int = padding + col * (tileWidth + tileSpacing)
        val y: Int = padding + row * (tileHeight + tileSpacing)
        GRAPHICS.setColor(getTileColor(GRIDS(row)(col)))
        GRAPHICS.setPenWidth(3)
        GRAPHICS.drawFillRect(x, y, tileWidth, tileHeight)

        // Draw the value centered in the rectangle
        val value: String = GRIDS(row)(col).toString
        if (value != "0") {
          val textWidth: Int = value.length * 20
          val textX: Int = x + (tileWidth - textWidth) / 2 // Adjust for approximate centering
          val textY: Int = y + tileHeight / 2 + 15 // Adjust for vertical centering
          val color: Color = if (value.toInt <= 4) new Color(119, 110, 101) else new Color(249, 246, 242)
          GRAPHICS.drawString(textX, textY, value, color, 35)
        }
      }
    }
    // Draw the score at the bottom of the window
    GRAPHICS.drawString(padding, HEIGHT - padding, s"Score: $SCORE", new Color(249, 246, 242), 40)
  }

  def getTileColor(value: Int): Color = {
    value match {
      case 0 => new Color(204, 192, 179)  // Empty tile color
      case 2 => new Color(238, 228, 218)  // 2 tile color
      case 4 => new Color(237, 224, 200)  // 4 tile color
      case 8 => new Color(242, 177, 121)  // 8 tile color
      case 16 => new Color(245, 149, 99)  // 16 tile color
      case 32 => new Color(246, 124, 95)  // 32 tile color
      case 64 => new Color(246, 94, 59)   // 64 tile color
      case 128 => new Color(237, 207, 114) // 128 tile color
      case 256 => new Color(237, 204, 97)  // 256 tile color
      case 512 => new Color(237, 204, 97)  // 512 tile color
      case 1024 => new Color(237, 204, 97) // 1024 tile color
      case 2048 => new Color(237, 204, 97) // 2048 tile color
      case _ => new Color(119, 110, 101)   // Default color for large values
    }
  }

  def newGame(): Unit = {
    GRIDS = Array.ofDim(4, 4)  // Reset the grid to an empty 4x4 grid
    SCORE = 0  // Reset the score
    GAME_STATE = true  // Set the game state to true (game is active)
    generateNewValue()  // Generate the first value
    generateNewValue()  // Generate the second value
    update()  // Update the window
  }


  def gameOver(): Unit = {
    GAME_STATE = false
    GRAPHICS.clear(Color.BLACK)
    GRAPHICS.setColor(Color.WHITE)
    GRAPHICS.drawString(WIDTH / 2 - 50, HEIGHT / 2, s"Game Over! Score: $SCORE")
    GRAPHICS.drawString(WIDTH / 2 - 50, HEIGHT / 2 + 20, "Restart? Y/N")
  }

  def startGame(): Unit = {
    newGame()  // Initialize a new game
    userInput()  // Set up key listeners for user input
    while (GAME_STATE) {
      if (!possibleMove()) {
        gameOver()
      }
    }
  }


  def userInput(): Unit = {
    GRAPHICS.setKeyManager(new java.awt.event.KeyAdapter {
      override def keyPressed(e: KeyEvent): Unit = {
        e.getKeyCode match {
          case KeyEvent.VK_W | KeyEvent.VK_UP => move('w')
          case KeyEvent.VK_A | KeyEvent.VK_LEFT => move('a')
          case KeyEvent.VK_S | KeyEvent.VK_DOWN => move('s')
          case KeyEvent.VK_D | KeyEvent.VK_RIGHT => move('d')
          case KeyEvent.VK_Y if !GAME_STATE => startGame()
          case KeyEvent.VK_N if !GAME_STATE => System.exit(-1)
          case KeyEvent.VK_ESCAPE => gameOver()
        }
      }
    })
  }

  def move(direction: Char): Unit = {
    direction match {
      case 'w' => moveUp()
      case 'a' => moveLeft()
      case 's' => moveDown()
      case 'd' => moveRight()
    }
    generateNewValue()
    update()
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


}

object Game extends App {
  new Game_Window().startGame()
}
