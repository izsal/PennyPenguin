package com.example.pennypenguin.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class IDRVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = CurrencyUtil.formatThousands(originalText)
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val transformationSuffix = formattedText.substring(0, minOf(offset + (formattedText.length - originalText.length), formattedText.length))
                // This is a simplified offset mapping for thousands separators
                // In a production app, we'd count the dots added before the offset
                var dotsCount = 0
                var originalCount = 0
                for (char in formattedText) {
                    if (char == '.') {
                        dotsCount++
                    } else {
                        originalCount++
                    }
                    if (originalCount == offset) break
                }
                return offset + dotsCount
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val transformationSuffix = formattedText.substring(0, minOf(offset, formattedText.length))
                val dotsCount = transformationSuffix.count { it == '.' }
                return offset - dotsCount
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
