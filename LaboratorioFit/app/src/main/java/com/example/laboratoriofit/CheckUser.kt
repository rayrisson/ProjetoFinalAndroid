package com.example.laboratoriofit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class CheckUser: AppCompatActivity(){
    var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(user == null){
            val intent1 = Intent(this, LoginActivity::class.java)
            startActivity(intent1)
            finish()
        }else{
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
            finish()
        }
    }

}