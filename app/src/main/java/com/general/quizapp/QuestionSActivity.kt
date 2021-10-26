package com.general.quizapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.general.quizapp.modell.Response
import com.general.quizapp.prefence.MyPrefence
import com.general.quizapp.retrofit.ApiService
import com.general.quizapp.retrofit.RetrofitInstance
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_question_s.*


class QuestionSActivity : AppCompatActivity() {
    var caterogy:String?=null
    var score:Int=0
    var correct:Int=0
    var wrong:Int=0
    var lost:Int=0
    var won:Int=0
    lateinit var intent2:Intent
    lateinit var times:CountDownTimer
    lateinit var animation:Animation
    lateinit var mediaPlayer: MediaPlayer
    lateinit var prefence: MyPrefence
    lateinit var vibratoreffect:VibrationEffect
    lateinit var vibrator:Vibrator
    lateinit var difficulty:String

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_s)
        prefence= MyPrefence(this@QuestionSActivity)
        exit.setOnClickListener{
            onBackPressed()
        }
        progess.setProgressTintList(ColorStateList.valueOf(Color.RED))
        progress.getProgressDrawable().setColorFilter(
                Color.parseColor("#FB8C00"), android.graphics.PorterDuff.Mode.SRC_ATOP)
        var retrofitInstance= RetrofitInstance.getQuizQuestions()?.create(ApiService::class.java)
        var caterogynum:String=intent.getStringExtra("catorgy").toString()
        caterogy=caterogynum
        difficulty=intent.getStringExtra("difficulty").toString()
        retrofitInstance?.quizquestions(12, difficulty, caterogynum.toInt(), "multiple")
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(getObserver())
        setimages()

    }

    override fun onBackPressed() {
        var intentmain:Intent = Intent(baseContext, MainActivity::class.java)
        times.start().cancel()
        startActivity(intentmain)
        super.onBackPressed()
    }
    private fun setimages() {
        if (caterogy.equals("15"))
        question_.setImageResource(R.drawable.video__game)

        if (caterogy.equals("19"))
            question_.setImageResource(R.drawable.iconmath)

        if (caterogy.equals("10"))
            question_.setImageResource(R.drawable.bookicon)

        if (caterogy.equals("11"))
            question_.setImageResource(R.drawable.movieicon)

        if (caterogy.equals("17"))
            question_.setImageResource(R.drawable.natureicon)

        if (caterogy.equals("18"))
            question_.setImageResource(R.drawable.computericon)

        if (caterogy.equals("24"))
            question_.setImageResource(R.drawable.politicsicon)

        if (caterogy.equals("12"))
            question_.setImageResource(R.drawable.musicicon)

        if (caterogy.equals("27"))
            question_.setImageResource(R.drawable.animalicon)

        if (caterogy.equals("23"))
            question_.setImageResource(R.drawable.historyicon)

        if (caterogy.equals("20"))
            question_.setImageResource(R.drawable.mythologyicon)

        if (caterogy.equals("28"))
            question_.setImageResource(R.drawable.vehiclesicons)

        if (caterogy.equals("21"))
            question_.setImageResource(R.drawable.sportsicon)

        if (caterogy.equals("16"))
            question_.setImageResource(R.drawable.boardgamesicon)

        if (caterogy.equals("14"))
            question_.setImageResource(R.drawable.televisonicon)

        if (caterogy.equals("22"))
            question_.setImageResource(R.drawable.geographyicon)

        if (caterogy.equals("31"))
            question_.setImageResource(R.drawable.anime)

        if (caterogy.equals("9"))
            question_.setImageResource(R.drawable.generalquiz)
    }

    fun setuptimer(timess: String) {

        times= object : CountDownTimer(timess.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text = ":${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                intent2 = Intent(baseContext, ResultActivty::class.java)
                intent2.putExtra("score",score.toString())
                intent2.putExtra("correct",correct.toString())
                intent2.putExtra("won",won.toString())
                intent2.putExtra("lost",lost.toString())
                intent2.putExtra("wrong",wrong.toString())
                startActivity(intent2)
            }

        }
        if(!timess.equals("0")) {
            times.start()
            timer.visibility=View.VISIBLE
        }
    }


    private fun getObserver(): Observer<Response> {
        return object : Observer<Response> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Response) {
                var timess: String = intent.getStringExtra("time").toString()
                if (nexte.text.equals("Next")) {
                var variantnumber:Int=0
                var prosess:Int=1
                var variantslist= arrayListOf(t.results[variantnumber].incorrectAnswers[0], t.results[variantnumber].incorrectAnswers[1], t.results[variantnumber].incorrectAnswers[2], t.results[variantnumber].correctAnswer)
                    var correctanswer:String=t.results[variantnumber].correctAnswer
                    ques.text=t.results[variantnumber].question
                    setquestions(variantslist, correctanswer)
                    nexte.setOnClickListener {
                        animation=AnimationUtils.loadAnimation(applicationContext, R.anim.anim)
                        cardview.startAnimation(animation)
                        Handler().postDelayed(Runnable {

                        }, 0)
                        setbacgrounddefault()
                        variantnumber = variantnumber + 1
                        prosess = prosess + 1
                        progress.progress = prosess
                        progress.max = 10
                        frage.text = "Question ${prosess}/10"
                        ques.text = t.results[variantnumber].question
                        var variantslist = arrayListOf(t.results[variantnumber].incorrectAnswers[0], t.results[variantnumber].incorrectAnswers[1], t.results[variantnumber].incorrectAnswers[2], t.results[variantnumber].correctAnswer)
                        var correctanswer:String=t.results[variantnumber].correctAnswer
                        setquestions(variantslist, correctanswer)
                        if (prosess==9)
                            viewbacground.visibility=View.GONE
                        if (prosess == 10) {
                            view2bacground.visibility=View.GONE
                            nexte.text ="See Result"
                            nexte.setOnClickListener {
                                if(!timess.equals("0")){
                                times.start().cancel()
                                }
                               times.onFinish()
                            }



                        }
                    }
                }





                progess.visibility= View.GONE

                    setuptimer(timess)



            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }

        }
}

   fun vibrationcheck(){
       @RequiresApi(Build.VERSION_CODES.O)
       if (prefence.getBoolen("vibration")==true){
       vibrator= getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
       vibratoreffect= VibrationEffect.createOneShot(300,VibrationEffect.PARCELABLE_WRITE_RETURN_VALUE)
       vibrator.vibrate(vibratoreffect)
       }
   }
    fun soundcheck(){
        if (prefence.getBoolen("sound")==true)
            mediaPlayer.start()
    }
    fun checkdifficulty_minusscore() {
        wrong=wrong+1
        if (difficulty == "easy") {
            score = score - 1
            lost=wrong
        }
        if (difficulty == "medium") {
            score = score -2
            lost=wrong*2
        }
        if (difficulty == "hard") {
            score = score - 3
            lost=wrong*3
        }

    }

    fun checkdifficulty_addscore() {
        correct=correct+1
        if (difficulty == "easy") {
            score = score + 1
            won=correct
        }
        if (difficulty == "medium") {
            score = score + 3
            won=correct*3
        }
        if (difficulty == "hard") {
            score = score + 5
            won=correct*5
        }

    }
    private fun setquestions(variantslist: ArrayList<String>, correctanswer: String) {


        var variant1:String
        variant1=variantslist.random()
        option1.text=variant1
        variantslist.remove(variant1)

        var variant2:String
        variant2=variantslist.random()
        option2.text=variant2
        variantslist.remove(variant2)

        var variant3:String
        variant3=variantslist.random()
        option3.text=variant3
        variantslist.remove(variant3)

        var variant4:String
        variant4=variantslist.random()
        option4.text=variant4
        variantslist.remove(variant4)




        option1.setOnClickListener {
            vibrationcheck()
            if(variant1.equals(correctanswer)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.correctanswer)
                soundcheck()
                option1.setBackgroundResource(R.drawable.correct_answer)
                checkdifficulty_addscore()

            }
            if(variant2.equals(correctanswer))
                option2.setBackgroundResource(R.drawable.correct_answer)
            if(variant3.equals(correctanswer))
                option3.setBackgroundResource(R.drawable.correct_answer)
            if(variant4.equals(correctanswer))
                option4.setBackgroundResource(R.drawable.correct_answer)
            if(!variant1.equals(correctanswer)){
                mediaPlayer=MediaPlayer.create(this,R.raw.wronganswer)
                soundcheck()
                option1.setBackgroundResource(R.drawable.wrong_answer)
                checkdifficulty_minusscore()
            }
    cancelclickable()
        }
        option2.setOnClickListener {
          vibrationcheck()
            if(variant1.equals(correctanswer))
                option1.setBackgroundResource(R.drawable.correct_answer)
            if(variant2.equals(correctanswer)){
                mediaPlayer=MediaPlayer.create(this,R.raw.correctanswer)
                soundcheck()
                option2.setBackgroundResource(R.drawable.correct_answer)
                checkdifficulty_addscore()
            }
            if(variant3.equals(correctanswer))
                option3.setBackgroundResource(R.drawable.correct_answer)
            if(variant4.equals(correctanswer))
                option4.setBackgroundResource(R.drawable.correct_answer)
            if(!variant2.equals(correctanswer)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.wronganswer)
                soundcheck()
                option2.setBackgroundResource(R.drawable.wrong_answer)
                checkdifficulty_minusscore()
            }
            cancelclickable()
        }
        option3.setOnClickListener {
           vibrationcheck()
            if(variant1.equals(correctanswer))
                option1.setBackgroundResource(R.drawable.correct_answer)
            if(variant2.equals(correctanswer))
                option2.setBackgroundResource(R.drawable.correct_answer)
            if(variant3.equals(correctanswer)){
                mediaPlayer=MediaPlayer.create(this,R.raw.correctanswer)
                soundcheck()
                option3.setBackgroundResource(R.drawable.correct_answer)
                checkdifficulty_addscore()
            }
            if(variant4.equals(correctanswer))
                option4.setBackgroundResource(R.drawable.correct_answer)
            if(!variant3.equals(correctanswer)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.wronganswer)
                soundcheck()
                option3.setBackgroundResource(R.drawable.wrong_answer)
                checkdifficulty_minusscore()
            }
            cancelclickable()

        }
        option4.setOnClickListener {
             vibrationcheck()
            if(variant1.equals(correctanswer))
                option1.setBackgroundResource(R.drawable.correct_answer)
            if(variant2.equals(correctanswer))
                option2.setBackgroundResource(R.drawable.correct_answer)
            if(variant3.equals(correctanswer))
                option3.setBackgroundResource(R.drawable.correct_answer)
            if(variant4.equals(correctanswer)){
                mediaPlayer=MediaPlayer.create(this,R.raw.correctanswer)
                soundcheck()
                option4.setBackgroundResource(R.drawable.correct_answer)
                checkdifficulty_addscore()
            }

            if (!variant4.equals(correctanswer)){
                mediaPlayer=MediaPlayer.create(this,R.raw.wronganswer)
                soundcheck()
                option4.setBackgroundResource(R.drawable.wrong_answer)
                checkdifficulty_minusscore()
            }

            cancelclickable()
        }


    }
 fun cancelclickable() {
        option2.isClickable=false
        option3.isClickable=false
        option1.isClickable=false
        option4.isClickable=false
    }

    fun setbacgrounddefault(){
        option1.setBackgroundResource(R.drawable.options)
        option2.setBackgroundResource(R.drawable.options)
        option3.setBackgroundResource(R.drawable.options)
        option4.setBackgroundResource(R.drawable.options)
    }


}


