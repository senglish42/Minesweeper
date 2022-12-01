import kotlin.random.Random

fun createDiceGameRandomizer(n: Int): Int {
    var count = -1
    while (++count >= 0) {
        val seed = Random(count)
        var friendRes = 0
        repeat(n) { friendRes += seed.nextInt(1, 7) }
        var myRes = 0
        repeat(n) { myRes += seed.nextInt(1, 7) }
        if (myRes > friendRes) break
    }
    return count
}