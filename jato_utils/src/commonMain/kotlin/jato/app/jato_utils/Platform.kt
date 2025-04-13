package jato.app.jato_utils


enum class JPlatform {
    Android,
    Ios,
    Desktop,
    Web,
}

expect fun platform(): JPlatform
