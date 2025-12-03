package y2025.day03

import Day
import y2025.day03.Aoc03.findLongJolts
import y2025.day03.Aoc03.findLongJolts2
import y2025.day03.Aoc03.findSimpleJolts

private object Aoc03: Day() {
	fun String.findMaxIndexFrom(fromIndex: Int, position: Int, size: Int): Int {
		val range = fromIndex .. length - 1 - size + position
		var lastCh = '0'
		var index = -1
		for (i in range) {
			val ch = get(i)
			if (ch > lastCh) {
				lastCh = ch
				index = i
			}
			if (ch == '9')
				break
		}
		return index
	}

	fun String.findSimpleJolts(): Int {
		val firstIndex = findMaxIndexFrom(0, 1, 2)
		val first = get(firstIndex)
		val second = substring(firstIndex + 1).max()
		return "$first$second".toInt()
	}

	fun String.findLongJolts(): Long {
		val i1 = findMaxIndexFrom(0, 1, 12)
		val i2 = findMaxIndexFrom(i1 + 1, 2, 12)
		val i3 = findMaxIndexFrom(i2 + 1, 3, 12)
		val i4 = findMaxIndexFrom(i3 + 1, 4, 12)
		val i5 = findMaxIndexFrom(i4 + 1, 5, 12)
		val i6 = findMaxIndexFrom(i5 + 1, 6, 12)
		val i7 = findMaxIndexFrom(i6 + 1, 7, 12)
		val i8 = findMaxIndexFrom(i7 + 1, 8, 12)
		val i9 = findMaxIndexFrom(i8 + 1, 9, 12)
		val i10 = findMaxIndexFrom(i9 + 1, 10, 12)
		val i11 = findMaxIndexFrom(i10 + 1, 11, 12)
		val i12 = findMaxIndexFrom(i11 + 1, 12, 12)
		return ("" + get(i1) + get(i2) + get(i3) + get(i4) + get(i5) + get(i6) + get(i7) + get(i8) + get(i9) + get(i10) + get(i11) + get(i12)).toLong()

	}

	fun String.findLongJolts2(): Long {
		var lastIndex = -1
		val chars = CharArray(12)

		for (targetValue in 1 .. 12) {
			val index = findMaxIndexFrom(lastIndex + 1, targetValue, 12)
			chars[targetValue-1] = this[index]
			lastIndex = index
		}

		return chars.concatToString().toLong()
	}
}

fun main() {
	Aoc03
		.test(
			"""987654321111111
811111111111119
234234234234278
818181911112111"""
		)
		.initPart1(357, 17435)
		.lines {
			it.sumOf { line ->
				line.findSimpleJolts()
			}
		}
		.initPart2(3121910778619L, 172886048065379L)
		.lines {
			it.sumOf { line ->
				line.findLongJolts()
			}
		}
		.initPart2(3121910778619L, 172886048065379L)
		.lines {
			it.sumOf { line ->
				line.findLongJolts2()
			}
		}
}