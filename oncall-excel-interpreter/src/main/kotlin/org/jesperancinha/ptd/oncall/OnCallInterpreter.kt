package org.jesperancinha.ptd.oncall

import biweekly.Biweekly
import biweekly.component.VEvent
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.*

data class TimeInterval(
    val start: LocalTime,
    val end: LocalTime,
    val isNextDay: Boolean = false
)

data class OnCallHeader(
    val columnIndex: Int,
    val label: String,
    val days: List<DayOfWeek>? = null,
    val intervals: List<TimeInterval> = emptyList(),
    val isSunday: Boolean = false,
    val isHoliday: Boolean = false,
    val isSaturday: Boolean = false
)

class OnCallInterpreter(
    private val icsFile: File,
    private val xltFile: File,
    private val year: Int,
    private val month: Month
) {
    private val dutchHolidays = getDutchHolidays(year)

    private val amsterdamZone = ZoneId.of("Europe/Amsterdam")

    fun interpret(outputFile: File) {
        val events = parseIcs()
        val workbook = HSSFWorkbook(FileInputStream(xltFile))
        val sheet = workbook.getSheetAt(0)

        val headers = parseHeaders(sheet.getRow(0))

        val daysInMonth = month.length(Year.isLeap(year.toLong()))
        for (day in 1..daysInMonth) {
            val date = LocalDate.of(year, month, day)
            val row = sheet.getRow(day) ?: sheet.createRow(day)
            
            val dateCell = row.getCell(0) ?: row.createCell(0)
            dateCell.setCellValue(day.toDouble())
            
            headers.forEach { header ->
                val hours = calculateHoursForHeader(date, header, events)
                if (hours > 0) {
                    val cell = row.getCell(header.columnIndex) ?: row.createCell(header.columnIndex)
                    cell.setCellValue(hours)
                }
            }
        }

        FileOutputStream(outputFile).use { fos ->
            workbook.write(fos)
        }
        workbook.close()
    }

    private fun parseIcs(): List<VEvent> {
        return Biweekly.parse(icsFile).all().flatMap { it.getEvents() }
    }

    private fun parseHeaders(row: org.apache.poi.ss.usermodel.Row): List<OnCallHeader> {
        val headers = mutableListOf<OnCallHeader>()
        for (colIndex in 1 until row.lastCellNum) {
            val cell = row.getCell(colIndex) ?: continue
            val label = cell.stringCellValue
            headers.add(parseHeaderLabel(colIndex, label))
        }
        return headers
    }

    private fun parseHeaderLabel(columnIndex: Int, label: String): OnCallHeader {
        return when {
            label.contains("Feestdag", ignoreCase = true) -> 
                OnCallHeader(columnIndex, label, isHoliday = true)
            label.contains("Zondag", ignoreCase = true) -> 
                OnCallHeader(columnIndex, label, isSunday = true)
            label.contains("Zaterdag", ignoreCase = true) && label.contains("24h", ignoreCase = true) ->
                 OnCallHeader(columnIndex, label, isSaturday = true)
            label.contains("Zaterdag", ignoreCase = true) -> {
                val intervals = parseIntervals(label)
                OnCallHeader(columnIndex, label, isSaturday = true, intervals = intervals)
            }
            label.contains("Ma - Za", ignoreCase = true) -> {
                val intervals = parseIntervals(label)
                OnCallHeader(columnIndex, label, days = (DayOfWeek.MONDAY..DayOfWeek.SATURDAY).toList(), intervals = intervals)
            }
            else -> OnCallHeader(columnIndex, label)
        }
    }

    private fun parseIntervals(label: String): List<TimeInterval> {
        val timeRegex = "(\\d{1,2}:\\d{2})\\s*-\\s*(\\d{1,2}:\\d{2})".toRegex()
        return timeRegex.findAll(label).map { match ->
            val startStr = match.groupValues[1].let { if (it.length == 4) "0$it" else it }
            val endStr = match.groupValues[2].let { if (it.length == 4) "0$it" else it }
            val start = LocalTime.parse(startStr)
            val end = LocalTime.parse(endStr)
            TimeInterval(start, end, end <= start && end != LocalTime.MIDNIGHT)
        }.toList()
    }

    private fun calculateHoursForHeader(date: LocalDate, header: OnCallHeader, events: List<VEvent>): Double {
        val isHoliday = dutchHolidays.contains(date)
        val isSunday = date.dayOfWeek == DayOfWeek.SUNDAY
        val isSaturday = date.dayOfWeek == DayOfWeek.SATURDAY

        if (header.isHoliday && !isHoliday) return 0.0
        if (header.isHoliday && isHoliday) return getOnCallHoursForDay(date, events, LocalTime.MIN, LocalTime.MAX)

        if (header.isSunday && !isSunday) return 0.0
        if (header.isSunday && isSunday && !isHoliday) return getOnCallHoursForDay(date, events, LocalTime.MIN, LocalTime.MAX)

        if (header.isSaturday && !isSaturday) return 0.0
        if (header.isSaturday && isSaturday && !isHoliday) {
             if (header.intervals.isEmpty()) return getOnCallHoursForDay(date, events, LocalTime.MIN, LocalTime.MAX)
             // else fall through to interval check
        }

        if (header.days != null && !header.days.contains(date.dayOfWeek)) return 0.0
        if (isHoliday || isSunday) return 0.0 // Ma-Za headers don't apply on holidays or Sundays

        var totalHours = 0.0
        header.intervals.forEach { interval ->
            totalHours += getOnCallHoursForDay(date, events, interval.start, if (interval.isNextDay) LocalTime.MAX else interval.end)
        }
        
        return totalHours
    }

    private fun getOnCallHoursForDay(date: LocalDate, events: List<VEvent>, headerStart: LocalTime, headerEnd: LocalTime): Double {
        val dayStart = ZonedDateTime.of(date, headerStart, amsterdamZone)
        val dayEnd = if (headerEnd == LocalTime.MIDNIGHT || (headerEnd == LocalTime.MAX)) 
            ZonedDateTime.of(date.plusDays(1), LocalTime.MIDNIGHT, amsterdamZone)
        else 
            ZonedDateTime.of(date, headerEnd, amsterdamZone)

        var totalDuration = Duration.ZERO
        events.forEach { event ->
            val eventStart = ZonedDateTime.ofInstant(event.dateStart.value.toInstant(), ZoneId.of("UTC"))
            val eventEnd = ZonedDateTime.ofInstant(event.dateEnd.value.toInstant(), ZoneId.of("UTC"))

            val intersectStart = if (eventStart.isAfter(dayStart)) eventStart else dayStart
            val intersectEnd = if (eventEnd.isBefore(dayEnd)) eventEnd else dayEnd

            if (intersectStart.isBefore(intersectEnd)) {
                totalDuration = totalDuration.plus(Duration.between(intersectStart, intersectEnd))
            }
        }
        return totalDuration.toMinutes() / 60.0
    }

    private fun getDutchHolidays(year: Int): Set<LocalDate> {
        val holidays = mutableSetOf<LocalDate>()
        holidays.add(LocalDate.of(year, 1, 1)) // New Year
        holidays.add(LocalDate.of(year, 4, 27)) // King's Day
        holidays.add(LocalDate.of(year, 5, 5)) // Liberation Day (every 5 years usually, but often listed)
        holidays.add(LocalDate.of(year, 12, 25)) // Christmas
        holidays.add(LocalDate.of(year, 12, 26)) // Second Christmas

        // Easter, Ascension, Pentecost
        val easter = getEaster(year)
        holidays.add(easter) // Easter Sunday (already covered by Sunday header usually, but good to have)
        holidays.add(easter.plusDays(1)) // Easter Monday
        holidays.add(easter.plusDays(39)) // Ascension Day
        holidays.add(easter.plusDays(49)) // Pentecost Sunday
        holidays.add(easter.plusDays(50)) // Pentecost Monday
        
        return holidays
    }

    private fun getEaster(year: Int): LocalDate {
        val a = year % 19
        val b = year / 100
        val c = year % 100
        val d = b / 4
        val e = b % 4
        val f = (b + 8) / 25
        val g = (b - f + 1) / 3
        val h = (19 * a + b - d - g + 15) % 30
        val i = c / 4
        val k = c % 4
        val l = (32 + 2 * e + 2 * i - h - k) % 7
        val m = (a + 11 * h + 22 * l) / 451
        val month = (h + l - 7 * m + 114) / 31
        val day = ((h + l - 7 * m + 114) % 31) + 1
        return LocalDate.of(year, month, day)
    }
}

operator fun DayOfWeek.rangeTo(other: DayOfWeek): List<DayOfWeek> {
    val list = mutableListOf<DayOfWeek>()
    var curr = this
    while (curr != other) {
        list.add(curr)
        curr = DayOfWeek.of(curr.value % 7 + 1)
    }
    list.add(other)
    return list
}
