package com.example.medicinereminder.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

object AccessibilityUtils {

    fun Modifier.accessibleDescription(description:String):Modifier{
        return this.semantics {
            contentDescription = description
        }
    }
}