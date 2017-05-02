package com.github.sybila.huctl.parser

/**
 * Abstraction over assignment expressions in the HUCTLp file.
 *
 * It remembers basic info such as flag and location for future processing.
 */
internal data class Assignment<out T: Any>(
        val name: String,
        val item: T,
        val location: String,
        val flagged: Boolean
)