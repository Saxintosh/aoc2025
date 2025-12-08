package y2025.day01

import Day

private object Aoc01: Day() {
	override val benchmarkRepetition = 100

	fun part1(lines: List<String>): Int {
		var dial = 50
		var zeroCount = 0

		lines
			.map { it[0] to it.substring(1).toInt() }
			.forEach { (dir, amount) ->
				val netAmount = amount % 100
				dial += when (dir) {
					'L'  -> (100 - netAmount)
					'R'  -> netAmount
					else -> throw Exception()
				}
				dial %= 100
				if (dial == 0) zeroCount++
			}
		return zeroCount
	}

	fun part1b(lines: List<String>): Int {
		var dial = 50
		return lines
			.count {
				val dir = it[0]
				val amount = it.substring(1).toInt()
				dial = when (dir) {
					'L'  -> (dial - amount).mod(100)
					else -> (dial + amount).mod(100)
				}
				dial == 0
			}
	}

	fun part1c(lines: List<String>): Int {
		val intermediate = lines
			.runningFold(50) { dial, item ->
				when (item[0]) {
					'L'  -> (dial - item.drop(1).toInt()).mod(100)
					'R'  -> (dial + item.drop(1).toInt()).mod(100)
					else -> throw Exception()
				}
			}
		return intermediate.count { it == 0 }
	}

	fun part2(lines: List<String>): Int {
		var dial = 50
		var zeroCount = 0

		lines
			.map { it[0] to it.substring(1).toInt() }
			.forEach { (dir, amount) ->
				zeroCount += (amount / 100)
				val netAmount = amount % 100
				val originalDial = dial
				when (dir) {
					'L'  -> dial -= netAmount
					'R'  -> dial += netAmount
					else -> throw Exception()
				}
				when {
					dial < 0  -> {
						if (originalDial != 0)
							zeroCount++
						dial += 100
					}

					dial > 99 -> {
						dial -= 100
						zeroCount++
					}

					dial == 0 -> {
						if (originalDial != 0)
							zeroCount++
					}
				}
			}
		return zeroCount
	}

	fun part2b(lines: List<String>): Int {
		var dial = 50
		var zeroCount = 0

		lines
			.forEach { line ->
				val dir = line[0]
				val amount = line.substring(1).toInt()
				zeroCount += (amount / 100)
				val netAmount = amount.mod(100)
				val originalDial = dial
				when (dir) {
					'L'  -> dial -= netAmount
					else -> dial += netAmount
				}
				if (dial < 0) {
					if (originalDial != 0)
						zeroCount++
					dial += 100
				} else if (dial > 99) {
					dial -= 100
					zeroCount++
				} else if (dial == 0 && originalDial != 0) {
					zeroCount++
				}
			}
		return zeroCount
	}
}


fun main() {
	Aoc01.test(
		"""L68
L30
R48
L5
R60
L55
L1
L99
R14
L82"""
	)

	Aoc01.initPart1(3, 1129)
		.inputAsLines(Aoc01::part1)

	Aoc01.initPart1(3, 1129)
		.inputAsLines(Aoc01::part1b)

	Aoc01.initPart1(3, 1129)
		.inputAsLines(Aoc01::part1c)

	Aoc01.initPart2(6, 6638)
		.inputAsLines(Aoc01::part2)

	Aoc01.initPart2(6, 6638)
		.inputAsLines(Aoc01::part2b)
}