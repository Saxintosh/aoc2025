package y2025.day07

import ChGrid
import Day
import Point

private object Aoc07: Day() {
	data class TimeLine(var p: Point) {
		var count = 1L
		fun down() {
			p = p.down()
		}
	}

	class TLSet: HashSet<TimeLine>() {
		fun split(tl: TimeLine) {
			val t1 = TimeLine(tl.p.left()).also { it.count = tl.count }
			val t2 = TimeLine(tl.p.right()).also { it.count = tl.count }
			addAndCount(t1)
			addAndCount(t2)
		}

		fun addAndCount(tl: TimeLine) {
			val t = this.find { it.p == tl.p }
			if (t == null)
				this.add(tl)
			else
				t.count += tl.count
		}
	}
}

fun main() {
	Aoc07
		.test(
			""".......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
..............."""
		)
		.initPart1(21, 1628)
		.inputAsLines { lines ->
			val g = ChGrid(lines)
			val s = g.findFirst('S')!!
			var split = 0
			val beams = mutableSetOf(s)
			while (beams.isNotEmpty()) {
				if (beams.first() !in g)
					break
				beams.toList().forEach { beam ->
					beams -= beam
					val b = beam.down()
					if (g[b] == '^') {
						split++
						val b1 = b.left()
						val b2 = b.right()
						if (b1 in g) beams += b1
						if (b2 in g) beams += b2
					} else {
						beams += b
					}
				}
			}
			split
		}
		.initPart1(21, 1628)
		.inputAsLines { lines ->
			val lines2 = lines.filter { !(it.all { c -> c == '.' }) }
			val g = ChGrid(lines2)
			val s = g.findFirst('S')!!
			var split = 0
			var beams = mutableSetOf(s)
			while (beams.first() in g) {
				val newBeams = mutableSetOf<Point>()
				beams.map { it.down() }.forEach {
					if (g[it] == '^') {
						newBeams.add(it.left())
						newBeams.add(it.right())
						split++
					} else
						newBeams.add(it)
				}
				beams = newBeams
			}
			split
		}
		.initPart2(40L, 27055852018812L)
		.inputAsLines { lines ->
			val lines2 = lines.filter { !(it.all { c -> c == '.' }) }
			val g = ChGrid(lines2)
			val s = g.findFirst('S')!!
			var tlSet = Aoc07.TLSet()
			tlSet.add(Aoc07.TimeLine(s))
			while (tlSet.first().p in g) {
				val newTlSet = Aoc07.TLSet()
				tlSet.forEach { tl ->
					tl.down()
					if (g[tl.p] == '^') {
						newTlSet.split(tl)
					} else
						newTlSet.addAndCount(tl)
				}
				tlSet = newTlSet
			}
			tlSet.sumOf { it.count }
		}

}