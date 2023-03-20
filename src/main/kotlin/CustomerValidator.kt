import arrow.core.*
import arrow.typeclasses.Semigroup

class CustomerValidator {

    fun validateCustomer(customer:Customer): Either<Nel<CustomerValidationError>, List<Customer>> =
        listOf(customer).traverse(Semigroup.nonEmptyList()) {
            it.validateErrors()
        }.toEither()

}

fun Customer.validateEmail(): ValidatedNel<CustomerValidationError, Customer> =
    if(!"^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex().matches(email))
        CustomerValidationError.InvalidEmail.invalidNel()
    else
        validNel()

fun Customer.validateAge(): ValidatedNel<CustomerValidationError, Customer> =
    if(age !in 0..100)
        CustomerValidationError.InvalidAge.invalidNel()
    else
        validNel()

fun Customer.validateName(): ValidatedNel<CustomerValidationError, Customer> =
    if(name.isEmpty())
        CustomerValidationError.EmptyName.invalidNel()
    else
        validNel()

fun Customer.validateErrors(): ValidatedNel<CustomerValidationError, Customer> =
    validateEmail().zip(
        Semigroup.nonEmptyList(),
        validateAge()
    ).zip(
        validateName()
    ) { _, _ -> this }.handleErrorWith { CustomerValidationError.InvalidCustomer(it).invalidNel() }

fun CustomerValidationError.gatherErrors() =
    when(this) {
        is CustomerValidationError.InvalidCustomer -> this.reasons.map { it.msg }
        else -> listOf(this.msg)
    }