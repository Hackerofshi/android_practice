package com.shixin.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.shixin.databinding.modle.User
import com.shixin.rxjava.R
import com.shixin.rxjava.databinding.ActivityDatabingTestBinding


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