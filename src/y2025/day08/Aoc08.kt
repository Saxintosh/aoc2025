package y2025.day08

import Day
import y2025.day08.Aoc08.sortByDistances
import y2025.day08.Aoc08.toPoint3D
import java.util.*

private object Aoc08: Day() {
	override val benchmarkRepetition = 1

	data class Point3D(val x: Long, val y: Long, val z: Long) {
		override fun toString() = "($x, $y, $z)"
	}

	fun String.toPoint3D(): Point3D {
		val (x, y, z) = this.split(",").map(String::toLong)
		return Point3D(x, y, z)
	}

	fun distance(a: Point3D, b: Point3D): Long {
		val dx = a.x - b.x
		val dy = a.y - b.y
		val dz = a.z - b.z
		return dx * dx + dy * dy + dz * dz
	}

	fun sortByDistances(points: List<Point3D>): List<Triple<Point3D, Point3D, Long>> {
		val result = mutableListOf<Triple<Point3D, Point3D, Long>>()

		for (i in points.indices) {
			for (j in i + 1 until points.size) {
				val d = distance(points[i], points[j])
				result.add(Triple(points[i], points[j], d))
			}
		}

		return result.sortedBy { it.third }
	}

	class Circuit: HashSet<Point3D>()
	class Circuits: LinkedList<Circuit>() {
		fun findCircuit(p: Point3D) = find { p in it }

		fun addSegment(s: Triple<Point3D, Point3D, Long>) {
			val (p1, p2, _) = s
			val c1 = findCircuit(p1)
			val c2 = findCircuit(p2)
			when {
				c1 == null && c2 == null -> {
					val newCircuit = Circuit()
					newCircuit.add(p1)
					newCircuit.add(p2)
					add(newCircuit)
				}

				c1 != null && c2 == null -> {
					c1.add(p2)
				}

				c1 == null && c2 != null -> {
					c2.add(p1)
				}

				c1 == c2                 -> {
					//skip
				}

				c1 != null && c2 != null -> {
					c1.addAll(c2)
					remove(c2)
				}

				else                     -> throw Exception("UNEXPECTED")
			}

		}
	}
}

fun main() {
	Aoc08
		.test(
			"""162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689"""
		)
		.initPart1(40, 68112)
		.inputAsLines { lines ->
			val points = lines.map { line ->
				line.toPoint3D()
			}

			val circuits = Aoc08.Circuits()
			val toTake = if (Aoc08.isTest) 10 else 1000
			println("Take: $toTake")
			sortByDistances(points)
				.also { println("Size: ${it.size}") }
				.asSequence()
				.take(toTake)
				.forEach(circuits::addSegment)

			circuits.map { it.size }.sorted().takeLast(3).fold(1) { acc, i -> acc * i }
		}
		.initPart2(25272L, 44543856L)
		.inputAsLines { lines ->
			val points = lines.map { line ->
				line.toPoint3D()
			}

			val circuits = Aoc08.Circuits()
			val pointsTotal = points.size
			var last: Triple<Aoc08.Point3D, Aoc08.Point3D, Long>? = null
			sortByDistances(points)
				.also { println("Size: ${it.size}") }
				.asSequence()
				.takeWhile { circuits.firstOrNull()?.size != pointsTotal }
				.forEach {
					last = it
					circuits.addSegment(it)
				}

			last!!.first.x * last.second.x
		}
}