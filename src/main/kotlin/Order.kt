import arrow.core.Either
import arrow.core.flatMap

class Order{

    private val orderItems = mutableListOf<OrderItem>()

    fun addProduct(orderItem:OrderItem){
        orderItems.add(orderItem)
    }

    private fun calculateAmount(): Either<OrderError, Double> =
        when {
            orderItems.isEmpty() -> Either.Left(OrderError.EmptyOrder)
            else -> Either.Right(orderItems.sumOf { it.unitPrice * it.quantity })
        }

    private fun applyDiscount(amount: Double, discountPercent:Double):Either<OrderError, Double> =
        if(discountPercent !in 0.00..100.00) {
            Either.Left(OrderError.NegativeDiscount)
        } else {
            val reducedAmount = amount - discountPercent * amount / 100
            Either.Right(reducedAmount)
        }

    private fun applyPromoCode(amount: Double, promoCode:String):Either<OrderError, Double> =
        when {
            promoCode.contains("INVALID") -> Either.Left(OrderError.InvalidPromoCode(promoCode))
            promoCode == "VALID50" -> Either.Right(amount - 50)
            else -> Either.Right(amount)
        }

    fun calculateTotalPrice(discountPercent:Double, promoCode: String):Either<OrderError, Double> =
        calculateAmount()
            .flatMap { amount -> applyDiscount(amount, discountPercent) }
            .flatMap { amount -> applyPromoCode(amount, promoCode) }
}

