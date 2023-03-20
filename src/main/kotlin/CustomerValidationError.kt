import arrow.core.Nel

sealed class CustomerValidationError(val msg: String) {
    object InvalidEmail: CustomerValidationError("Invalid email address!")
    object InvalidAge: CustomerValidationError("Invalid age!")
    object EmptyName: CustomerValidationError("Customer name cannot be empty!")
    data class InvalidCustomer(val reasons:Nel<CustomerValidationError>): CustomerValidationError("Invalid customer data!")
}