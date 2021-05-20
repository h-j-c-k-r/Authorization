package test.handh.authorization.app.extensions

fun Float.round(digits: Int): String {
    return "%.${digits}f".format(this)
}