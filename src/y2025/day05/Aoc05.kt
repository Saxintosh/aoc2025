package y2025.day05

import Day
import kotlin.math.max

private object Aoc05: Day()

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
		.inputAsText { text ->
			val (top, bottom) = text.split("\n\n")
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
		.inputAsText { text ->
			val (top, _) = text.split("\n\n")
			val freshIDS = top.lines()
				.map { line ->
					val (a, b) = line.split("-")
					(a.toLong() .. b.toLong())
				}
				.sortedBy { it.first }

			// ordered, now I have to look for overlaps
			var nextIndex = -1L
			var sum = 0L
			freshIDS.forEach { range ->
				val currentStart = max(range.first, nextIndex)
				if (range.last >= currentStart) {
					nextIndex = range.last + 1
					sum += (nextIndex - currentStart)
				}
			}
			sum
		}
}