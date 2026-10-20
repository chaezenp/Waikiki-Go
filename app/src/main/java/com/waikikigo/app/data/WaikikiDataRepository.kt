package com.waikikigo.app.data

import com.waikikigo.app.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WaikikiDataRepository {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            username = "Kai_Explorer",
            email = "kai@waikikigo.com",
            selectedTags = setOf(DietaryTag.HAWAIIAN_TRADITIONAL, DietaryTag.PESCATARIAN),
            allergies = setOf(Allergy.PEANUT),
            totalPoints = 450,
            completedQuestIds = setOf("q1"),
            redeemedRewardIds = emptySet()
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _batteryMode = MutableStateFlow(BatteryLocationMode.HIGH_PERFORMANCE)
    val batteryMode: StateFlow<BatteryLocationMode> = _batteryMode.asStateFlow()

    private val sampleRestaurants = listOf(
        Restaurant(
            id = "r1",
            name = "Ono Seafood",
            description = "Iconic local poke counter famous for fresh ahi and tako poke made to order with sesame oil and limu.",
            cuisine = "Hawaiian Poke",
            address = "747 Kapahulu Ave, Waikiki, HI",
            latitude = 21.2778,
            longitude = -157.8130,
            rating = 4.8,
            priceRange = "$$",
            imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c",
            dietaryTags = setOf(DietaryTag.HAWAIIAN_TRADITIONAL, DietaryTag.PESCATARIAN, DietaryTag.GLUTEN_FREE, DietaryTag.DAIRY_FREE, DietaryTag.NUT_FREE),
            excludedAllergies = setOf(Allergy.PEANUT, Allergy.TREE_NUT, Allergy.MILK),
            menuHighlights = listOf("Shoyu Ahi Poke Bowl", "Hawaiian Style Spicy Ahi", "Tako Poke with Limu"),
            distanceMeters = 280
        ),
        Restaurant(
            id = "r2",
            name = "Duke's Waikiki",
            description = "Beachfront dining named after Duke Kahanamoku. Serve fresh seafood, steaks, and the world-famous Hula Pie.",
            cuisine = "Seafood & Grill",
            address = "2335 Kalakaua Ave, Waikiki, HI",
            latitude = 21.2762,
            longitude = -157.8268,
            rating = 4.7,
            priceRange = "$$$",
            imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5",
            dietaryTags = setOf(DietaryTag.PESCATARIAN, DietaryTag.GLUTEN_FREE, DietaryTag.SHELLFISH_FREE, DietaryTag.NUT_FREE),
            excludedAllergies = setOf(Allergy.PEANUT, Allergy.TREE_NUT),
            menuHighlights = listOf("Duke's Ultimate Fish Tacos", "Kualoa Crab Cakes", "Original Hula Pie"),
            distanceMeters = 150
        ),
        Restaurant(
            id = "r3",
            name = "Rainbow Drive-In",
            description = "Historic plate lunch staple since 1961 serving generous Hawaiian comfort food like Loco Moco and Chili Rice.",
            cuisine = "Hawaiian Comfort / Plate Lunch",
            address = "3308 Kanaina Ave, Waikiki, HI",
            latitude = 21.2735,
            longitude = -157.8140,
            rating = 4.6,
            priceRange = "$",
            imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd",
            dietaryTags = setOf(DietaryTag.HAWAIIAN_TRADITIONAL, DietaryTag.NUT_FREE),
            excludedAllergies = setOf(Allergy.PEANUT, Allergy.TREE_NUT, Allergy.SHELLFISH),
            menuHighlights = listOf("Classic Loco Moco Plate", "Mix Plate (Mahi, BBQ Beef, Chicken)", "Slush Float"),
            distanceMeters = 420
        ),
        Restaurant(
            id = "r4",
            name = "Marugame Udon Waikiki",
            description = "Bustling cafeteria-style eatery featuring freshly made sanuki udon noodles and tempura skewers.",
            cuisine = "Japanese Udon & Tempura",
            address = "2310 Kuhio Ave, Waikiki, HI",
            latitude = 21.2785,
            longitude = -157.8250,
            rating = 4.7,
            priceRange = "$",
            imageUrl = "https://images.unsplash.com/photo-1569718212165-3a8278d5f624",
            dietaryTags = setOf(DietaryTag.VEGETARIAN, DietaryTag.DAIRY_FREE, DietaryTag.NUT_FREE),
            excludedAllergies = setOf(Allergy.PEANUT, Allergy.TREE_NUT, Allergy.MILK, Allergy.SHELLFISH),
            menuHighlights = listOf("Kake Udon with Tempura Flakes", "Niku Udon with Sweet Beef", "Vegetable Kakiage Tempura"),
            distanceMeters = 200
        ),
        Restaurant(
            id = "r5",
            name = "Poke Bar Waikiki",
            description = "Build-your-own fresh poke bowl spot with custom base options like purple rice, salad greens, and gluten-free marinades.",
            cuisine = "Custom Poke",
            address = "2260 Kalakaua Ave, Waikiki, HI",
            latitude = 21.2790,
            longitude = -157.8280,
            rating = 4.6,
            priceRange = "$$",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999",
            dietaryTags = setOf(DietaryTag.VEGAN, DietaryTag.VEGETARIAN, DietaryTag.GLUTEN_FREE, DietaryTag.DAIRY_FREE, DietaryTag.NUT_FREE, DietaryTag.PESCATARIAN),
            excludedAllergies = setOf(Allergy.PEANUT, Allergy.TREE_NUT, Allergy.MILK, Allergy.EGG),
            menuHighlights = listOf("Tofu & Seaweed Vegan Bowl", "Fresh Salmon Sriracha Ahi Bowl", "Gluten-Free Ponzu Ahi"),
            distanceMeters = 310
        ),
        Restaurant(
            id = "r6",
            name = "Helena's Hawaiian Food",
            description = "James Beard Award winner serving authentic traditional Hawaiian feasts including Pipikaula short ribs and Poi.",
            cuisine = "Traditional Hawaiian",
            address = "1240 N School St (Waikiki Express Shuttle)",
            latitude = 21.2750,
            longitude = -157.8200,
            rating = 4.9,
            priceRange = "$$",
            imageUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836",
            dietaryTags = setOf(DietaryTag.HAWAIIAN_TRADITIONAL, DietaryTag.GLUTEN_FREE, DietaryTag.DAIRY_FREE, DietaryTag.NUT_FREE),
            excludedAllergies = setOf(Allergy.PEANUT, Allergy.TREE_NUT, Allergy.MILK, Allergy.WHEAT),
            menuHighlights = listOf("Pipikaula Short Ribs", "Kalua Pig & Lomi Salmon", "Fresh Poi & Haupia"),
            distanceMeters = 500
        )
    )

    private val sampleQuests = listOf(
        Quest(
            id = "q1",
            restaurantId = "r1",
            restaurantName = "Ono Seafood",
            title = "Snap Your Fresh Ahi Poke! 📸",
            description = "Capture a high-quality picture of your poke bowl with Waikiki palm trees or ocean in the background.",
            pointsReward = 150,
            questType = QuestType.PHOTO,
            isCompleted = true
        ),
        Quest(
            id = "q2",
            restaurantId = "r2",
            restaurantName = "Duke's Waikiki",
            title = "Record the Hula Pie Craze 🎥",
            description = "Record a short 5-second video tasting Duke's legendary Hula Pie at sunset.",
            pointsReward = 200,
            questType = QuestType.VIDEO,
            isCompleted = false
        ),
        Quest(
            id = "q3",
            restaurantId = "r3",
            restaurantName = "Rainbow Drive-In",
            title = "Loco Moco Foodie Review ⭐",
            description = "Post a 2-sentence review highlighting your favorite plate lunch item.",
            pointsReward = 100,
            questType = QuestType.REVIEW,
            isCompleted = false
        ),
        Quest(
            id = "q4",
            restaurantId = "r4",
            restaurantName = "Marugame Udon Waikiki",
            title = "Fresh Udon Noodle Check-In 📍",
            description = "Check in while standing in line at Marugame and upload a photo of the fresh udon making station.",
            pointsReward = 120,
            questType = QuestType.CHECK_IN,
            isCompleted = false
        ),
        Quest(
            id = "q5",
            restaurantId = "r5",
            restaurantName = "Poke Bar Waikiki",
            title = "Custom Bowl Masterpiece 📸",
            description = "Take a photo showing your colorful custom poke bowl with all dietary toppings.",
            pointsReward = 140,
            questType = QuestType.PHOTO,
            isCompleted = false
        )
    )

    private val sampleRewards = listOf(
        Reward(
            id = "rw1",
            restaurantId = "r1",
            restaurantName = "Ono Seafood",
            title = "Free Passion Orange Guava (POG) Juice 🍹",
            description = "Receive 1 free canned POG juice or Hawaiian iced tea with the purchase of any large poke bowl.",
            purchaseRequirement = "Requires purchase of 1 Regular or Large Poke Bowl",
            pointCost = 250,
            couponCode = "ONO-POG-BUY1",
            category = RewardCategory.GIFT_WITH_PURCHASE,
            isRedeemed = false
        ),
        Reward(
            id = "rw2",
            restaurantId = "r2",
            restaurantName = "Duke's Waikiki",
            title = "15% Off Total Dinner Bill 🏷️",
            description = "Get 15% off your entire dining bill during dinner service.",
            purchaseRequirement = "Requires purchase of at least 1 Main Entrée ($25 min order)",
            pointCost = 400,
            couponCode = "DUKES-15OFF-DINNER",
            category = RewardCategory.PERCENT_DISCOUNT,
            isRedeemed = false
        ),
        Reward(
            id = "rw3",
            restaurantId = "r3",
            restaurantName = "Rainbow Drive-In",
            title = "Buy 1 Plate Lunch Get 2nd 50% Off (BOGO 50%) 🍱",
            description = "Purchase any Loco Moco or Mix Plate and get a second plate lunch at 50% off.",
            purchaseRequirement = "Requires purchase of 1 Regular Plate Lunch",
            pointCost = 200,
            couponCode = "RAINBOW-BOGO50",
            category = RewardCategory.BUY_ONE_GET_ONE,
            isRedeemed = false
        ),
        Reward(
            id = "rw4",
            restaurantId = "r4",
            restaurantName = "Marugame Udon Waikiki",
            title = "Free Tempura Skewer with Udon Purchase 🍢",
            description = "Get 1 free Shrimp or Chicken Tempura skewer when ordering any regular or large udon bowl.",
            purchaseRequirement = "Requires purchase of any Udon Bowl",
            pointCost = 300,
            couponCode = "MARUGAME-TEMP-BUY1",
            category = RewardCategory.GIFT_WITH_PURCHASE,
            isRedeemed = false
        ),
        Reward(
            id = "rw5",
            restaurantId = "r6",
            restaurantName = "Helena's Hawaiian Food",
            title = "Free Coconut Haupia Dessert with Feast 🥥",
            description = "Receive a complimentary Haupia dessert platter when ordering Helena's Combination Feast.",
            purchaseRequirement = "Requires purchase of Helena's Combination Feast",
            pointCost = 450,
            couponCode = "HELENA-HAUPIA-FEAST",
            category = RewardCategory.GIFT_WITH_PURCHASE,
            isRedeemed = false
        ),
        Reward(
            id = "rw6",
            restaurantId = "r5",
            restaurantName = "Poke Bar Waikiki",
            title = "20% Off Second Poke Bowl 🏷️",
            description = "Get 20% discount on a second custom poke bowl during the same order.",
            purchaseRequirement = "Requires purchase of 1 Custom Poke Bowl at full price",
            pointCost = 350,
            couponCode = "POKEBAR-20OFF-BUY1",
            category = RewardCategory.PERCENT_DISCOUNT,
            isRedeemed = false
        )
    )

    private val _quests = MutableStateFlow(sampleQuests)
    val quests: StateFlow<List<Quest>> = _quests.asStateFlow()

    private val _rewards = MutableStateFlow(sampleRewards)
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()

    fun getFilteredRestaurants(searchQuery: String = ""): List<Restaurant> {
        val user = _userProfile.value
        return sampleRestaurants.filter { restaurant ->
            val matchesSearch = searchQuery.isBlank() ||
                    restaurant.name.contains(searchQuery, ignoreCase = true) ||
                    restaurant.cuisine.contains(searchQuery, ignoreCase = true) ||
                    restaurant.description.contains(searchQuery, ignoreCase = true)

            // Allergy Safety Filter: Ensure restaurant excludes all allergies specified by user
            val isSafeFromAllergies = user.allergies.all { allergy ->
                restaurant.excludedAllergies.contains(allergy)
            }

            // Dietary Tags Filter: If user selected tags, check if restaurant provides at least one requested tag
            val matchesDietary = user.selectedTags.isEmpty() || user.selectedTags.any { tag ->
                restaurant.dietaryTags.contains(tag)
            }

            matchesSearch && isSafeFromAllergies && matchesDietary
        }
    }

    fun updateProfile(selectedTags: Set<DietaryTag>, allergies: Set<Allergy>) {
        _userProfile.value = _userProfile.value.copy(
            selectedTags = selectedTags,
            allergies = allergies
        )
    }

    fun completeQuest(questId: String) {
        val currentQuests = _quests.value.toMutableList()
        val index = currentQuests.indexOfFirst { it.id == questId }
        if (index != -1 && !currentQuests[index].isCompleted) {
            val completedQuest = currentQuests[index].copy(isCompleted = true)
            currentQuests[index] = completedQuest
            _quests.value = currentQuests

            val pointsEarned = completedQuest.pointsReward
            val currentProfile = _userProfile.value
            _userProfile.value = currentProfile.copy(
                totalPoints = currentProfile.totalPoints + pointsEarned,
                completedQuestIds = currentProfile.completedQuestIds + questId
            )
        }
    }

    fun redeemReward(rewardId: String): Boolean {
        val currentRewards = _rewards.value.toMutableList()
        val index = currentRewards.indexOfFirst { it.id == rewardId }
        if (index != -1 && !currentRewards[index].isRedeemed) {
            val reward = currentRewards[index]
            val profile = _userProfile.value
            if (profile.totalPoints >= reward.pointCost) {
                currentRewards[index] = reward.copy(isRedeemed = true)
                _rewards.value = currentRewards

                _userProfile.value = profile.copy(
                    totalPoints = profile.totalPoints - reward.pointCost,
                    redeemedRewardIds = profile.redeemedRewardIds + rewardId
                )
                return true
            }
        }
        return false
    }

    fun toggleBatteryMode() {
        _batteryMode.value = if (_batteryMode.value == BatteryLocationMode.HIGH_PERFORMANCE) {
            BatteryLocationMode.BATTERY_SAVER
        } else {
            BatteryLocationMode.HIGH_PERFORMANCE
        }
    }
}
