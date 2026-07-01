package com.strawtechberry.yupana.feature.assignment.ui.common

/**
 * Converts UTC epoch millis (as returned by Material3's `DatePickerState`) to an ISO
 * `yyyy-MM-dd` string, without a date library — this project doesn't depend on
 * kotlinx-datetime and `java.time`/`SimpleDateFormat` aren't available in `commonMain`
 * (iOS target). Uses Howard Hinnant's `civil_from_days` algorithm (pure integer
 * arithmetic, no platform date APIs, well-tested).
 */
fun epochMillisToIsoDate(millis: Long): String {
    val z = floorDiv(millis, 86_400_000L) + 719468
    val era = floorDiv(if (z >= 0) z else z - 146096, 146097)
    val doe = z - era * 146097
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    val y = yoe + era * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val day = (doy - (153 * mp + 2) / 5 + 1).toInt()
    val month = if (mp < 10) (mp + 3).toInt() else (mp - 9).toInt()
    val year = if (month <= 2) y + 1 else y

    return "$year-${month.pad2()}-${day.pad2()}"
}

/** `kotlin.math.floorDiv` isn't available for `Long` in `commonMain`; implemented manually. */
private fun floorDiv(x: Long, y: Long): Long {
    val q = x / y
    return if (x % y != 0L && (x < 0) != (y < 0)) q - 1 else q
}

private fun Int.pad2(): String = if (this < 10) "0$this" else "$this"
