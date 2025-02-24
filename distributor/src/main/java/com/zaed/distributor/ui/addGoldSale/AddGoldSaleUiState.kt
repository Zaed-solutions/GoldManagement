package com.zaed.distributor.ui.addGoldSale

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.sale.WholesaleGoldSale

data class AddGoldSaleUiState(
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val initialSale: WholesaleGoldSale = WholesaleGoldSale(),
    val sale: WholesaleGoldSale = WholesaleGoldSale(),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    val currentUser: User = User(),
    val moneyPayments: List<MoneyPayment> = emptyList(),
    val goldPayments: List<GoldPayment> = emptyList(),
){
    val laborCost: Double
        get() = sale.products.sumOf { it.laborCost }
    val totalAmount: Double
        get() = sale.products.sumOf { it.grams * it.gramPrice }
    val totalMoneyPaid: Double
        get() = moneyPayments.filter { it.type != PaymentType.FUTURES }.sumOf { it.amount }
    val totalGoldPrice: Double
        get() =
             goldPayments.filter { it.pricePerGram!=0.0 }.sumOf { it.pricePerGram * it.givenGoldAmount }

    val totalPaid: Double
        get() = totalMoneyPaid + totalGoldPrice
    val totalGoldAmount: Double
        get() = goldPayments.sumOf { it.givenGoldAmount }


}
