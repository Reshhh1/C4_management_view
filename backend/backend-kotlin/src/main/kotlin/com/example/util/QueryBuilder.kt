package com.example.util

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.lowerCase

class QueryBuilder {
    companion object {

        /**
         * Builds a case-insensitive condition that checks if the
         * given value exists in the front part of the column data
         * @author Reshwan Barhoe
         * @param column that's being checked
         * @param value that's being checked
         * @return a sql operator
         */
        fun buildCiStartLikeCondition(column: Column<String>, value: String?): Op<Boolean> {
            return (column.lowerCase() like "${value?.lowercase()}%")
        }

        /**
         * Builds a case-insensitive condition that checks if the exact
         * given value exists in the column data
         * @author Reshwan Barhoe
         * @param column that's being checked
         * @param value that's being checked
         * @return a sql operator
         */
        fun buildCiExactLikeCondition(column: Column<String>, value: String?): Op<Boolean> {
            return (column.lowerCase() like "${value?.lowercase()}")
        }
    }
}