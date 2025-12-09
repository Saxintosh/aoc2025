package y2025.day09

import Day
import Point
import findPolygonPoints
import xInclusiveRange
import y2025.day09.Aoc09.isRectangleInPolygon
import y2025.day09.Aoc09.sortByArea
import yInclusiveRange
import kotlin.math.abs

private object Aoc09: Day() {
	override val benchmarkRepetition = 1

	fun area(point1: Point, point2: Point): Long = abs(point1.x.toLong() - point2.x.toLong() + 1) * abs(point1.y.toLong() - point2.y.toLong() + 1)

	fun sortByArea(points: List<Point>): List<Triple<Point, Point, Long>> {
		val result = mutableListOf<Triple<Point, Point, Long>>()

		for (i in points.indices) {
			for (j in i + 1 until points.size) {
				val d = area(points[i], points[j])
				result.add(Triple(points[i], points[j], d))
			}
		}
		return result.sortedBy { it.third }
	}

	fun isValid(p1: Point, p2: Point, polygon: Set<Point>): Boolean {
		val rectPoints = mutableSetOf<Point>()

		// Determina la Bounding Box (minX, maxX, minY, maxY)
		val minX = minOf(p1.x, p2.x)
		val maxX = maxOf(p1.x, p2.x)
		val minY = minOf(p1.y, p2.y)
		val maxY = maxOf(p1.y, p2.y)

		// Itera su tutti i punti all'interno e sul bordo
		for (x in xInclusiveRange(p1,p2)) {
			for (y in yInclusiveRange(p1,p2)) {
				if (Point(x, y) !in polygon)
					return false
			}
		}
		return true
	}

	fun isRectangleInPolygon(
		rectangle: Triple<Point, Point, Long>,
		polygonPoints: Set<Point>
	): Boolean {
		val p1 = rectangle.first
		val p2 = rectangle.second
		val minX = minOf(p1.x, p2.x)
		val maxX = maxOf(p1.x, p2.x)
		val minY = minOf(p1.y, p2.y)
		val maxY = maxOf(p1.y, p2.y)
		val rectCorners = listOf(
			Point(minX, minY),
			Point(maxX, minY),
			Point(minX, maxY),
			Point(maxX, maxY)
		)
		// ottimizzazione:
		if (rectCorners.any { it !in polygonPoints })
			return false
		return isValid(p1, p2, polygonPoints)
	}

}

fun main() {
	Aoc09
		.test(
			"""7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3"""
		)
		.initPart1(50L, 4759930955L)
		.inputAsLines { lines ->
			val points = lines.map { Point.fromLine(it) }
			val squares = sortByArea(points)
			squares.last().third
		}
		.initPart2(24L, 1L)
		.inputAsLines { lines ->
			val points = lines.map { Point.fromLine(it) }
			val squares = sortByArea(points)
			println(squares.size)
			val polygon = findPolygonPoints(points)
			val filtered = squares.filter { isRectangleInPolygon(it, polygon) }
			filtered.last().third
		}
}

// 1525207666 too low