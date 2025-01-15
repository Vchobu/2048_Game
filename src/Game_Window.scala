/**
 * Before execution, make sure to mark src directory as "Sources Root"
 * To do this click right mouse button on src -> Mark Directory as -> Sources Root
 * After that directory icon will become blue.
 *
 * Also, don't forget to add FunGraphics as library
 */

import scala.util.Random
import hevs.graphics.FunGraphics

import java.awt.event.{KeyEvent, KeyAdapter}
import java.awt.Color

/**
 * This class represents the game window and logic for the 2048 game.
 */
class Game_Window {
  // Constants for window dimensions and game graphics
  private val WIDTH: Int = 400 // Width of the game window
  private val HEIGHT: Int = 450 // Height of the game window
  private val GRAPHICS: FunGraphics = new FunGraphics(WIDTH, HEIGHT, "2048") // FunGraphics instance for drawing
  private val RANDOM: Random = new Random() // Random instance for generating random values

  // Game state variables
  private var GRIDS: Array[Array[Int]] = Array.ofDim[Int](4, 4) // 4x4 grid to represent the game board
  private var SCORE: Int = 0 // Player's score
  private var GAME_STATE: Boolean = false // Tracks if the game is active

  /**
   * Updates the game window by redrawing the game board and score.
   */
  def update(): Unit = {
    GRAPHICS.clear(new Color(187, 173, 160)) // Clear the window with the background color

    val padding: Int = 20 // Padding around the board
    val tileSpacing: Int = 5 // Spacing between tiles
    val rows: Int = GRIDS.length // Number of rows in the grid
    val cols: Int = GRIDS(0).length // Number of columns in the grid
    val totalPadding: Int = 2 * padding + (cols - 1) * tileSpacing // Total padding for calculating tile sizes
    val tileWidth: Int = (WIDTH - totalPadding) / cols // Width of each tile
    val tileHeight: Int = (HEIGHT - 50 - totalPadding) / rows // Height of each tile

    for (row <- GRIDS.indices) {
      for (col <- GRIDS(row).indices) {
        val x: Int = padding + col * (tileWidth + tileSpacing) // X-coordinate of the tile
        val y: Int = padding + row * (tileHeight + tileSpacing) // Y-coordinate of the tile

        GRAPHICS.setColor(getTileColor(GRIDS(row)(col))) // Set the color based on the tile value
        GRAPHICS.setPenWidth(3) // Set the border width of tiles
        GRAPHICS.drawFillRect(x, y, tileWidth, tileHeight) // Draw the filled rectangle for the tile

        // Draw the tile's value, centered within the rectangle
        val value: String = GRIDS(row)(col).toString
        if (value != "0") { // Only draw non-zero values
          val textX: Int = x + (tileWidth - value.length * 20) / 2 // X-coordinate for centered text
          val textY: Int = y + tileHeight / 2 + 15 // Y-coordinate for centered text
          val color: Color = if (value.toInt <= 4) new Color(119, 110, 101) else new Color(249, 246, 242) // Text color
          GRAPHICS.drawString(textX, textY, value, color, 35) // Draw the tile's value
        }
      }
    }
    // Draw the score at the bottom of the window
    GRAPHICS.drawString(padding, HEIGHT - padding, s"Score: $SCORE", new Color(249, 246, 242), 40)
  }

