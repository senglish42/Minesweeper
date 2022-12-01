package minesweeper

import kotlin.random.Random

const val NOT_VALID_NUM = "First two arguments should be numbers between 1 and 9"
const val NOT_VALID_MINES = "You should input a number between 1 and 81"
fun Array<Char>.fillRandom(num: Int) {
    val rand = Random.Default
    repeat(num) {
        while (true) {
            val res = rand.nextInt(0, 81)
            if (this[res] == '.') { this[res] = 'X' ; break }
        }
    }
}

class Minesweeper {
    private val arr = Array(81) {'.'}
    private var mines = 0
    private var minesLeft = 0
    private var isMineChosen = false
    fun run() {
        println("How many mines do you want on the field?")
        this.mines = readln().toIntOrNull() ?: -1
        if (this.mines !in 1..81) { println(NOT_VALID_MINES) ; return }
        arr.fillRandom(this.mines)
        this.minesLeft = this.mines
        while (show() && this.minesLeft > 0) {
            while (true) {
                try {
                    println("Set/unset mines marks or claim a cell as free:")
                    val str = readln().split(' ')
                    if (str.size != 3) throw Exception("Not valid amount of arguments")
                    val (x, y) = str.subList(0, 2).map {
                        if (it.toIntOrNull() == null || it.toInt() !in 1..9) throw Exception(NOT_VALID_NUM)
                        it.toInt() - 1
                    }
                    if (str[2] !in arrayOf("free", "mine")) throw Exception("The third argument should be mine of free")
                    if (arr[y * 9 + x] in '1'..'8') throw Exception("There is a number here!")
                    if (arr[y * 9 + x] == '/') throw Exception("The cell is already free!")
                    if (str[2] == "free") {
                        if (arr[y * 9 + x] == 'X') { isMineChosen = true ; break }
                        if (arr[y * 9 + x] == '.') { unlockCells(x, y) ; break }
                        if (arr[y * 9 + x] == '*') { isMineChosen = true ; break }
                    } else {
                        if (arr[y * 9 + x] == 'X') { arr[y * 9 + x] = '*' ; --this.minesLeft ; break }
                        else if (arr[y * 9 + x] == '*') { arr[y * 9 + x] = 'X' ; ++this.minesLeft ; break }
                        else if (arr[y * 9 + x] == '.') { arr[y * 9 + x] = '%' ; break }
                        else if (arr[y * 9 + x] == '%') { arr[y * 9 + x] = '.' ; break }
                    }
                } catch(e: Exception) {
                    println(e.message)
                }
            }
        }
        println(if (isMineChosen) "You stepped on a mine and failed!" else "Congratulations! You found all the mines!")
    }
    private fun unlockCells(x: Int, y: Int, mine: Array<Char> = arrayOf('X', '*'), free: Array<Char> = arrayOf('.', '%')) {
        var count = 0
        if (x > 0 && arr[y * 9 + x - 1] in mine) ++count
        if (x < 8 && arr[y * 9 + x + 1] in mine) ++count
        if (y > 0 && arr[(y - 1) * 9 + x] in mine) ++count
        if (y < 8 && arr[(y + 1) * 9 + x] in mine) ++count
        if (y > 0 && x > 0 && arr[(y - 1) * 9 + x - 1] in mine) ++count
        if (y > 0 && x < 8 && arr[(y - 1) * 9 + x + 1] in mine) ++count
        if (y < 8 && x > 0 && arr[(y + 1) * 9 + x - 1] in mine) ++count
        if (y < 8 && x < 8 && arr[(y + 1) * 9 + x + 1] in mine) ++count
        if (count > 0) arr[y * 9 + x] = Character.forDigit(count, 10)
        else {
            arr[y * 9 + x] = '/'
            if (x > 0 && arr[y * 9 + x - 1] in free) unlockCells(x - 1, y)
            if (x < 8 && arr[y * 9 + x + 1] in free) unlockCells(x + 1, y)
            if (y > 0 && arr[(y - 1) * 9 + x] in free) unlockCells(x, y - 1)
            if (y < 8 && arr[(y + 1) * 9 + x] in free) unlockCells(x, y + 1)
            if (y > 0 && x > 0 && arr[(y - 1) * 9 + x - 1] in free) unlockCells(x - 1, y - 1)
            if (y > 0 && x < 8 && arr[(y - 1) * 9 + x + 1] in free) unlockCells(x + 1, y - 1)
            if (y < 8 && x > 0 && arr[(y + 1) * 9 + x - 1] in free) unlockCells(x - 1, y + 1)
            if (y < 8 && x < 8 && arr[(y + 1) * 9 + x + 1] in free) unlockCells(x + 1, y + 1)
        }
    }
    private fun show(): Boolean {
        println("""
                 │123456789│
                —│—————————│
                """.trimIndent())
        for (i in 0..8) {
            print("${i + 1}│")
            for (n in 0..8) {
                print(if (isMineChosen) {
                        if (arr[i * 9 + n] == '%') '*' else if (arr[i * 9 + n] == '*') 'X'  else arr[i * 9 + n]
                        } else if (arr[i * 9 + n] !in arrayOf('X', '%')) arr[i * 9 + n]
                        else if (arr[i * 9 + n] == '%') '*'
                        else '.')
            }
            print("│\n")
        }
        println("—│—————————│")
        return if (arr.count { it in arrayOf('.', '%') } == this.mines) false else !isMineChosen
    }
}

fun main() = Minesweeper().run()
