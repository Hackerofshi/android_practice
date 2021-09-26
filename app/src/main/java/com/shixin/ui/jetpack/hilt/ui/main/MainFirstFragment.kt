package com.shixin.ui.jetpack.hilt.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.shixin.ui.jetpack.hilt.di.qualifiers.ActivityScope
import com.shixin.ui.jetpack.hilt.di.qualifiers.AppScope
import com.shixin.ui.jetpack.hilt.di.qualifiers.FragmentScope
import com.shixin.ui.jetpack.hilt.ui.main.ActivityViewModel
import com.shixin.ui.jetpack.hilt.ui.main.FragmentViewModel
import com.shixin.R
import com.shixin.ui.rxjava.SecondActivity

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class MainFirstFragment : Fragment(R.layout.fragment_first) {

    private val TAG = this.javaClass.toString()

    @AppScope
    @Inject
    lateinit var appHash: String

    @ActivityScope
    @Inject
    lateinit var activityHash: String

    @FragmentScope
    @Inject
    lateinit var fragmentHash: String

    private val activityViewModel by activityViewModels<ActivityViewModel>()
    private val fragmentViewModel by viewModels<FragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "app : $appHash")
        Log.d(TAG, "activity : $activityHash")
        Log.d(TAG, "fragment : $fragmentHash")
        Log.d(TAG, "activity vm: $activityViewModel")
        Log.d(TAG, "fragment vm: $fragmentViewModel")
        Log.d(TAG, "activity vm repo: ${activityViewModel.repository}")
        Log.d(TAG, "fragment vm repo: ${fragmentViewModel.repository}")

        button_activity.setOnClickListener {
            //startActivity(Intent(activity, SecondActivity::class.java));
            activityViewModel.test()
        }
        button_fragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        //Activity
        lifecycleScope.launch {
            activityViewModel.sharedFlow.collect {
                Log.i("TAG", "-----------$it")
            }
        }
    }
}
