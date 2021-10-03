package com.general.quizapp


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.general.quizapp.logun.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.difficultydialog.*
import kotlinx.android.synthetic.main.difficultydialog.view.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.android.synthetic.main.settingslayout.*
import kotlinx.android.synthetic.main.settingslayout.view.*


class MainActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var intentmain:Intent
    var caterygnum:Int?=0
    lateinit var prefence: MyPrefence
    lateinit var mediaplayer:MediaPlayer
    lateinit var vibrator: Vibrator
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intent
        prefence= MyPrefence(this@MainActivity)
        firebaseAuth=FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        clickcatorgy()
         openclosedrawerlayout()
    }

    private fun openclosedrawerlayout() {
        nav_view.bringToFront()
        val toggle=ActionBarDrawerToggle(this, drawer_layout, R.string.navigation_open_drawer, R.string.navigation_close_drawer)
        userId=firebaseAuth.currentUser?.uid.toString()
        firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener {result->
                    userdataname.text=result.getString("username").toString()
                    profilmain.setBackgroundColor(Color.parseColor(result.getString("color")))
                    profilmain.text=result.getString("firstLetter")
                    level.text=result.getString("level")?.substring(0, result.getString("level")?.indexOf(".")!!).toString()
                }


        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        drawerlayout.setOnClickListener{
            if (!drawer_layout.isDrawerOpen(GravityCompat.START))
                drawer_layout.openDrawer(GravityCompat.START)
            else
                drawer_layout.closeDrawer(GravityCompat.END)
        }

    }

    override fun onBackPressed(){
        val main= Intent(Intent.ACTION_MAIN)
        main.addCategory(Intent.CATEGORY_HOME)
        main.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(main)

    }
    fun soundcheck(){
        if (prefence.getBoolen("sound")==true){
            mediaplayer=MediaPlayer.create(this,R.raw.soundbutton)
            mediaplayer.start()
        }
    }

    fun vibrationcheck(){
        @RequiresApi(Build.VERSION_CODES.O)
        if (prefence.getBoolen("vibration")==true){
            vibrator=getSystemService(VIBRATOR_SERVICE) as Vibrator
            var vibrationEffect=VibrationEffect.createOneShot(300,VibrationEffect.PARCELABLE_WRITE_RETURN_VALUE)
            vibrator.vibrate(vibrationEffect)
        }
    }

    private fun showdialog() {
        val inflater=LayoutInflater.from(this@MainActivity)
        var view=inflater.inflate(R.layout.difficultydialog, null)
        intentmain.putExtra("time", "45000")
        if (caterygnum!=0){
            view.imagemedium.visibility=View.VISIBLE
            view.easy.visibility=View.GONE
            intentmain.putExtra("difficulty", "medium")
        }
        else {
            intentmain.putExtra("difficulty", "easy")
            view.esayimage.visibility = View.VISIBLE
            view.easy.visibility = View.VISIBLE
        }


        view.withtime.setOnClickListener {
            view.selectwithtime.visibility= View.VISIBLE
            view.selectnotime.visibility= View.INVISIBLE
            view.time.visibility=View.VISIBLE
            soundcheck()
        }
        view.withouttime.setOnClickListener {
            intentmain.putExtra("time", "0")
            view.selectwithtime.visibility= View.INVISIBLE
            view.selectnotime.visibility= View.VISIBLE
            view.time.visibility=View.GONE
            soundcheck()
        }
        view.easy.setOnClickListener {
            intentmain.putExtra("difficulty", "easy")
            view.imagemedium.visibility= View.INVISIBLE
            view.hardimage.visibility= View.INVISIBLE
            view.esayimage.visibility=View.VISIBLE
            soundcheck()
        }
        view.meduim.setOnClickListener {
            intentmain.putExtra("difficulty", "medium")
            view.imagemedium.visibility= View.VISIBLE
            view.hardimage.visibility= View.INVISIBLE
            view.esayimage.visibility=View.INVISIBLE
            soundcheck()
        }
        view.hard.setOnClickListener {
            intentmain.putExtra("difficulty", "hard")
            view.imagemedium.visibility= View.INVISIBLE
            view.hardimage.visibility= View.VISIBLE
            view.esayimage.visibility=View.INVISIBLE
            soundcheck()
        }
        view.thirtysecond.setOnClickListener {
            intentmain.putExtra("time", "30000")
            view.selectedthirtysecond.visibility=View.VISIBLE
            view.selectedviertelsecond.visibility=View.INVISIBLE
            view.selectedsixtysecond.visibility=View.INVISIBLE
            soundcheck()
        }
        view.viertelsecond.setOnClickListener {
            intentmain.putExtra("time", "45000")
            view.selectedthirtysecond.visibility=View.INVISIBLE
            view.selectedviertelsecond.visibility=View.VISIBLE
            view.selectedsixtysecond.visibility=View.INVISIBLE
            soundcheck()
        }
        view.sixtysecond.setOnClickListener {
            intentmain.putExtra("time", "60000")
            view.selectedthirtysecond.visibility=View.INVISIBLE
            view.selectedviertelsecond.visibility=View.INVISIBLE
            view.selectedsixtysecond.visibility=View.VISIBLE
            soundcheck()

        }
        val alertDialog: AlertDialog = AlertDialog.Builder(this@MainActivity)
                .setView(view)
                .create()
        view.start.setOnClickListener {
            soundcheck()
            startActivity(intentmain)
        }


        alertDialog.show()

    }

    private fun clickcatorgy() {
        intentmain= Intent(this, QuestionSActivity::class.java)
        videogames.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "15")
            showdialog()

        }
        math.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=19
            intentmain.putExtra("catorgy", caterygnum.toString())
            showdialog()

        }
        books.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "10")
            showdialog()

        }
       films.setOnClickListener {
           soundcheck()
           vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "11")
            showdialog()

        }
        nature.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "17")
            showdialog()

        }
        computers.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "18")
            showdialog()

        }
       politics.setOnClickListener {
           soundcheck()
           vibrationcheck()
           caterygnum=24
            intentmain.putExtra("catorgy", caterygnum.toString())
            showdialog()

        }
        musics.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "12")
            showdialog()

        }
        animals.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "27")
            showdialog()

        }
        history.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "23")
            showdialog()

        }
        mythology.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "20")
            showdialog()

        }
        vehicles.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "28")
            showdialog()

        }
        sport.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "21")
            showdialog()

        }
        boardsgame.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "16")
            showdialog()

        }
        television.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "14")
            showdialog()

        }
        geography.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "22")
            showdialog()

        }
        general.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "9")
            showdialog()

        }
        japaneseanime.setOnClickListener {
            soundcheck()
            vibrationcheck()
            caterygnum=0
            intentmain.putExtra("catorgy", "31")
            showdialog()

        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

         when(item.itemId){
             R.id.settings -> {

                 val inflater = LayoutInflater.from(this@MainActivity)
                 val view = inflater.inflate(R.layout.settingslayout, null)
                 val alertDialog = AlertDialog.Builder(this@MainActivity)
                         .setView(view)
                         .create()
                 alertDialog.show()
                 view.sound.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                         if (isChecked) {
                            prefence.setBoolen("sound",true)
                             view.sound.isChecked = true

                         } else {
                             prefence.setBoolen("sound",false)
                             view.sound.isChecked = false


                         }
                     }
                 })
                 view.sound.isChecked=prefence.getBoolen("sound")
                 view.vibration.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener{vibrationview,isChecked->
                     if (isChecked){
                         prefence.setBoolen("vibration",true)
                         view.vibration.isChecked=true
                     }else{
                         prefence.setBoolen("vibration",false)
                         view.vibration.isChecked=false
                     }
                 })
                 view.vibration.isChecked=prefence.getBoolen("vibration")
                 view.ok.setOnClickListener {
                     alertDialog.cancel()
                 }

             }
             R.id.profile->{
                 var intent=Intent(this,ProfilActivity::class.java)
                 startActivity(intent)
             }
             R.id.logout->{
                 var intent=Intent(this,LoginActivity::class.java)
                 startActivity(intent)
                 prefence.setString("login","logout")
                 finish()

             }
             R.id.statistics->{
                 var intent2=Intent(this,StatisticActivity::class.java)
                 startActivity(intent2)
             }
             R.id.leader->{
                 var intent=Intent(this,LeaderboardActivity::class.java)
                 startActivity(intent)
             }
             R.id.instruction->{
                 var intent=Intent(this,Instruction::class.java)
                 startActivity(intent)
             }


         }
        return true
    }

}