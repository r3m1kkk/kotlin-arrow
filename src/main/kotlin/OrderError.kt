sealed class OrderError(val errorMessage:String) {
    object EmptyOrder: OrderError("Order is empty!")
    object NegativeDiscount: OrderError("Discount cannot be negative!")
    data class InvalidPromoCode(val promoCode: String): OrderError("Invalid promo code: `$promoCode`!")
}