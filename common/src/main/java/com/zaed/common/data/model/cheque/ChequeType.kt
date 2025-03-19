package com.zaed.common.data.model.cheque

import androidx.annotation.StringRes
import com.zaed.common.R
import com.zaed.common.data.model.DropdownMenuItem

enum class ChequeType(
    @StringRes val titleRes: Int
):DropdownMenuItem{
    PERSONAL(R.string.personal),
    COMPANY(R.string.company);
}