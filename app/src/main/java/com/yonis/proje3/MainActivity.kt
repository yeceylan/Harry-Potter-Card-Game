package com.yonis.proje3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yonis.proje3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate((layoutInflater))
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent =Intent(this,menu::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun signinClicked(view : View){
        var email = binding.emailText.text.toString()
        if(!email.contains("@gmail.com")){
            email = email +"@gmail.com"
        }
        var password = binding.passwordText.text.toString()

        if(email.equals(" ") && password.equals(" ")){
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent =Intent(this@MainActivity,menu::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun signupClicked(view : View){
        var email = binding.emailText.text.toString()
        var password = binding.passwordText.text.toString()

        if(email.equals(" ") && password.equals(" ")){
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent =Intent(this@MainActivity,menu::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
}