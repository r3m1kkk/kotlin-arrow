import arrow.core.Either

fun main() {
    val order = Order()
    order.addProduct(OrderItem("LEGO23034", 199.99, 3))

    val orderAmount = order.calculateTotalPrice(0.0,"INVALID")

    when(orderAmount) {
        is Either.Right -> println("Total order amount is: ${orderAmount.value}")
        is Either.Left -> println("Error while calculating order amount. ${orderAmount.value.errorMessage}")
    }

    val customer = Customer("", "", 290)
    val registrationResult = CustomerValidator().validateCustomer(customer)

    when(registrationResult) {
        is Either.Left -> registrationResult.value.first().gatherErrors().forEach{ println(it) }
        is Either.Right -> println("Customer successfully passed validation. $registrationResult")
    }
}