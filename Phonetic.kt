package com.example.voicewriter

/** Avro-style phonetic Bangla: "ami" -> আমি */
object Phonetic {
    private val cons = mapOf(
        "kkh" to "ক্ষ", "gg" to "জ্ঞ",
        "kh" to "খ", "k" to "ক", "gh" to "ঘ", "g" to "গ", "Ng" to "ঙ",
        "chh" to "ছ", "ch" to "চ", "c" to "চ", "jh" to "ঝ", "j" to "জ", "NG" to "ঞ",
        "Th" to "ঠ", "T" to "ট", "Dh" to "ঢ", "D" to "ড", "N" to "ণ",
        "th" to "থ", "t" to "ত", "dh" to "ধ", "d" to "দ", "n" to "ন",
        "ph" to "ফ", "p" to "প", "f" to "ফ", "bh" to "ভ", "b" to "ব", "v" to "ভ", "m" to "ম",
        "z" to "য", "r" to "র", "l" to "ল", "sh" to "শ", "Sh" to "ষ", "S" to "শ", "s" to "স", "h" to "হ",
        "Rh" to "ঢ়", "R" to "ড়", "y" to "য়", "Y" to "য়", "q" to "ক", "x" to "ক্স"
    )
    private val mods = mapOf("ng" to "ং")
    // key -> (independent vowel, kar sign)
    private val vows = mapOf(
        "rri" to ("ঋ" to "ৃ"),
        "oi" to ("ঐ" to "ৈ"), "OI" to ("ঐ" to "ৈ"),
        "ou" to ("ঔ" to "ৌ"), "OU" to ("ঔ" to "ৌ"),
        "aa" to ("আ" to "া"), "a" to ("আ" to "া"),
        "ii" to ("ঈ" to "ী"), "I" to ("ঈ" to "ী"), "i" to ("ই" to "ি"),
        "uu" to ("ঊ" to "ূ"), "U" to ("ঊ" to "ূ"), "u" to ("উ" to "ু"),
        "e" to ("এ" to "ে"), "o" to ("অ" to ""), "O" to ("ও" to "ো"),
        "A" to ("অ্যা" to "্যা")
    )

    fun convert(s: String): String {
        val out = StringBuilder()
        var prevCons = false
        var i = 0
        while (i < s.length) {
            var done = false
            var len = minOf(3, s.length - i)
            while (len >= 1 && !done) {
                val tok = s.substring(i, i + len)
                val c = cons[tok]
                val m = mods[tok]
                val v = vows[tok]
                if (c != null) {
                    if (prevCons) out.append('্')
                    out.append(if (prevCons && tok == "y") "য" else c)
                    prevCons = true
                    done = true
                } else if (m != null) {
                    out.append(m)
                    prevCons = false
                    done = true
                } else if (v != null) {
                    out.append(if (prevCons) v.second else v.first)
                    prevCons = false
                    done = true
                }
                if (done) i += len else len--
            }
            if (!done) {
                out.append(s[i])
                prevCons = false
                i++
            }
        }
        return out.toString()
    }
}
