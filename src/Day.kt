import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.reflect.KFunction1
import kotlin.system.exitProcess
import kotlin.time.measureTimedValue

open class Day {
	private val srcPath: String
	private val client = HttpClient(CIO)
	open var testInput = ""
	open val benchmarkRepetition: Int = 1
	private val realInput: String
	private val day: Int

	init {
		val e = Exception()
		val s = e.stackTrace.first { it.className.startsWith("y") }
		val path = s.className.split(".").dropLast(1).joinToString("/")
		day = path.takeLast(2).toInt()
		srcPath = "src/$path/"
		loadInputFile()
		realInput = localFile("input.txt").readText()
	}

	fun localFile(path: String) = File("$srcPath$path")
	fun test(txt: String) = this.also { testInput = txt }

	private fun loadInputFile() {
		val input = localFile("input.txt")
		if (!input.exists())
			runBlocking {
				try {
					val sessionToken = File("$srcPath../../session").readText().trim()
					val response: HttpResponse = client.get("https://adventofcode.com/2025/day/$day/input") {
						header(HttpHeaders.Cookie, "session=$sessionToken")
					}
					input.writeText(response.bodyAsText().trim())

//					response.bodyAsChannel().copyAndClose(input.writeChannel())
				} finally {
					client.close()
				}
			}
	}

	fun <T> initPart1(resTest: T?, res: T? = null) = Part(1, resTest, res)
	fun <T> initPart2(resTest: T?, res: T? = null) = Part(2, resTest, res)

	inner class Part<T>(val part: Int, val resTest: T?, val res: T? = null) {
		private val openRed = "\u001B[31m"
		private val openGreen = "\u001B[32m"
		private val openYellow = "\u001B[33m"
		private val normalColor = "\u001B[0m"

		fun lines(label: String = "", block: (List<String>) -> T): Day {
			part(label, testInput.lines(), realInput.lines(), block)
			return this@Day
		}

		fun lines(function: KFunction1<List<String>, T>): Day {
			part(function.name, testInput.lines(), realInput.lines(), function)
			return this@Day
		}

		fun text(label: String = "", block: (String) -> T): Day {
			part(label, testInput, realInput, block)
			return this@Day
		}

		private fun <SRC> part(label: String, src1: SRC, src2: SRC, block: (SRC) -> T) {
			println("[$label]:")
			if (resTest != null) {
				val timedResult = measureTimedValue { block(src1) }
				println("${openGreen}Test Part $part$normalColor = ${timedResult.value}")
				if (timedResult.value != resTest) {
					println("$openRed              ERROR:$normalColor $resTest expected!")
					exitProcess(1)
				}
			}
			if (res != null) {
				var timedResult = measureTimedValue { block(src2) } // discarded
				var extraInfo = ""
				if (benchmarkRepetition > 1) {
					extraInfo = " out of $benchmarkRepetition"
					timedResult = measureTimedValue {
						repeat(benchmarkRepetition - 1) { block(src2) }
						block(src2)
					}
				}

				println("$openGreen     Part $part$normalColor = ${timedResult.value} in ${timedResult.duration.div(benchmarkRepetition)}$extraInfo")
				if (res != timedResult.value) {
					println("$openRed              ERROR:$normalColor $res expected!")
					exitProcess(1)
				}

			}
		}

	}
}