  /**
   * Determines the color of a tile based on its value.
   */
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
      case _ => new Color(119, 110, 101)   // Default color for other values
    }
  }

  /**
   * Starts a new game by resetting the board, score, and generating initial values.
   */
  def startGame(): Unit = {
    GRIDS = Array.ofDim[Int](4, 4)  // Reset the grid to an empty 4x4 grid
    SCORE = 0  // Reset the score
    GAME_STATE = true  // Set the game state to true (game is active)
    generateNewValue()  // Generate the first value
    generateNewValue()  // Generate the second value
    update()  // Update the window
  }

  /**
   * Ends the game with an animation and displays a game over message.
   */
  def gameOver(): Unit = {
    GAME_STATE = false // Mark the game as inactive

    val maxSize: Int = 450 // Maximum size for the animation
    val animationSpeed: Int = 20 // Speed of the animation
    val stepSize: Int = 25 // Size increment per animation step
    var currentSize: Int = 0 // Initial size of the animation square

    // Animation loop
    while (currentSize <= maxSize) {
      update() // Update the game board
      GRAPHICS.setColor(Color.BLACK) // Black color for the game-over animation
      GRAPHICS.drawFillRect(WIDTH / 2 - currentSize / 2, HEIGHT / 2 - currentSize / 2, currentSize, currentSize) // Draw the expanding square
      currentSize += stepSize // Increase the size for the next frame
      Thread.sleep(animationSpeed) // Pause to create the animation effect
    }

    // Draw the final game-over message
    GRAPHICS.drawString(WIDTH / 2 - 150, HEIGHT / 2, s"Game Over! Score: $SCORE", Color.WHITE, 30) // Display game over message
    GRAPHICS.drawString(WIDTH / 2 - 100, HEIGHT / 2 + 30, "Restart? Y/N", Color.WHITE, 30) // Display restart prompt
  }

  /**
   * Sets up the key listener for user input to control the game.
   */
  def userInput(): Unit = {
    GRAPHICS.setKeyManager(new KeyAdapter {
      override def keyPressed(e: KeyEvent): Unit = {
        e.getKeyCode match {
          case KeyEvent.VK_W | KeyEvent.VK_UP if GAME_STATE => move('w') // Move up
          case KeyEvent.VK_A | KeyEvent.VK_LEFT if GAME_STATE => move('a') // Move left
          case KeyEvent.VK_S | KeyEvent.VK_DOWN if GAME_STATE => move('s') // Move down
          case KeyEvent.VK_D | KeyEvent.VK_RIGHT if GAME_STATE => move('d') // Move right
          case KeyEvent.VK_Y if !GAME_STATE => startGame() // Restart game
          case KeyEvent.VK_N if !GAME_STATE => System.exit(-1) // Exit the game
          case KeyEvent.VK_ESCAPE => gameOver() // End the game
          case _ => // Ignore other inputs
        }
      }
    })
  }

  /**
   * Handles movement in the specified direction and updates the game state.
   * The character representing the movement direction ('w', 'a', 's', or 'd').
   */
  def move(direction: Char): Unit = {
    direction match {
      case 'w' => moveUp()    // Move tiles up
      case 'a' => moveLeft()  // Move tiles left
      case 's' => moveDown()  // Move tiles down
      case 'd' => moveRight() // Move tiles right
    }
    generateNewValue() // Add a new tile to the grid after a move
    update() // Redraw the game state
    if (!possibleMove()) { // Check if further moves are possible
      gameOver() // End the game if no moves are possible
    }
  }

  /**
   * Moves all tiles in the game grid upwards, merging tiles with the same value.
   *
   * This method processes each column of the game grid (`GRIDS`) as follows:
   * - Tiles are moved to the topmost available position in their column.
   * - Adjacent tiles with the same value are merged into one, doubling the value.
   * - The `SCORE` is updated based on the values of merged tiles.
   *
   * ## Implementation Details:
   * - The `pointer` variable tracks the next available position for a tile in the current column.
   * - The `merged` array ensures that each tile can only be merged once per move.
   *
   * - Empty tiles (value `0`) are skipped during processing.
   */
  def moveUp(): Unit = {
    for (col <- 0 until 4) {
      var pointer: Int = 0 // Pointer for the next position to fill
      val merged: Array[Boolean] = Array.fill(4)(false) // Tracks whether a tile has already been merged
      for (row <- 0 until 4) {
        if (GRIDS(row)(col) != 0) {
          if (pointer > 0 && GRIDS(pointer - 1)(col) == GRIDS(row)(col) && !merged(pointer - 1)) {
            GRIDS(pointer - 1)(col) *= 2
            SCORE += GRIDS(pointer - 1)(col) // Update the score
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

  /**
   * Moves all tiles in the grid to the left, merging tiles with the same value.
   *
   * This method processes each row of the grid (`GRIDS`) to move non-zero tiles
   * to the leftmost positions. If two adjacent tiles have the same value, they
   * are merged into a single tile, and the score is updated accordingly.
   *
   * ## Detailed Process:
   * - Iterates through each row in the grid.
   * - Tracks the position (`pointer`) where the next tile should be placed.
   * - Merges tiles with the same value if they have not been merged already.
   * - Updates the grid and score after each move.
   */
  def moveLeft(): Unit = {
    for (row <- GRIDS) {
      var pointer: Int = 0 // Pointer for the next position to fill
      val merged: Array[Boolean] = Array.fill(4)(false) // Tracks whether a tile has already been merged
      for (j <- row.indices) {
        if (row(j) != 0) {
          if (pointer > 0 && row(pointer - 1) == row(j) && !merged(pointer - 1)) {
            row(pointer - 1) *= 2
            SCORE += row(pointer - 1) // Update the score
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

  /**
   * Moves all tiles in the grid downward, merging tiles with the same value.
   *
   * Processes each column from bottom to top, moving non-zero tiles downward to the closest empty
   * position or merging them with adjacent tiles of the same value. Updates the score accordingly.
   */
  def moveDown(): Unit = {
    for (col <- 0 until 4) {
      var pointer: Int = 3 // Pointer for the next position to fill (starting from the bottom)
      val merged: Array[Boolean] = Array.fill(4)(false) // Tracks whether a tile has already been merged
      for (row <- (0 until 4).reverse) {
        if (GRIDS(row)(col) != 0) {
          if (pointer < 3 && GRIDS(pointer + 1)(col) == GRIDS(row)(col) && !merged(pointer + 1)) {
            GRIDS(pointer + 1)(col) *= 2
            SCORE += GRIDS(pointer + 1)(col) // Update the score
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

  /**
   * Moves all tiles in the grid to the right, merging tiles with the same value.
   *
   * Processes each row from right to left, moving non-zero tiles to the rightmost
   * available positions or merging them with adjacent tiles of the same value.
   * Updates the score accordingly.
   */
  def moveRight(): Unit = {
    for (row <- GRIDS) {
      var pointer: Int = row.length - 1 // Pointer for the next position to fill (starting from the right)
      val merged: Array[Boolean] = Array.fill(4)(false) // Tracks whether a tile has already been merged
      for (j <- row.indices.reverse) {
        if (row(j) != 0) {
          if (pointer < row.length - 1 && row(pointer + 1) == row(j) && !merged(pointer + 1)) {
            row(pointer + 1) *= 2
            SCORE += row(pointer + 1) // Update the score
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

  /**
   * Finds all empty positions in the grid.
   * Return an array of empty positions as (row, column) pairs.
   */
  def checkForZero(): Array[Array[Int]] = {
    var counter: Int = 0 // Counter for empty positions
    for (i <- GRIDS.indices; j <- GRIDS(i).indices if GRIDS(i)(j) == 0) {
      counter += 1
    }
    val result: Array[Array[Int]] = new Array[Array[Int]](counter)
    var pos: Int = 0
    for (i <- GRIDS.indices; j <- GRIDS(i).indices if GRIDS(i)(j) == 0) {
      result(pos) = Array[Int](i, j)
      pos += 1
    }
    result
  }

  /**
   * Adds a new tile (value 2 or 4) to a random empty position on the grid.
   */
  def generateNewValue(): Unit = {
    val emptyPositions: Array[Array[Int]] = checkForZero()
    if (emptyPositions.nonEmpty) {
      val position: Array[Int] = emptyPositions(RANDOM.nextInt(emptyPositions.length))
      GRIDS(position(0))(position(1)) = if (RANDOM.nextInt(10) == 9) 4 else 2 // 10% chance for 4, otherwise 2
    }
  }

  /**
   * Checks if any moves are possible. Return true if a move is possible, false otherwise.
   */
  def possibleMove(): Boolean = {
    for (i <- 0 until 4; j <- 0 until 4) {
      if (GRIDS(i)(j) == 0) return true // Empty tile exists
      if (i < 3 && GRIDS(i)(j) == GRIDS(i + 1)(j)) return true // Merge possible vertically
      if (j < 3 && GRIDS(i)(j) == GRIDS(i)(j + 1)) return true // Merge possible horizontally
    }
    false
  }
}

/**
 * Main object to run the 2048 game.
 */
object Game_Window extends App {
  val game: Game_Window = new Game_Window // Create a new game instance
  game.userInput() // Set up user input
  game.startGame() // Start the game
}
