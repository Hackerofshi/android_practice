package com.shixin.ui.jetpack.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.shixin.ui.jetpack.databinding.modle.User
import com.shixin.R
import com.shixin.databinding.ActivityDatabingTestBinding


class DatabingTestActivity : AppCompatActivity() {

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityDatabingTestBinding>(this, R.layout.activity_databing_test)
        // setContentView(R.layout.activity_databing_test)
        user = User()
        user.name = "111";
        user.password = "456"

        binding.user = user

        binding.tvPwd.setText("---")

    }
}