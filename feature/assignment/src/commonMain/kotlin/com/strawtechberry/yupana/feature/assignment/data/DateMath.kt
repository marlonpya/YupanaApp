package com.strawtechberry.yupana.feature.assignment.data

/**
 * Adds one calendar month to an ISO `yyyy-MM-dd` date, clamping the day to the target
 * month's length (e.g. 2026-01-31 -> 2026-02-28). No date library involved (same
 * constraint as `ui/common/DateFormatting.kt`): `java.time` isn't available in
 * `commonMain` (iOS target) and this project doesn't depend on kotlinx-datetime.
 */
internal fun addOneMonthIso(iso: String): String {
    val parts = iso.split("-")
    val year = parts[0].toInt()
    val month = parts[1].toInt()
    val day = parts[2].toInt()

    val nextMonth = if (month == 12) 1 else month + 1
    val nextYear = if (month == 12) year + 1 else year
    val clampedDay = minOf(day, daysInMonth(nextYear, nextMonth))

    return "$nextYear-${nextMonth.pad2()}-${clampedDay.pad2()}"
}

private fun daysInMonth(year: Int, month: Int): Int {
    val lengths = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    return if (month == 2 && isLeapYear(year)) 29 else lengths[month - 1]
}

private fun isLeapYear(year: Int): Boolean = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0

private fun Int.pad2(): String = if (this < 10) "0$this" else "$this"
