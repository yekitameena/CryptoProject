package com.example.cryptoproject.presentation.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.cryptoproject.common.Constants
import com.example.cryptoproject.databinding.ActivityMainBinding
import com.example.cryptoproject.domain.model.CryptoItem
import com.example.cryptoproject.presentation.viewmodel.CryptoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

//Project MVVM, unit test, hilt
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: CryptoViewModel by viewModels()
    var adapter = CryptoAdapter()
    var cryptoList: List<CryptoItem> = listOf()
    var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tournamentRecyclerview.adapter = adapter
        initView()
        setSearchView()
        setTimer()
    }

    override fun onStart() {
        super.onStart()
        countDownTimer?.start()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer?.cancel()
    }

    private fun setTimer() {
        countDownTimer = object : CountDownTimer(
            Constants.TIMER_MILLIS_IN_FUTURE,
            Constants.POLLING_INTERVAl
        ) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.getCryptoDetailsList(Constants.LIMIT, Constants.OFFSET)
            }

            override fun onFinish() {
                val isActivityInForeground = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
                if (isActivityInForeground) {
                    countDownTimer?.start()
                }
            }
        }
    }

    private fun initView() {
        viewModel.getCryptoDetailsList(Constants.LIMIT, Constants.OFFSET)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.cryptoDetailsList.collect {
                withContext(Dispatchers.Main) {
                    it.data?.let {
                            list -> adapter.updateList(list)
                            adapter.notifyDataSetChanged()
                            cryptoList = list
                    }
                }
            }
        }
    }

    fun setSearchView(){
        binding.searchText.editText!!.hint = "Search crypto"
        binding.searchText.editText!!.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                binding.searchText.editText?.hint = ""
            }
            else{
                if (binding.searchText.editText!!.text.isEmpty()){
                    binding.searchText.editText?.hint = "Search crypto"
                }
            }
        }
        binding.searchText.editText!!.addTextChangedListener(SearchTextWatcher())
        binding.clearSearch.setOnClickListener {
            binding.searchText.editText!!.setText("")
            if (!binding.searchText.editText!!.hasFocus()){
                binding.searchText.editText?.hint = "Search crypto"
            }
        }
        binding.searchIcon.setOnClickListener {
            binding.searchText.clearFocus()
            hideKeyboard(applicationContext, binding.root)
        }
        binding.searchText.editText!!.setOnEditorActionListener( TextView.OnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                binding.searchText.clearFocus()
                hideKeyboard(applicationContext,binding.root)
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    inner class SearchTextWatcher: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val searchText = binding.searchText.editText!!.text.toString().trim()
                .lowercase(Locale.getDefault())
            if(searchText.isNotEmpty()){
                binding.clearSearch.visibility = View.VISIBLE
                binding.searchIcon.visibility = View.INVISIBLE
            }
            else{
                binding.clearSearch.visibility = View.INVISIBLE
                binding.searchIcon.visibility = View.VISIBLE
            }

            val list = cryptoList.filter { it.name?.lowercase(Locale.getDefault())?.contains(searchText) == true }.toMutableList()
            adapter.updateList(list)
            adapter.notifyDataSetChanged()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

}


