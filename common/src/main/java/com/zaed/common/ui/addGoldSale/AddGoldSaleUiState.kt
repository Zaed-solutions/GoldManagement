package com.zaed.common.ui.addGoldSale

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.addpurchase.ProductType

data class AddGoldSaleUiState(
    val isFinished: Boolean = false,
    val isLoading: Boolean = false,
    val initialSale: WholesaleTransaction = WholesaleTransaction(productType = ProductType.GOLD),
    val sale: WholesaleTransaction = WholesaleTransaction(productType = ProductType.GOLD),
    val selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    val customerSearchQuery: String = "",
    val suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    val currentUser: User = User(),
    val payments: List<Payment> = emptyList(),
    val categories: List<Category> = emptyList(),
) {
    val totalMoneyPaid
        get() = payments.filter { it.type != PaymentType.FUTURES}.filter { it.type != PaymentType.GOLD }
            .sumOf { if (it.type == PaymentType.REMAIN) it.amount.unaryMinus() else it.amount }
    val totalGoldPaid
        get() = payments.filterIsInstance<GoldPayment>().sumOf { it.givenGoldAmount }

    val totalMoneyFuturePaid get() = payments.filter { (it.type == PaymentType.FUTURES) && !it.goldPayment }.sumOf { it.amount }
    val totalGoldFuturePaid get() = payments.filter { (it.type == PaymentType.FUTURES) && it.goldPayment }.sumOf { it.amount }

    val totalMoneyAmount get() = if (sale.payWithCash) sale.totalAmount else sale.products.sumOf { it.totalLaborCost }
    val totalGoldAmount get() = sale.products.sumOf { it.grams }
}
