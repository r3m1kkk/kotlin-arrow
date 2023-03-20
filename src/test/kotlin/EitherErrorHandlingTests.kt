import arrow.core.Either
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EitherErrorHandlingTests {

    @Test
    fun `Either_Right with price sum should be returned when order has items`(){
        val order = Order()
        order.addProduct(OrderItem("LEGO23034", 199.99, 3))
        order.addProduct(OrderItem("MACBOOK2342", 67.89, 2))

        val orderAmount = order.calculateTotalPrice(0.0,"NONE")

        assertEquals(Either.Right(735.75), orderAmount)
    }

    @Test
    fun `Either_Right with price sum, discount and promo code should be returned`(){
        val order = Order()
        order.addProduct(OrderItem("LEGO23034", 100.00, 4))
        order.addProduct(OrderItem("MACBOOK2342", 20.00, 5))

        val orderAmount = order.calculateTotalPrice(10.0,"VALID50")

        assertEquals(Either.Right(400.0), orderAmount)
    }

    @Test
    fun `Either_Left should be returned when order is empty`(){
        val order = Order()

        val orderAmount = order.calculateTotalPrice(0.0,"NONE")

        assertEquals(Either.Left(OrderError.EmptyOrder), orderAmount)
    }

    @Test
    fun `Either_Left should be returned when discount is negative`(){
        val order = Order()
        order.addProduct(OrderItem("LEGO23034", 199.99, 3))

        val orderAmount = order.calculateTotalPrice(-10.0,"NONE")

        assertEquals(Either.Left(OrderError.NegativeDiscount), orderAmount)
    }

    @Test
    fun `Either_Left should be returned when promo code is empty`(){
        val order = Order()
        order.addProduct(OrderItem("LEGO23034", 199.99, 3))

        val orderAmount = order.calculateTotalPrice(0.0,"INVALID")

        assertEquals(Either.Left(OrderError.InvalidPromoCode("INVALID")), orderAmount)
    }

}