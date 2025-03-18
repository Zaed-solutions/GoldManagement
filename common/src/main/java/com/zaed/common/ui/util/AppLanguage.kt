package com.zaed.common.ui.util

import com.zaed.common.R

enum class AppLanguage(val titleRes: Int, val code: String) {
    ENGLISH(R.string.english, "en"),
    ARABIC(R.string.arabic, "ar"),
    FRENCH(R.string.french, "fr");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return values().firstOrNull { it.code == code } ?: ENGLISH
        }
    }
}