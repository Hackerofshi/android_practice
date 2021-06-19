package com.shixin.ui.jetpack.databinding.modle

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class User {
    public var name: String? = null

    public var password: String? = null
}


class GoodsBean : BaseObservable() {

    @Bindable
    var name = ""
    set(value) {
        field = value
        //只更新本字段
       //notifyPropertyChanged(BR.name)
    }

    @Bindable
    var details = ""
    set(value) {
        field = value
        //更新所有字段
        notifyChange()
    }

    var price = 0F

}