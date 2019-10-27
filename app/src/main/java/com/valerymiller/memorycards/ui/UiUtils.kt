package com.valerymiller.memorycards.ui

/**
 *  Determine span for items in grid.
 */
fun determineSpan(cardNumber: Int) =
    when(cardNumber) {
        12 -> 3
        else -> 4
    }

/**
 *  Determine spacing for items in grid.
 */
fun determineSpacing(cardNumber: Int) =
    when(cardNumber) {
        12 -> 20
        16 -> 16
        20 -> 12
        else -> 8
    }

/**
 *  Determine delay step for animation in grid in milliseconds.
 */
fun determineDelayStep(cardNumber: Int): Long =
    when(cardNumber) {
        12 -> 30L
        16 -> 25L
        20 -> 20L
        else -> 15L
    }