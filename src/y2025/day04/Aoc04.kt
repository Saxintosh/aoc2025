package y2025.day04

import ChGrid
import Day
import Point
import y2025.day04.Aoc04.fewerThanFourRollsOfPaper
import y2025.day04.Aoc04.removeAndCount


private object Aoc04: Day() {
	fun Point.fewerThanFourRollsOfPaper(g: ChGrid) = adjacent().count { g[it] == '@' } < 4

	fun Sequence<Pair<Point, Char>>.removeAndCount(g: ChGrid) = count { pAndCh ->
		val p = pAndCh.first
		val ch = pAndCh.second
		when (ch) {
			'x'  -> true
			'@'  -> {
				if (p.fewerThanFourRollsOfPaper(g))
					true.also { g[p] = 'x' }
				else
					false
			}

			else -> {
				false
			}
		}
	}

}

fun main() {
	Aoc04
		.test(
			"""..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@."""
		)
		.initPart1(13, 1516)
		.lines {
			val g = ChGrid(it)
			g.asPointsSequenceAndValue()
				.count { p -> p.second == '@' && p.first.fewerThanFourRollsOfPaper(g) }
		}
		.initPart2(43, 1)
		.lines {
			val g = ChGrid(it)
			var last = 0
			var current = 0
			do {
				last = current
				current = g.asPointsSequenceAndValue().removeAndCount(g)
			} while (current > last)
			current
		}

}