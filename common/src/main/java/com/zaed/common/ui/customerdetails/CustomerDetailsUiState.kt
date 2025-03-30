package com.zaed.common.ui.customerdetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.CustomerType
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.sale.WholesaleTransaction

data class CustomerDetailsUiState(
    val loading: Boolean = false,
    val customer: WholeSaleCustomer = WholeSaleCustomer(type = CustomerType.SILVER),
    val currentDistributor: User = User(),
    val moneyPayments:  List<Payment> = emptyList(),
    val goldPayments:  List<Payment> = emptyList(),
    val sales : List<WholesaleTransaction> = emptyList(),
)