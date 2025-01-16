package com.example.twse.model.constants

/**
 * 篩選選項
 */
enum class FilterOptions(val description: String) {
    CODE_ASC("依股票代號升序"),
    CODE_DESC("依股票代號降序");

    companion object {
        /**
         * 根據字串值返回對應的 FilterOptions 枚舉，找不到則返回 null。
         */
        fun fromValue(value: String): FilterOptions? {
            return values().find { it.name == value }
        }
    }
}
