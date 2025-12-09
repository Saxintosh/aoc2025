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
		fun fromLine(line: String, separator: String = ","): Point {
			val (x, y) = line.split(separator)
			return Point(x.trim().toInt(), y.trim().toInt())
		}

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

fun xInclusiveRange(a: Point, b: Point): IntRange {
	return if (a.x <= b.x)
		a.x .. b.x
	else
		b.x .. a.x
}

fun yInclusiveRange(a: Point, b: Point): IntRange {
	return if (a.y <= b.y)
		a.y .. b.y
	else
		b.y .. a.y
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

/**
 * Trova tutti i punti interi (x, y) all'interno e sul bordo di un poligono
 * con lati solo orizzontali o verticali (assi-allineato).
 *
 * @param vertices La lista dei vertici consecutivi del poligono.
 * @return Una Set (per punti unici) di tutti i Point interni e sul bordo.
 */
fun findPolygonPoints(vertices: List<Point>): Set<Point> {
	if (vertices.size < 3) return emptySet()

	val allPoints = mutableSetOf<Point>()

	// 1. TROVARE I PUNTI SUL BORDO (Edges)
	// Connetti P1 a P2, P2 a P3, ..., Pn, P1
	for (i in vertices.indices) {
		val p1 = vertices[i]
		val p2 = vertices.getOrNull(i + 1) ?: vertices[0] // Connessione circolare

		// Determina il segmento (orizzontale o verticale)
		if (p1.x == p2.x) { // Segmento verticale
			val startY = minOf(p1.y, p2.y)
			val endY = maxOf(p1.y, p2.y)
			for (y in startY .. endY) {
				allPoints.add(Point(p1.x, y))
			}
		} else if (p1.y == p2.y) { // Segmento orizzontale
			val startX = minOf(p1.x, p2.x)
			val endX = maxOf(p1.x, p2.x)
			for (x in startX .. endX) {
				allPoints.add(Point(x, p1.y))
			}
		}
		// Nota: Assumiamo che il poligono sia assi-allineato, quindi o p1.x==p2.x O p1.y==p2.y
	}

	// 2. TROVARE I PUNTI INTERNI (Interior)

	// Trova la Bounding Box (rettangolo minimo che contiene il poligono)
	val minX = vertices.minOf { it.x }
	val maxX = vertices.maxOf { it.x }
	val minY = vertices.minOf { it.y }
	val maxY = vertices.maxOf { it.y }

	// Controlla ogni punto intero (x, y) all'interno della Bounding Box
	for (x in minX .. maxX) {
		for (y in minY .. maxY) {
			val currentPoint = Point(x, y)

			// Se il punto è già stato aggiunto come punto del bordo, continua
			if (allPoints.contains(currentPoint)) continue

			// Applica il Test del Ray Casting (Raggio)
			// Lancia un raggio orizzontale (a destra) da (x, y) e conta le intersezioni
			// con i bordi verticali del poligono.
			var intersectionCount = 0

			for (i in vertices.indices) {
				val p1 = vertices[i]
				val p2 = vertices[(i + 1) % vertices.size]

				// Consideriamo solo i lati verticali per semplificare il conteggio delle intersezioni orizzontali
				if (p1.x == p2.x) { // Segmento verticale
					val edgeX = p1.x
					val startY = minOf(p1.y, p2.y)
					val endY = maxOf(p1.y, p2.y)

					// Un punto (x, y) è INTERNO se:
					// 1. Il raggio da (x, y) attraversa il segmento verticale (edgeX).
					// 2. L'intersezione avviene strettamente a destra del punto (x < edgeX).
					// 3. La coordinata y del punto è compresa tra le y del segmento (startY < y <= endY).
					//    Usiamo (y <= endY) per gestire i vertici correttamente.

					if (x < edgeX && y in startY .. endY) {
						// Trattiamo i casi in cui il punto giace su un vertice orizzontale,
						// ma abbiamo già gestito tutti i punti del bordo sopra.

						// Incrementa se l'intersezione non è su un vertice,
						// o se lo è, ma il vertice è quello superiore (per evitare doppi conteggi).
						if (y != startY) { // Se y coincide con il vertice inferiore, salta per evitare doppi conteggi
							intersectionCount++
						}
					}
				}
			}

			// Se il conteggio delle intersezioni è DISPARI, il punto è INTERNO
			if (intersectionCount % 2 != 0) {
				allPoints.add(currentPoint)
			}
		}
	}

	return allPoints
}