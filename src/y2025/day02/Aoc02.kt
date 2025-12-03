package y2025.day02

import Day
import y2025.day02.Aoc02.Item

private object Aoc02: Day() {


	class Item(first: String, second: String) {
		val first = first.toLong()
		val second = second.toLong()

		fun process1(): Long = (first .. second)
			.asSequence()
			.mapNotNull {
				val n = it.toString()
				if (n.length % 2 == 1)
					null
				else {
					val h = n.length / 2
					val x1 = n.take(h)
					val x2 = n.takeLast(h)
					if (x1 == x2)
						it
					else
						null
				}
			}
			.sum()

		fun process2(): Long = (first .. second)
			.asSequence()
			.mapNotNull {
				val n = it.toString()
				if (n.isRepeatedPattern())
					it
				else
					null
			}
			.sum()

		/**
		 * Se una stringa è composta da pattern ripetuti, quando viene raddoppiata il pattern originale riapparirà prima del punto medio.
		 * Ad esempio, con "abcabc", doubled = "abcabcabcabc" e cercando "abcabc" dall'indice 1 la trovi all'indice 3, che è minore di 6.
		 */
		fun String.isRepeatedPattern(): Boolean {
			if (length < 2) return false
			val doubled = this + this
			return doubled.indexOf(this, 1) < length
		}

		val rex = """(\d+)\1+""".toRegex()

		fun process3(): Long = (first .. second)
			.asSequence()
			.mapNotNull {
				val n = it.toString()
				if (rex.matches(n))
					it
				else
					null
			}
			.sum()

	}
}

fun main() {
	Aoc02
		.test("""11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124""")
		.initPart1(1227775554L, 19219508902L)
		.text { fullLine ->
			fullLine
				.splitToSequence(",")
				.map { item -> item.split("-").let { Item(it[0], it[1]) } }
				.sumOf { it.process1() }
		}
		.initPart2(4174379265L, 27180728081L)
		.text("doubled") { fullLine ->
			fullLine
				.splitToSequence(",")
				.map { item -> item.split("-").let { Item(it[0], it[1]) } }
				.sumOf { it.process2() }
		}
		.initPart2(4174379265L, 27180728081L)
		.text("regex") { fullLine ->
			fullLine
				.splitToSequence(",")
				.map { item -> item.split("-").let { Item(it[0], it[1]) } }
				.sumOf { it.process3() }
		}
}