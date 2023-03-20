# Kotlin and error management using Arrow.kt library

## Either

Either can represent one of two values: Left or Right.
As a rule of thumb we model success with Either.Right and failure using Either.Left.

```kotlin
    private fun applyDiscount(amount: Double, discountPercent:Double):Either<OrderError, Double> =
        if(discountPercent !in 0.00..100.00) {
            Either.Left(OrderError.NegativeDiscount)
        } else {
            val reducedAmount = amount - discountPercent * amount / 100
            Either.Right(reducedAmount)
        }
```

[Order.kt - Validation with Either](src/main/kotlin/Order.kt)

[EitherErrorHandlingTests.kt - Unit tests for Either](src/test/kotlin/EitherErrorHandlingTests.kt)

## Monadic chaining with Either

```kotlin
    fun calculateTotalPrice(discountPercent:Double, promoCode: String):Either<OrderError, Double> =
        calculateAmount()
            .flatMap { amount -> applyDiscount(amount, discountPercent) }
            .flatMap { amount -> applyPromoCode(amount, promoCode) }
```

## Validated

Validated with accumulating errors instead fail-fast approach shown above.

```kotlin
    fun Customer.validateErrors(): ValidatedNel<CustomerValidationError, Customer> =
        validateEmail().zip(
            Semigroup.nonEmptyList(),
            validateAge()
        ).zip(
            validateName()
        ) { _, _ -> this }.handleErrorWith { CustomerValidationError.InvalidCustomer(it).invalidNel() }
```

[CustomerValidator.kt - Validation with Validated](src/main/kotlin/CustomerValidator.kt)
