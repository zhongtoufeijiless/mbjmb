package com.tcssj.mbjmb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcssj.mbjmb.network.VerifyCodeRepository
import com.tcssj.mbjmb.model.SendVerifyCodeRequest
import com.tcssj.mbjmb.model.ResponseData
import com.tcssj.mbjmb.model.VerifyCodeBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class VerifyCodeIntent {
    data class SendCode(val mobile: String, val type: String = "text") : VerifyCodeIntent()
}

data class VerifyCodeState(
    val loading: Boolean = false,
    val result: ResponseData<VerifyCodeBody>? = null,
    val error: String? = null
)

class VerifyCodeViewModel : ViewModel() {
    private val _state = MutableStateFlow(VerifyCodeState())
    val state: StateFlow<VerifyCodeState> = _state

    fun handleIntent(intent: VerifyCodeIntent) {
        when (intent) {
            is VerifyCodeIntent.SendCode -> sendCode(intent.mobile, intent.type)
        }
    }

    private fun sendCode(mobile: String, type: String) {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val req = SendVerifyCodeRequest(type = type, mobile = mobile)
            val result = VerifyCodeRepository.sendVerifyCode(req)
            _state.value = if (result.isSuccess) {
                _state.value.copy(loading = false, result = result.getOrNull(), error = null)
            } else {
                _state.value.copy(loading = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
} 