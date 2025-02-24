package com.zaed.distributor.ui.goldsaledetails

import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentStatus
import com.zaed.common.data.model.sale.WholesaleGoldSale

data class GoldSaleDetailsUiState(
    val isLoading: Boolean = false,
    val isSaleDeleted: Boolean = false,
    val currentUser: User = User(),
    val sale: WholesaleGoldSale = WholesaleGoldSale(),
    val payments: List<Payment> = emptyList(),
) {
    val paymentStatus: PaymentStatus
        get() = payments.filterIsInstance<GoldPayment>().any { it.pricePerGram == 0.0 }.let {
            if (it) PaymentStatus.SPECIFYING_KARAT else PaymentStatus.PAID
        }

}
