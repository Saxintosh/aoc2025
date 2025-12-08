package y2025.day06

import ChGrid
import Day
import Point
import y2025.day06.Aoc06.rgx

private object Aoc06: Day() {
	val rgx = Regex(" +")

	class Grid2(val g: ChGrid) {
		val xRange = g.yRange
		val yRange = g.xRange

		fun getChAt(x: Int, y: Int): Char {
			val newX = g.xRange.last - y
			val newY = x
			return g[Point(newX, newY)] ?: ' '
		}


		fun line(y: Int): String {
			val sb = StringBuilder()
			for (x in xRange) {
				sb.append(getChAt(x, y))
			}
			return sb.toString()
		}
	}
}

fun main() {
	Aoc06
		.test(
			"""123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  """
		)
		.initPart1(4277556L, 7644505810277L)
		.inputAsLines { lines ->
			val ops = lines.last().trim().split(rgx)
			val acc = LongArray(ops.size) { 0 }
			ops.forEachIndexed { idx, op ->
				if (op == "*") acc[idx] = 1
			}
			lines.dropLast(1)
				.forEach { line ->
					val nums = line.trim().split(rgx)
					nums.forEachIndexed { idx, num ->
						if (ops[idx] == "+")
							acc[idx] += num.toLong()
						else
							acc[idx] *= num.toLong()
					}
				}
			acc.sum()
		}
		.initPart2(3263827L, 12841228084455L)
		.inputAsLines {
			val rg = Aoc06.Grid2(ChGrid(it))
			var sum = 0L
			val numbers = mutableListOf<Long>()

			for (y in rg.yRange) {
				val line = rg.line(y)
				if (line.isBlank())
					continue
				val lastCh = line.last()
				if (lastCh == '+') {
					numbers += line.dropLast(1).trim().toLong()
					sum += numbers.sum()
					numbers.clear()
				} else if (lastCh == '*') {
					numbers += line.dropLast(1).trim().toLong()
					sum += numbers.fold(1L) { acc, v -> acc * v }
					numbers.clear()
				} else {
					numbers += line.trim().toLong()
				}
			}
			sum
		}


}