package com.st10361554.prog7314_ice_task_4.news_utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val ISO_DATE_FORMAT = "yyyy-MM-dd"
    private const val ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    /**
     * Format date for API request (ISO 8601 format)
     * @param date Date to format
     * @param includeTime Whether to include time in the format
     * @return Formatted date string
     */
    fun formatForApi(date: Date, includeTime: Boolean = false): String {
        val format = if (includeTime) ISO_DATETIME_FORMAT else ISO_DATE_FORMAT
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * Get date string for today
     */
    fun getTodayDate(): String {
        return formatForApi(Date(), false)
    }

    /**
     * Get date string for days ago
     * @param daysAgo Number of days ago
     */
    fun getDaysAgo(daysAgo: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return formatForApi(calendar.time, false)
    }

    /**
     * Get date string for a week ago
     */
    fun getWeekAgo(): String = getDaysAgo(7)

    /**
     * Get date string for a month ago
     */
    fun getMonthAgo(): String = getDaysAgo(30)
}