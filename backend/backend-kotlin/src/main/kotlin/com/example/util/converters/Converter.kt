package com.example.util.converters

import org.jetbrains.exposed.sql.ResultRow

/**
 * Converter interface
 * @author Reshwan Barhoe
 */
interface Converter<Business> {
    fun convertToBusinessModel(row: ResultRow): Business
}