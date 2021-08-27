package com.example.laboratoriofit.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.laboratoriofit.data.dieta.Dieta
import com.example.laboratoriofit.data.user.UserInfo
import com.example.laboratoriofit.data.user.UserInfoRepository

class UserViewModel(private val userInfoRepository: UserInfoRepository) : ViewModel() {
    var _Info = MutableLiveData<UserInfo>()

    fun retrieveItem(): LiveData<UserInfo> {
        userInfoRepository.getInfo{_Info.value = it}
        return _Info
    }

    fun update(map: Map<String, Any>){
        userInfoRepository.update(map)
    }

    fun isEntryValid(nome: String, peso: String, altura: String, genero: String): Boolean{
        return (nome.isNotBlank() && peso.isNotBlank() && peso.toDouble() > 0 && altura.isNotBlank() && altura.toInt() > 0 && genero.isNotBlank())
    }
}