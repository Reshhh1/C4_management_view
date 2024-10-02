package com.example.util.enums

/**
 * Collection of error codes
 * @author Reshwan Barhoe
 */
enum class ErrorCode(val code: String) {
    // General
    G_NUMERIC_ID_REQUIRED(code = "G-1"),
    G_UNAUTHORIZED(code = "G-2"),
    G_SESSION_EXPIRED(code = "G-3"),
    G_INVALID_REQUEST_BODY(code = "G-4"),
    G_INVALID_QUERY_CHARACTER(code = "G-5"),

    // User
    U_NOT_FOUND(code = "U-1"),
    U_EMAIL_EXISTS(code = "U-2"),
    U_INVALID_CREDENTIALS(code = "U-101"),
    U_INVALID_LENGTH_FIRST_NAME(code = "U-201"),
    U_INVALID_LENGTH_LAST_NAME(code = "U-202"),
    U_INVALID_LENGTH_PREFIXES(code = "U-203"),
    U_INVALID_EMAIL(code = "U-204"),
    U_EMAIL_IS_EMPTY(code = "U-205"),
    U_INVALID_PASSWORD(code = "U-206"),
    U_INCORRECT_PASSWORD(code = "U-207"),
    U_NEW_PASSWORD_EQUAL_TO_OLD(code = "U-208"),
    U_INVALID_ROLE(code = "U-209"),


    // Teams
    T_NOT_FOUND(code = "T-1"),
    T_NAME_EXISTS(code = "T-2"),

    T_INVALID_NAME_LENGTH(code = "T-101"),
    T_INVALID_NAME_CHARACTER(code = "T-102"),
    T_INVALID_STARTING_CHARACTER(code = "T-103"),
    T_EMPTY_NAME(code = "T-104"),

    T_MEMBER_NOT_FOUND(code = "T-201"),
    T_INVALID_MEMBER_AMOUNT(code = "T-202"),
    T_DUPLICATE_MEMBER_IDS(code = "T-203"),
    T_MEMBER_IS_EMPTY(code = "T-204"),

    T_AVATAR_NOT_FOUND(code = "T-301"),
    T_INVALID_MIME_TYPE(code = "T-302"),
    T_INVALID_SIZE(code = "T-303"),
    T_INVALID_EXTENSION(code = "T-304"),
    T_INVALID_DIMENSIONS(code = "T-305"),

    T_CREATOR_IS_EMPTY(code = "T-10000"),
    T_CREATOR_NOT_FOUND(code = "T-10001"),

    //C4 model
    C1_NOT_FOUND(code = "C1-1"),
    C2_NOT_FOUND(code = "C2-1"),
    C3_NOT_FOUND(code = "C3-1")
}



