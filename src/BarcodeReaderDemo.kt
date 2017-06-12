import java.util.Date
import java.io.*

import com.dynamsoft.barcode.Barcode
import com.dynamsoft.barcode.BarcodeReader
import com.dynamsoft.barcode.ReadResult

object BarcodeReaderDemo {

    private fun GetFormat(iIndex: Int): Long {
        var lFormat: Long = 0

        when (iIndex) {
            1 -> lFormat = Barcode.OneD or Barcode.QR_CODE or Barcode.PDF417 or Barcode.DATAMATRIX
            2 -> lFormat = Barcode.OneD
            3 -> lFormat = Barcode.QR_CODE
            4 -> lFormat = Barcode.CODE_39
            5 -> lFormat = Barcode.CODE_128
            6 -> lFormat = Barcode.CODE_93
            7 -> lFormat = Barcode.CODABAR
            8 -> lFormat = Barcode.ITF
            9 -> lFormat = Barcode.INDUSTRIAL_25
            10 -> lFormat = Barcode.EAN_13
            11 -> lFormat = Barcode.EAN_8
            12 -> lFormat = Barcode.UPC_A
            13 -> lFormat = Barcode.UPC_E
            14 -> lFormat = Barcode.PDF417
            15 -> lFormat = Barcode.DATAMATRIX
            else -> {
            }
        }

        return lFormat
    }

    @Throws(Exception::class)
    @JvmStatic fun main(args: Array<String>) {

        var iMaxCount = 0
        var lFormat: Long = -1
        var iIndex = 0
        var pszImageFile: String? = null
        var strLine: String?
        var bExitFlag = false

        println("*************************************************")
        println("Welcome to Dynamsoft Barcode Reader Demo")
        println("*************************************************")
        println("Hints: Please input 'Q'or 'q' to quit the application.")

        val cin = BufferedReader(java.io.InputStreamReader(
                System.`in`))

        while (true) {
            iMaxCount = 0x7FFFFFFF
            lFormat = Barcode.OneD or Barcode.QR_CODE or Barcode.PDF417 or Barcode.DATAMATRIX

            while (true) {
                println()
                println(">> Step 1: Input your image file's full path:")
                strLine = cin.readLine()
                if (strLine != null && strLine.trim { it <= ' ' }.length > 0) {
                    strLine = strLine.trim { it <= ' ' }
                    if (strLine.equals("q", ignoreCase = true)) {
                        bExitFlag = true
                        break
                    }

                    if (strLine.length >= 2 && strLine[0] == '\"'
                            && strLine[strLine.length - 1] == '\"') {
                        pszImageFile = strLine.substring(1, strLine.length - 1)
                    } else {
                        pszImageFile = strLine
                    }

                    val file = java.io.File(pszImageFile)
                    if (file.exists() && file.isFile)
                        break
                }

                println("Please input a valid path.")
            }

            if (bExitFlag)
                break

            while (true) {
                println()
                println(">> Step 2: Choose a number for the format(s) of your barcode image:")
                println("   1: All")
                println("   2: OneD")
                println("   3: QR Code")
                println("   4: Code 39")
                println("   5: Code 128")
                println("   6: Code 93")
                println("   7: Codabar")
                println("   8: Interleaved 2 of 5")
                println("   9: Industrial 2 of 5")
                println("   10: EAN-13")
                println("   11: EAN-8")
                println("   12: UPC-A")
                println("   13: UPC-E")
                println("   14: PDF417")
                println("   15: DATAMATRIX")

                strLine = cin.readLine()
                if (strLine!!.length > 0) {
                    try {
                        iIndex = Integer.parseInt(strLine)
                        lFormat = GetFormat(iIndex)
                        if (lFormat != 0L)
                            break
                    } catch (exp: Exception) {
                    }

                }

                println("Please choose a valid number. ")

            }

            while (true) {
                println()
                println(">> Step 3: Input the maximum number of barcodes to read per page: ")

                strLine = cin.readLine()
                if (strLine!!.length > 0) {
                    try {
                        iMaxCount = Integer.parseInt(strLine)
                        if (iMaxCount > 0)
                            break
                    } catch (exp: Exception) {
                    }

                }

                println("Please re-input the correct number again.")
            }

            println()
            println("Barcode Results:")
            println("----------------------------------------------------------")

            // Set license
            val br = BarcodeReader("t0068MgAAAGxNV3AJYFj7hdkzlWpF8Lf4u8xtqFeOATCGwOPh/IaUPQw7618VDYZzn9gC0kjSsNPDKwstHrpoecoH8ae4BnA=")

            // Read barcode
            val ullTimeBegin = Date().time
            val result = br.readFile(pszImageFile!!, lFormat, iMaxCount)
            val ullTimeEnd = Date().time

            if (result.errorCode != BarcodeReader.DBR_OK && result.errorCode != BarcodeReader.DBRERR_LICENSE_EXPIRED && result.errorCode != BarcodeReader.DBRERR_LICENSE_INVALID) {
                println(result.errorString)
                continue
            }

            // Output barcode result
            /*
			 * Total barcode(s) found: 2. Total time spent: 0.218 seconds.
			 *
			 * Barcode 1: Page: 1 Type: CODE_128 Value: Zt=-mL-94 Region: {Left:
			 * 100, Top: 20, Width: 100, Height: 40}
			 *
			 * Barcode 2: Page: 1 Type: CODE_39 Value: Dynamsoft Region: {Left:
			 * 100, Top: 200, Width: 180, Height: 30}
			 */

            var pszTemp: String

            if (result.barcodes == null || result.barcodes.size == 0) {
                pszTemp = String.format(
                        "No barcode found. Total time spent: %.3f seconds.", (ullTimeEnd - ullTimeBegin).toFloat() / 1000)
                println(pszTemp)
            } else {
                pszTemp = String.format("Total barcode(s) found: %d. Total time spent: %.3f seconds.", result.barcodes.size, (ullTimeEnd - ullTimeBegin).toFloat() / 1000)
                println(pszTemp)

                iIndex = 0
                while (iIndex < result.barcodes.size) {
                    val barcode = result.barcodes[iIndex]

                    pszTemp = String.format("  Barcode %d:", iIndex + 1)
                    println(pszTemp)
                    pszTemp = String.format("    Page: %d", barcode.pageNumber)
                    println(pszTemp)
                    pszTemp = String.format("    Type: %s", barcode.formatString)
                    println(pszTemp)
                    pszTemp = "    Value: " + barcode.displayValue
                    println(pszTemp)


                    pszTemp = String.format("    Region: {Left: %d, Top: %d, Width: %d, Height: %d}",
                            barcode.boundingBox.x, barcode.boundingBox.y, barcode.boundingBox.width, barcode.boundingBox.height)

                    println(pszTemp)
                    println()
                    iIndex++
                }
            }
        }
    }
}
