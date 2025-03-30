package com.zaed.common.ui.addcustomers

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.AddWholeSaleCustomerRequest
import com.zaed.common.data.model.customer.CustomerType
import com.zaed.common.ui.auth.FieldsError

data class AddCustomersState(
    val isLoading: Boolean = false,
    val distributor : User = User(),
    val error: AddCustomersScreenError? = null,
    val fieldsError: FieldsError = FieldsError.NONE,
    val successStatus: Boolean = false,
    val isEditMode: Boolean = false,
    val customerId: String = "",
    val request: AddWholeSaleCustomerRequest = AddWholeSaleCustomerRequest(type = CustomerType.SILVER)
)