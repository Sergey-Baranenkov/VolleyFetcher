package com.example.volleyproject

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MainActivity : AppCompatActivity() {
    private val url = "http://date.jsontest.com"
    private val method = Request.Method.GET

    private var mRequestQueue : RequestQueue? = null
    private var dateTextView: TextView? = null
    private var millisecondsTextView: TextView? = null
    private var timeTextView: TextView? = null
    private var fetchButton: Button? = null
    private var errorTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRequestQueue = Volley.newRequestQueue(this);

        dateTextView = findViewById(R.id.dateTextView);
        millisecondsTextView = findViewById(R.id.millisecondsTextView);
        timeTextView = findViewById(R.id.timeTextView)
        errorTextView = findViewById(R.id.errorTextView)
        fetchButton = findViewById(R.id.fetchButton)
        fetchButton!!.setOnClickListener{
            fetchDateTime(url, method)
        }

        updateValues("", "", "")

        fetchDateTime(url, method)
    }

    private fun fetchDateTime(url: String, method: Int){
        val request = JsonObjectRequest(
            method,
            url,
            null,
            { response ->
                try {
                    val date = response.getString("date").toString()
                    val ms = response.getLong("milliseconds_since_epoch").toString()
                    val time = response.getString("time").toString()
                    updateValues(date, ms, time)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error: VolleyError ->
                if (error is NoConnectionError) {
                        showError("Невозможно получить информацию с сервера")
                    } else {
                        error.printStackTrace()
                    }
            }
        mRequestQueue!!.add(request)
    }

    private fun updateValues(date: String, ms: String, time: String) {
        dateTextView!!.text = getString(R.string.date, date)
        millisecondsTextView!!.text = getString(R.string.milliseconds, ms)
        timeTextView!!.text = getString(R.string.time, time)
        errorTextView!!.text = ""
    }

    private fun showError(message: String) {
        errorTextView!!.text = "Ошибка: $message"
    }
}
