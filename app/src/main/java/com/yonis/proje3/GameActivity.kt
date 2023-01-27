package com.yonis.proje3

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gtappdevelopers.kotlingfgproject.Adapter
import com.yonis.proje3.menu.Companion.courseList
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {
    // on below line we are creating variables
    // for our swipe to refresh layout,
    // recycler view, adapter and list.
    private lateinit var db : FirebaseFirestore
    lateinit var mediaPlayer: MediaPlayer
    lateinit var finishPlayer: MediaPlayer
    lateinit var failPLayer: MediaPlayer
    lateinit var congratPlayer: MediaPlayer
    var handler = Handler()
    var runnable=Runnable{ }
    lateinit var cardRV: RecyclerView
    lateinit var adapter: Adapter
    var player:String = ""
    var score:Int=0
    var score1:Int=0
    var control: Boolean = true
    var time = 0L
    var pastTime:Int=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        mediaPlayer= MediaPlayer.create(this,R.raw.song)
        failPLayer= MediaPlayer.create(this, R.raw.fail)
        congratPlayer = MediaPlayer.create(this, R.raw.congrats)
        mediaPlayer.start() // no need to call prepare(); create() does that for you

        player=intent.getStringExtra("player").toString()
        if(player=="Tek Oyuncu"){
            //textView = findViewById(R.id.scoreText1)
            scoreText1.visibility = View.INVISIBLE
             time=45000L
        }else{
            scoreText.setTextColor(ContextCompat.getColor(this,R.color.teal_700))
            scoreText1.setTextColor((ContextCompat.getColor(this,R.color.black)))
              time=60000L
        }


        object : CountDownTimer(time,1000){
            override fun onTick(millisUntilFinished: Long) {
                timeText.text="Time: " + millisUntilFinished/1000
                pastTime++
            }

            override fun onFinish() {
                timeText.text="Time: 0"
                mediaPlayer.stop()
                congratPlayer.stop()
                failPLayer.start()

                handler.removeCallbacks(runnable)


                val alert = AlertDialog.Builder(this@GameActivity)

                alert.setTitle("Game Over")
                alert.setMessage("Restart the Game?")
                alert.setPositiveButton("Yes") {dialog,which->
                    //restart
                    val intent= Intent(this@GameActivity,menu::class.java)
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton("No"){dialog,which->
                    Toast.makeText(this@GameActivity,"Game Over", Toast.LENGTH_LONG).show()
                }
                alert.show()
            }

        }.start()

        db = Firebase.firestore


        var level: String = intent.getStringExtra("level").toString()


        // on below line we are initializing
        // our views with their ids.
        cardRV = findViewById(R.id.idRVCourses)

        // on below line we are creating a variable
        // for our grid layout manager and specifying
        // column count as 2
        val layoutManager = GridLayoutManager(this, level.toInt())

        cardRV.layoutManager = layoutManager

        // on below line we are initializing our adapter
        adapter = Adapter(courseList, this, clickedCard = ::clickedCard)

        // on below line we are setting
        // adapter to our recycler view.
        cardRV.adapter = adapter

        adapter.notifyDataSetChanged()

    }

    private fun clickedCard(index: Int,id: Int,id1:Int){
        congratPlayer = MediaPlayer.create(this, R.raw.congrats)

        if(player=="Tek Oyuncu"){
            if(id == id1){
                score = (score + courseList.get(index).point.toString().toInt()*(pastTime.toDouble()/10)).toInt()
                println(pastTime)
                scoreText.text="Score: "+score
                mediaPlayer.pause()
                congratPlayer.start()
                congratPlayer.setOnCompletionListener {
                    mediaPlayer.start()
                }

                finish()
            }else{
                score = (score - courseList.get(index).point.toString().toInt()*((time/1000-pastTime).toDouble()/10)).toInt()
                scoreText.text="Score: "+score
            }
        }else {
            if(control==true){
                //1. oyuncu score
                scoreText1.setTextColor(ContextCompat.getColor(this,R.color.teal_700))
                scoreText.setTextColor((ContextCompat.getColor(this,R.color.black)))
                if(id == id1){
                    score = score + courseList.get(index).point.toString().toInt()
                    scoreText.text="1st plyr Score: "+score
                    mediaPlayer.pause()
                    congratPlayer.start()
                    congratPlayer.setOnCompletionListener {
                        mediaPlayer.start()
                    }
                    finish()
                }else{
                    score = score - courseList.get(index).point.toString().toInt()
                    scoreText.text="1st plyr Score: "+score
                }
                control=false
            }else{
                //2. oyuncu score
                scoreText.setTextColor(ContextCompat.getColor(this,R.color.teal_700))
                scoreText1.setTextColor((ContextCompat.getColor(this,R.color.black)))

                if(id == id1){
                    score1 = score + courseList.get(index).point.toString().toInt()
                    scoreText1.text="2nd plyr Score: "+score1
                    mediaPlayer.pause()
                    congratPlayer.start()
                    congratPlayer.setOnCompletionListener {
                        mediaPlayer.start()
                    }
                    finish()
                }else{
                    score1 = score - courseList.get(index).point.toString().toInt()
                    scoreText1.text="2nd plyr Score: "+score1
                }
                control=true
            }

        }
    }

    override fun finish(){
        finishPlayer = MediaPlayer.create(this, R.raw.finish)
        var count:Int=0
        for(card in courseList){
            if(card.status==true){
                count++
            }else{
                continue
            }
        }
        if(count== courseList.size){
            timeText.text="Time: 0"
            mediaPlayer.stop()
            congratPlayer.stop()
            finishPlayer.start()

            handler.removeCallbacks(runnable)


            val alert = AlertDialog.Builder(this@GameActivity)

            alert.setTitle("Conguratulations!!!")
            alert.setMessage("Restart the Game?")
            alert.setPositiveButton("Yes") {dialog,which->
                //restart
                val intent= Intent(this@GameActivity,menu::class.java)
                finish()
                startActivity(intent)
            }
            alert.setNegativeButton("No"){dialog,which->
                Toast.makeText(this@GameActivity,"Game Over", Toast.LENGTH_LONG).show()
            }
            alert.show()
        }
    }
}
