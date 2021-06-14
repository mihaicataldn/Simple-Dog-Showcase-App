package com.example.dogshowcaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.dogshowcaseapp.Adapter.DogsAdapter
import com.example.dogshowcaseapp.Model.DogsApi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val imageList = ArrayList<DogsApi>()

    private lateinit var dogsRV: RecyclerView
    private lateinit var dogNameText: EditText
    private  lateinit var searchBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //view
        dogsRV = findViewById(R.id.dogRecyclerView)
        dogNameText = findViewById(R.id.dogsNameET)
        searchBtn = findViewById(R.id.searchBtn)

        //setting up staggered ayout manager
        dogsRV.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        searchBtn.setOnClickListener{

            //getting text from edit txt

            var name = dogNameText.text.toString()

            //call dog search function

            searchDogs(name)


        }
        allDogs()
    }

    private fun allDogs() {

        imageList.clear()
        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://dog.ceo/api/breeds/image/random/50")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {
                    val result = JSONObject(response.toString())
                    val image  = result.getJSONArray("message")

                    //iterate each item in array to fetch items

                    for (i in 0 until image.length()){
                        val list = image.get(i)
                        imageList.add(
                            DogsApi(list.toString())
                        )
                    }
                    dogsRV.adapter = DogsAdapter(this@MainActivity, imageList)
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(this@MainActivity, "ERROR: "+ anError, Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun searchDogs(name: String) {

        imageList.clear()

        AndroidNetworking.initialize(this)
        //call the url to get the image
        //pass name parameter in the url !concate string!
        AndroidNetworking.get("https://dog.ceo/api/breed/$name/images")
            .setPriority(Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener {
                override fun onResponse(response: String?) {

                    val result = JSONObject(response.toString())
                    val image  = result.getJSONArray("message")

                    //iterate each item in array to fetch items

                    for (i in 0 until image.length()){
                        val list = image.get(i)
                        imageList.add(
                            DogsApi(list.toString())
                        )
                    }
                    dogsRV.adapter = DogsAdapter(this@MainActivity, imageList)
                }

                override fun onError(anError: ANError?) {
                    Toast.makeText(this@MainActivity, "ERROR: "+ anError, Toast.LENGTH_SHORT).show()

                }

            })
    }
}