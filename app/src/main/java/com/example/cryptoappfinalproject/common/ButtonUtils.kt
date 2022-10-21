package com.example.cryptoappfinalproject.common

import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.domain.ButtonActions
import com.example.cryptoappfinalproject.domain.ButtonTypes

object ButtonUtil {

    val BUTTONS = listOf<ButtonTypes>(
        ButtonTypes.Numeric(1),
        ButtonTypes.Numeric(2),
        ButtonTypes.Numeric(3),
        ButtonTypes.Numeric(4),
        ButtonTypes.Numeric(5),
        ButtonTypes.Numeric(6),
        ButtonTypes.Numeric(7),
        ButtonTypes.Numeric(8),
        ButtonTypes.Numeric(9),
//        ButtonTypes.Dot("."),
        ButtonTypes.Numeric(0),
        ButtonTypes.Remove(R.drawable.ic_remove_button, ButtonActions.REMOVE),
    )

}