package com.waikikigo.app.model

enum class DietaryTag(val displayName: String) {
    VEGAN("Vegan"),
    VEGETARIAN("Vegetarian"),
    GLUTEN_FREE("Gluten-Free"),
    HALAL("Halal"),
    KOSHER("Kosher"),
    DAIRY_FREE("Dairy-Free"),
    NUT_FREE("Nut-Free"),
    SHELLFISH_FREE("Shellfish-Free"),
    PESCATARIAN("Pescatarian"),
    HAWAIIAN_TRADITIONAL("Traditional Hawaiian")
}

enum class Allergy(val displayName: String) {
    PEANUT("Peanuts"),
    TREE_NUT("Tree Nuts"),
    SHELLFISH("Shellfish"),
    FISH("Fish"),
    EGG("Egg"),
    MILK("Milk / Dairy"),
    SOY("Soy"),
    WHEAT("Wheat / Gluten")
}

enum class QuestType(val displayName: String) {
    PHOTO("Photo Challenge 📸"),
    VIDEO("Video Challenge 🎥"),
    REVIEW("Local Grind Review ⭐"),
    CHECK_IN("Waikiki Check-In 📍")
}

data class UserProfile(
    val username: String = "WaikikiExplorer",
    val email: String = "explorer@waikikigo.com",
    val selectedTags: Set<DietaryTag> = setOf(DietaryTag.HAWAIIAN_TRADITIONAL),
    val allergies: Set<Allergy> = emptySet(),
    val totalPoints: Int = 350,
    val completedQuestIds: Set<String> = emptySet(),
    val redeemedRewardIds: Set<String> = emptySet()
)

data class Quest(
    val id: String,
    val restaurantId: String,
    val restaurantName: String,
    val title: String,
    val description: String,
    val pointsReward: Int,
    val questType: QuestType,
    val isCompleted: Boolean = false
)

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val cuisine: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val priceRange: String,
    val imageUrl: String,
    val dietaryTags: Set<DietaryTag>,
    val excludedAllergies: Set<Allergy>,
    val menuHighlights: List<String>,
    val distanceMeters: Int = 350
)

enum class RewardCategory(val displayName: String) {
    DISCOUNT("Discount Coupon 🏷️"),
    FREE_ITEM("Free Food / Drink 🍹"),
    EXCLUSIVE("VIP Experience ✨")
}

data class Reward(
    val id: String,
    val restaurantId: String,
    val restaurantName: String,
    val title: String,
    val description: String,
    val pointCost: Int,
    val couponCode: String,
    val category: RewardCategory,
    val isRedeemed: Boolean = false
)

enum class BatteryLocationMode(val title: String, val updateIntervalMs: Long, val description: String) {
    HIGH_PERFORMANCE("Active Explorer Mode ⚡", 5000L, "High accuracy GPS updates while walking around Waikiki."),
    BATTERY_SAVER("Stationary Dine Saver 🔋", 30000L, "Low frequency location updates to conserve battery life while dining.")
}
