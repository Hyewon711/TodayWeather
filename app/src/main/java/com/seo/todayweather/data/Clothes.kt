package com.seo.todayweather.data

enum class Top(
    val temperatureRange: IntRange,
    val outfit: String,
    val icon: String
) {
    // 28 ~
    SLEEVELESS_T_SHIRT(28..100, "민소매" ,"URI주소"),
    LINEN_CLOTHES(28..100,"린넨 옷","URI 주소"),
    SHORT_SLEEVE(23..100, "반팔", "URI 주소"),
    // 23 ~
    THIN_SHIRT(23..27,"얇은 셔츠", "URI 주소"),
    // 20 ~
    BLOUSE(20..22,"블라우스","URI 주소"),
    LONG_SLEEVE(20..22,"긴팔티","URI 주소"),
    // 17 ~
    LIGHT_HOODIE(17..19,"가벼운 후드","URI 주소"),
    // 12 ~
    SWEATSHIRT(12..19,"맨투맨","URI 주소"),
    // 9 ~
    KNIT(9..19,"니트","URI 주소"),
    // 5 ~
    HEAVY_HOODIE(5..11,"두꺼운 후드", "URI 주소"),
    HEATTECH(5..8,"히트텍","URI주소"),
    // 5 이하
    TURTLENECK(-100..5,"목폴라", "URI주소"),
    HEAVY_KNIT(-100..5,"두꺼운 니트", "URI 주소"),
    SWEATER(-100..8, "스웨터","URI주소");

    fun isTemperatureInRange(temperature: Int): Boolean {
        return temperature in temperatureRange
    }
}

enum class Bottom(
    val temperatureRange: IntRange,
    val outfit: String,
    val icon: String
) {

    // 28 이상
    SHORT_SKIRT(28..100,"짧은 치마","URI주소"),
    // 23 ~
    SHORTS(23..100, "반바지","URI주소"),
    // 20 ~
    COTTON_PANTS(20..100, "면바지", "URI주소"),
    SLACKS(20..22,"슬랙스","URI주소"),
    // 17 ~
    LONG_PANTS(17..19,"긴바지","URI 주소"),
    // 9 ~
    JEANS(9..16, "청바지", "URI 주소"),
    NAPPING_PANTS(9..11,"기모바지","URI주소"),
    // 4 이하
    WARM_PANTS(0..10, "털옷바지","URI주소");

    fun isTemperatureInRange(temperature: Int): Boolean {
        return temperature in temperatureRange
    }
}

enum class Outerwear(
    val temperatureRange: IntRange,
    val outfit: String,
    val icon: String
) {
    // 17 ~ 19
    LIGHT_CARDIGAN(17..19,"얇은 가디건","URI주소"),
    // 12 ~ 16
    LIGHT_JACKET(12..16, "가벼운 자켓","URI주소"),
    CARDIGAN(12..16, "가디건", "URI 주소"),
    DENIM_JACKET(12..16, "청자켓", "URI주소"),
    // 9 ~ 11
    TRENCH_COAT(9..11,"트렌치 코트", "URI 주소"),
    FIELD_JACKET(9..11, "야상 자켓", "URI 주소"),
    JUMPER(9..11,"점퍼", "URI 주소"),



    HEAVY_JACKET(0..10, "두꺼운 자켓","URI주소");

    fun isTemperatureInRange(temperature: Int): Boolean {
        return temperature in temperatureRange
    }
}

// 사용 예시
fun chooseOutfit(temperature: Int): Triple<Top?, Bottom?, Outerwear?> {
    val chosenTop = Top.values().find { it.isTemperatureInRange(temperature) }
    val chosenBottom = Bottom.values().find { it.isTemperatureInRange(temperature) }
    val chosenOuterwear = Outerwear.values().find { it.isTemperatureInRange(temperature) }

    return Triple(chosenTop, chosenBottom, chosenOuterwear)
}

//// 출력
//val outfitChoices = chooseOutfit(15)
//Log.d(TAG, "Top: ${outfitChoices.first?.outfit}")
//Log.d(TAG, "Bottom: ${outfitChoices.second?.outfit}")
//Log.d(TAG, "Outerwear: ${outfitChoices.third?.outfit}")
//Log.d(TAG, "Accessories: ${outfitChoices.first?.accessories?.plus(outfitChoices.second?.accessories ?: emptyList())?.plus(outfitChoices.third?.accessories ?: emptyList())?.joinToString(", ")}")
