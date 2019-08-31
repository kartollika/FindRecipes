package kartollika.recipesbook.common.utils

import androidx.room.TypeConverter
import java.util.*


class TimeUtils {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    fun minutesToHoursAndMinutes(minutes: Int): String {
        if (minutes < 60) {
            return minutes.toString()
        }

        val hours = minutes / 60
        return "$hours hours ${minutes % 60} minutes"
    }
}