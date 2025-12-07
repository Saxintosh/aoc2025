enum class Direction(private val degree: Int) {
	Up(0), Right(90), Down(180), Left(270), UpRight(45), DownRight(135), DownLeft(225), UpLeft(315);

	fun turnRight() = entries.find { it.degree == (degree + 90) % 360 }!!
	fun turnLeft() = entries.find { it.degree == (degree - 90) % 360 }!!
}

data class Vector(val dx: Int, val dy: Int) {

	companion object {
		fun from(p1: Point, p2: Point) = Vector(p2.x - p1.x, p2.y - p1.y)
	}
}

data class Point(val x: Int, val y: Int) {
	fun up() = Point(x, y - 1)
	fun down() = Point(x, y + 1)
	fun left() = Point(x - 1, y)
	fun upLeft() = Point(x - 1, y - 1)
	fun downLeft() = Point(x - 1, y + 1)
	fun right() = Point(x + 1, y)
	fun upRight() = Point(x + 1, y - 1)
	fun downRight() = Point(x + 1, y + 1)

	fun adjacent() = buildList {
		add(up())
		add(down())
		add(left())
		add(right())
		add(upLeft())
		add(upRight())
		add(downLeft())
		add(downRight())
	}

	fun crossAdjacent() = buildList {
		add(up())
		add(down())
		add(left())
		add(right())
	}

	operator fun plus(v: Vector) = Point(x + v.dx, y + v.dy)
	operator fun minus(v: Vector) = Point(x - v.dx, y - v.dy)

	companion object {
		fun buildMove(from: Point, to: Point): Point.() -> Point {
			val dx = to.x - from.x
			val dy = to.y - from.y
			fun move(point: Point) = Point(point.x + dx, point.y + dy)
			return ::move
		}
	}

	fun go(dir: Direction) = when (dir) {
		Direction.Up        -> up()
		Direction.Right     -> right()
		Direction.Down      -> down()
		Direction.Left      -> left()
		Direction.UpRight   -> upRight()
		Direction.DownRight -> downRight()
		Direction.DownLeft  -> downLeft()
		Direction.UpLeft    -> upLeft()
	}

}

data class LPoint(val x: Long, val y: Long) {
	fun up() = LPoint(x, y - 1)
	fun down() = LPoint(x, y + 1)
	fun left() = LPoint(x - 1, y)
	fun upLeft() = LPoint(x - 1, y - 1)
	fun downLeft() = LPoint(x - 1, y + 1)
	fun right() = LPoint(x + 1, y)
	fun upRight() = LPoint(x + 1, y - 1)
	fun downRight() = LPoint(x + 1, y + 1)

	fun adjacent() = buildList {
		add(up())
		add(down())
		add(left())
		add(right())
		add(upLeft())
		add(upRight())
		add(downLeft())
		add(downRight())
	}

	fun crossAdjacent() = buildList {
		add(up())
		add(down())
		add(left())
		add(right())
	}

	fun move(dir: Direction) {
		when (dir) {
			Direction.Up        -> up()
			Direction.Right     -> right()
			Direction.Down      -> down()
			Direction.Left      -> left()
			Direction.UpRight   -> upRight()
			Direction.DownRight -> downRight()
			Direction.DownLeft  -> downLeft()
			Direction.UpLeft    -> upLeft()
		}
	}

}

data class HRange(val y: Int, val range: IntRange)

open class ChGrid(src: List<String>) {
	val lines = src.map { it.toCharArray() }
	val xRange = lines[0].indices
	val yRange = lines.indices

	fun getLine(y: Int) = lines[y].joinToString("")

	operator fun get(x: Int, y: Int) = lines.getOrNull(y)?.getOrNull(x)
	operator fun get(p: Point) = get(p.x, p.y)
	operator fun get(p: Pair<Int, Int>) = get(p.first, p.second)

	operator fun set(x: Int, y: Int, ch: Char) = lines[y].set(x, ch)
	operator fun set(p: Point, ch: Char) = set(p.x, p.y, ch)

	fun extract(r: HRange) = getLine(r.y).substring(r.range)

	fun extract(points: List<Point>) = buildList {
		points.forEach {
			val ch = this@ChGrid[it]
			if (ch != null)
				add(it to ch)
		}
	}

	fun asPointsSequence() = sequence {
		yRange.forEach { y ->
			xRange.forEach { x ->
				yield(Point(x, y))
			}
		}
	}

	fun asPointsSequenceAndValue() = sequence {
		yRange.forEach { y ->
			xRange.forEach { x ->
				yield(Point(x, y) to get(x, y)!!)
			}
		}
	}

	fun findFirst(ch: Char) = asPointsSequenceAndValue().firstOrNull { it.second == ch }?.first


	operator fun contains(p: Point) = p.x in xRange && p.y in yRange

	fun deepHashCode() = lines.fold(1) { acc, array -> (31 * acc) + array.contentHashCode() }
	override fun toString() = buildString {
		lines.forEach { appendLine(it) }
	}
}