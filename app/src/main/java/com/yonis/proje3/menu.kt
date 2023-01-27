package com.yonis.proje3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.yonis.proje3.databinding.ActivityMenuBinding
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class menu : AppCompatActivity(),View.OnClickListener {
    private lateinit var db : FirebaseFirestore
    private lateinit var binding: ActivityMenuBinding
    private lateinit var auth: FirebaseAuth
    lateinit var tempList: ArrayList<CardRVModal>
    var level:String =""
    var cardC:Int = 0
    var cardD:Int = 0
    var check:Boolean = false
    var check1:Boolean = false
    var player:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        binding.button4?.setOnClickListener{
        startClicked()
        }
        playerCheck.setOnClickListener(this)
        playerCheck1.setOnClickListener(this)
        hard1.setOnClickListener(this)
        hard2.setOnClickListener(this)
        hard3.setOnClickListener(this)

        auth = Firebase.auth
        db = Firebase.firestore
        courseList = ArrayList()
        tempList = ArrayList()



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.game_menu, menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        p0 as CheckBox
        var isChecked: Boolean = p0.isChecked
        when (p0.id) {
            R.id.playerCheck -> if (isChecked) {
                player="Tek Oyuncu"
                playerCheck1.isChecked = false
            }
            R.id.playerCheck1 -> if (isChecked) {
                player="Çok Oyuncu"
                playerCheck.isChecked = false

            }
        }
        when (p0.id) {
            R.id.hard1 -> if (isChecked) {
                hard2.isChecked = false
                hard3.isChecked = false
            }
            R.id.hard2 -> if (isChecked) {
                hard1.isChecked = false
                hard3.isChecked = false
            }
            R.id.hard3 -> if (isChecked) {
                hard1.isChecked = false
                hard2.isChecked = false
            }
        }
        if((hard1.isChecked || hard2.isChecked) || hard3.isChecked){
            if(hard1.isChecked){
                level="2"
                cardC=1
                cardD=0
            }
            else if(hard2.isChecked){
                level="4"
                cardC=2
                cardD=2
            }
            else{
                level="6"
                cardC=5
                cardD=4
            }
            upload()
        }

    }

    fun startClicked(){
        if(!playerCheck.isChecked && !playerCheck1.isChecked){
            Toast.makeText(this, "Please select a player type!", Toast.LENGTH_LONG).show()
        }
        else if((!hard1.isChecked && !hard2.isChecked) && !hard3.isChecked) {
            Toast.makeText(this, "Please select a level!", Toast.LENGTH_LONG).show()
        }else{
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("level",level)
            intent.putExtra("player",player)
        startActivity(intent)}
        courseList.clear()
        println(tempList)
        courseList.addAll(tempList.shuffled())

    }

    private fun upload(){
        var index:Int = 0
        var index1:Int = 0
        var index2:Int = 0
        var index3:Int = 0

        db.collection("Gryfindor").addSnapshotListener { value, error ->
            if (value != null) {
                if (!value.isEmpty) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val documents = value.documents
                        for (document in documents) {
                            if (index < cardC) {
                                val point = document.get("point").toString().toInt()*2
                                val url = document.get("url").toString()
                                val bitMap = Picasso.get().load(url).get()
                                val id = document.id

                                tempList.add(CardRVModal(point, false, id, bitMap,R.drawable.card))
                                tempList.add(CardRVModal(point, false, id, bitMap, R.drawable.card))
                            } else
                                break
                            index++
                        }
                        runOnUiThread {
                            if(check==true){
                                if(check1==true){
                                    binding.button4?.isEnabled = true
                                }
                            }else{
                                check=true
                            }
                        }
                    }
                }
            }
        }
        db.collection("Slytherin").addSnapshotListener { value, error ->
            if(value != null){
                if(!value.isEmpty){
                    CoroutineScope(Dispatchers.IO).launch {
                        val documents = value.documents
                        for(document in documents){
                            if(index1 < cardC){
                                val point  = document.get("point").toString().toInt()*2
                                val url = document.get("url").toString()
                                val bitMap = Picasso.get().load(url).get()
                                val id = document.id

                                tempList.add(CardRVModal(point,false,id,bitMap,R.drawable.card))
                                tempList.add(CardRVModal(point,false,id,bitMap,R.drawable.card))
                            }
                            else
                                break
                            index1++
                        }
                        runOnUiThread {
                            if(check==true){
                                if(check1==true){
                                    binding.button4?.isEnabled = true
                                }
                            }else{
                                check=true
                            }
                        }
                    }
                }
            }
        }
        if(cardC>1){
            db.collection("Ravenclaw").addSnapshotListener { value, error ->
                if(value != null){
                    if(!value.isEmpty){
                        CoroutineScope(Dispatchers.IO).launch {
                            val documents = value.documents
                            for(document in documents){
                                if(index2 < cardD){
                                    val point  = document.get("point").toString().toInt()
                                    val url = document.get("url").toString()
                                    val bitMap = Picasso.get().load(url).get()
                                    val id = document.id

                                    tempList.add(CardRVModal(point,false,id,bitMap,R.drawable.card))
                                    tempList.add(CardRVModal(point,false,id,bitMap,R.drawable.card))
                                }
                                else
                                    break
                                index2++
                            }
                            runOnUiThread {
                                if(check1==true){
                                    if(check==true){
                                        binding.button4?.isEnabled = true
                                    }
                                }else{
                                    check1=true
                                }
                            }
                        }
                    }
                }
            }
            db.collection("Hufflepuf").addSnapshotListener { value, error ->
                if(value != null){
                    if(!value.isEmpty){
                        CoroutineScope(Dispatchers.IO).launch {
                            val documents = value.documents
                            for(document in documents){
                                if(index3 < cardD){
                                    val point  = document.get("point").toString().toInt()
                                    val url = document.get("url").toString()
                                    val bitMap = Picasso.get().load(url).get()
                                    val id = document.id

                                    tempList.add(CardRVModal(point,false,id,bitMap,R.drawable.card))
                                    tempList.add(CardRVModal(point,false,id,bitMap,R.drawable.card))
                                }
                                else
                                    break
                                index3++
                            }
                            runOnUiThread {
                                if(check1==true){
                                    if(check==true){
                                        binding.button4?.isEnabled = true
                                    }
                                }else{
                                    check1=true
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            check1=true
        }


    }
    companion object{
        lateinit var courseList: ArrayList<CardRVModal>
    }
}