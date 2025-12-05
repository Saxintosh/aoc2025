package y2025.day05

import Day
import kotlin.math.max

private object Aoc05: Day() {

}

fun main() {
	Aoc05
		.test(
			"""3-5
10-14
16-20
12-18

1
5
8
11
17
32"""
		)
		.initPart1(3, 888)
		.inputAsText {
			val (top, bottom) = it.split("\n\n")
			val freshIDS = top.lines()
				.map { line ->
					val (a, b) = line.split("-")
					(a.toLong() .. b.toLong())
				}

			val ingredients = bottom.lines().map { it.toLong() }
			ingredients
				.count { ingredient ->
					freshIDS.any { ingredient in it }
				}
		}
		.initPart2(14L, 344378119285354L)
		.inputAsText {
			val (top, _) = it.split("\n\n")
			val freshIDS = top.lines()
				.map { line ->
					val (a, b) = line.split("-")
					(a.toLong() .. b.toLong())
				}
				.sortedWith(
					compareBy<LongRange> { it.start }
						.thenBy { it.endInclusive }
				)

			// ordered, now I have to look for overlaps
			var nextIndex = -1L
			var sum = 0L
			freshIDS.forEach {
				val currentStart = max(it.start, nextIndex)
				if (it.endInclusive >= currentStart) {
					nextIndex = it.endInclusive + 1
					sum += (nextIndex - currentStart)
				}
			}
			sum
		}
}