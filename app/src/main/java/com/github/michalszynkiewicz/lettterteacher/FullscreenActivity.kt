package com.github.michalszynkiewicz.lettterteacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.random.Random

// TODO: sometimes letters make the buttons too wide
// TODO: small letters
/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private lateinit var fullscreenContent: TextView

    private lateinit var tts: TextToSpeech

    private var correctAnswer = 1

    private var attemptCount = 0
    private var successCount = 0

    val buttons = arrayOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4);

    val letters = "abcdefghijklmnoprstuwz"
    val images = arrayOf(
        R.drawable.arbuz, R.drawable.bulka, R.drawable.cytryna, R.drawable.drzewo,
        R.drawable.ekran, R.drawable.foka, R.drawable.golab, R.drawable.hak, R.drawable.igly,
        R.drawable.jajo, R.drawable.kielbasa, R.drawable.lody, R.drawable.mis, R.drawable.noz,
        R.drawable.okno, R.drawable.pomidor, R.drawable.rower, R.drawable.sowa, R.drawable.trabka,
        R.drawable.usta, R.drawable.waga, R.drawable.zebra
    )
    
    val examples = arrayOf(
        "arbuz", "bułka", "cytryna", "drzewo",
        "ekran", "foka", "gołąb", "hak", "igły",
        "jajo", "kiełbasa", "lody", "miś", "nóż",
        "okno", "pomidor", "rower", "sowa", "trąbka",
        "usta", "waga", "zebra"
    )

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        initializeButton(0, R.id.button1)
        initializeButton(1, R.id.button2)
        initializeButton(2, R.id.button3)
        initializeButton(3, R.id.button4)

        tts = TextToSpeech(applicationContext) {
            if (it != TextToSpeech.ERROR) {
                tts.language = Locale.forLanguageTag("pl")
                tts.setPitch(1.3f)
                tts.setSpeechRate(0.6f)
                initNewTask()
            } else {
                Toast.makeText(this, "Usługa czytania tekstu jest niedostępna", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        tts.stop();
        tts.shutdown();
    }

    @SuppressLint("SetTextI18n")
    private fun initializeButton(number: Int, buttonId: Int) {
        val clickButton: Button = findViewById(buttonId);
        clickButton.setOnClickListener {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            val success = correctAnswer == number;
            clickButton.setBackgroundColor(clickButton.context.getColor(if (success) R.color.green else R.color.red))
            attemptCount++;
            successCount += if (success) 1 else 0;
            val status = findViewById<TextView>(R.id.status)
            status.setBackgroundColor(getColor(R.color.black))
            status.text = """Dobrze $successCount z $attemptCount"""

            var timeToNext : Long = 2000
            if (success) {
                tts.speak("Świetnie!", TextToSpeech.QUEUE_ADD, null);
            } else {
                val properButton = findViewById<Button>(buttons[correctAnswer])
                properButton.setBackgroundColor(getColor(R.color.light_blue_600))
                tts.speak("To, niestety, nie jest dobra odpowiedź!", TextToSpeech.QUEUE_ADD, null);
                timeToNext = 4000
            }

            Handler(Looper.getMainLooper()).postDelayed({
                initNewTask()
            }, timeToNext)
        }
    }

    private fun initNewTask() {
        val randomLetter = Random.nextInt(letters.length)
        val correctLetter = letters[randomLetter]

        val image = findViewById<ImageView>(R.id.task)
        image.setImageResource(images[randomLetter])
        image.setBackgroundColor(getColor(R.color.black))


        correctAnswer = Random.nextInt(3)

        val wrongLetters : Array<Char?> = differentLetters(correctLetter)
        var wrongLetterIdx = 0
        for (i in 0..3) {
            val button: Button = findViewById(buttons[i])
            button.setBackgroundColor(button.context.getColor(R.color.beige))
            if (i == correctAnswer) {
                button.text = """$correctLetter"""
            } else {
                button.text = """${wrongLetters[wrongLetterIdx++]}"""
            }
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        tts.speak("""$correctLetter jak ${examples[randomLetter]}""", TextToSpeech.QUEUE_ADD, null)
    }

    private fun differentLetters(correctLetter: Char): Array<Char?> {
        val usedLetters = mutableSetOf(correctLetter)
        val result: Array<Char?> = arrayOfNulls(3)

        for (i in result.indices) {
            while (true) {
                val letter = letters[Random.nextInt(letters.length)]
                if (!usedLetters.contains(letter)) {
                    usedLetters.add(letter)
                    result[i] = letter
                    break
                }
            }
        }

        return result
    }

}