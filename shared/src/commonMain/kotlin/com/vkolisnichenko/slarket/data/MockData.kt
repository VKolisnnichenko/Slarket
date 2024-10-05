package com.vkolisnichenko.slarket.data

import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.model.PizzaModel
import com.vkolisnichenko.slarket.domain.model.RestaurantDetailsModel

val pizzaItemsList = listOf(
    PizzaModel(
        "Cheese pizza",
        69,
        "https://images.arla.com/recordid/F67E678C-299C-46E8-B52D14612437E31D/vegetable-pizza.jpg?width=1200&height=630&mode=crop&format=jpg",
        "tomatoes, cheese, mushrooms, potatoes, chicken",
        id = 1
    ),
    PizzaModel(
        "S salyuami",
        228,
        "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipe%20Ramp%20Up%2F2021-07-Chicken-Alfredo-Pizza%2FChicken-Alfredo-Pizza-KitchnKitchn2970-1_01",
        "tomatoes, cheese, mushrooms, potatoes, chicken",
        id = 2
    ),
    PizzaModel(
        "Epic meat pizza",
        100,
        "https://assets.dots.live/misteram-public/0187c919-5b86-7061-b5f3-865991e86199-826x0.png",
        "tomatoes, cheese, mushrooms, potatoes, chicken",
        id = 3
    ),
    PizzaModel(
        "Cheese pizza",
        69,
        "https://nt.global.ssl.fastly.net/binaries/content/gallery/website/national/library/discover-and-learn/food-recipes/pizza-face.jpg",
        "tomatoes, cheese, mushrooms, potatoes, chicken"
    ),
    PizzaModel(
        "S salyuami",
        228,
        "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipe%20Ramp%20Up%2F2021-07-Chicken-Alfredo-Pizza%2FChicken-Alfredo-Pizza-KitchnKitchn2970-1_01",
        "tomatoes, cheese, mushrooms, potatoes, chicken",
        id = 4
    ),
    PizzaModel(
        "Epic meat pizza",
        100,
        "https://images.aws.nestle.recipes/resized/ae6660e5fab47d260f935ff39e75d808_Sunny_Side_Up_Pizza_1080_850.jpg",
        "tomatoes, cheese, mushrooms, potatoes, chicken",
        id = 5
    ),
    PizzaModel(
        "Slavuha pizza",
        123,
        "https://media.istockphoto.com/id/1349383878/photo/hawaiian-pizza-with-ham-and-pineapple.jpg?s=612x612&w=0&k=20&c=P7rJNWhe1utWDDXUa4ZyZdnl4C5he8jfWD-dKf_hefI=",
        "tomatoes, cheese, mushrooms, potatoes, chicken",
        id = 5
    ),
)

val mockDataPizzaList = listOf(
    PizzaRestaurantItemModel(
        1,
        "Pizza 32 SM",
        "Experience the authentic taste of Italy at Mama Mia’s, where traditional recipes meet modern creativity.",
        "https://images.arla.com/recordid/F67E678C-299C-46E8-B52D14612437E31D/vegetable-pizza.jpg?width=1200&height=630&mode=crop&format=jpg"
    ),
    PizzaRestaurantItemModel(
        2,
        "Pekorini",
        "Whether you're in the mood for a classic Margherita or an adventurous new flavor, every bite promises a slice of heaven.",
        "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipe%20Ramp%20Up%2F2021-07-Chicken-Alfredo-Pizza%2FChicken-Alfredo-Pizza-KitchnKitchn2970-1_01",
    ),
    PizzaRestaurantItemModel(
        3,
        "Krop pizza",
        "Discover gourmet pizzas with a twist at Urban Crust. We bring a city vibe to every dish, with inventive toppings and unique flavor combinations that will excite your taste buds.",
        "https://assets.dots.live/misteram-public/0187c919-5b86-7061-b5f3-865991e86199-826x0.png",
    ),
    PizzaRestaurantItemModel(
        4,
        "Mamamia",
        "At The Rustic Pie, simplicity is key. Our farm-to-table pizzas are crafted using local, organic ingredients, offering you a wholesome and delicious meal in every bite.",
        "https://nt.global.ssl.fastly.net/binaries/content/gallery/website/national/library/discover-and-learn/food-recipes/pizza-face.jpg",
    ),
    PizzaRestaurantItemModel(
        5,
        "Sous pizza",
        "Enjoy the warmth and comfort of wood-fired pizzas at Fireside Pizzas. Our menu features a range of classic and specialty pizzas, perfect for a cozy night out or a casual gathering with friends.",
        "https://images.aws.nestle.recipes/resized/ae6660e5fab47d260f935ff39e75d808_Sunny_Side_Up_Pizza_1080_850.jpg",
    )

)


val pizzaDetailsMockList = listOf(
    RestaurantDetailsModel(
        1,
        "Pizza 32 SM",
        "Experience the authentic taste of Italy at Mama Mia’s, where traditional recipes meet modern creativity.",
        "https://images.arla.com/recordid/F67E678C-299C-46E8-B52D14612437E31D/vegetable-pizza.jpg?width=1200&height=630&mode=crop&format=jpg",
        pizzaItemsList
    ),
    RestaurantDetailsModel(
        2,
        "Pekorini",
        "Whether you're in the mood for a classic Margherita or an adventurous new flavor, every bite promises a slice of heaven.",
        "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipe%20Ramp%20Up%2F2021-07-Chicken-Alfredo-Pizza%2FChicken-Alfredo-Pizza-KitchnKitchn2970-1_01",
        pizzaItemsList
    ),

    RestaurantDetailsModel(
        3,
        "Krop pizza",
        "Discover gourmet pizzas with a twist at Urban Crust. We bring a city vibe to every dish, with inventive toppings and unique flavor combinations that will excite your taste buds.",
        "https://assets.dots.live/misteram-public/0187c919-5b86-7061-b5f3-865991e86199-826x0.png",
        pizzaItemsList
    ),
    RestaurantDetailsModel(
        4,
        "Mamamia",
        "At The Rustic Pie, simplicity is key. Our farm-to-table pizzas are crafted using local, organic ingredients, offering you a wholesome and delicious meal in every bite.",
        "https://nt.global.ssl.fastly.net/binaries/content/gallery/website/national/library/discover-and-learn/food-recipes/pizza-face.jpg",
        pizzaItemsList
    ),
    RestaurantDetailsModel(
        5,
        "Sous pizza",
        "Enjoy the warmth and comfort of wood-fired pizzas at Fireside Pizzas. Our menu features a range of classic and specialty pizzas, perfect for a cozy night out or a casual gathering with friends.",
        "https://images.aws.nestle.recipes/resized/ae6660e5fab47d260f935ff39e75d808_Sunny_Side_Up_Pizza_1080_850.jpg",
        pizzaItemsList
    )
)
