package com.solution.alnahar.nearbyplaceskotlin

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.solution.alnahar.nearbyplaceskotlin.common.Common
import com.solution.alnahar.nearbyplaceskotlin.model.PlaceDetail
import com.solution.alnahar.nearbyplaceskotlin.remote.IGoogleApiService
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import retrofit2.Call
import retrofit2.Response

class ViewPlaceActivity : AppCompatActivity() {


    lateinit var mService: IGoogleApiService

    var mPlace: PlaceDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)

        // init service
        mService = Common.googleApiService
        placeName.text = ""
        placeAddress.text = ""
        placeOpeningHour.text = ""

        btn_show_on_map.setOnClickListener(View.OnClickListener {

            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mPlace!!.result!!.url))
            startActivity(mapIntent)


        })

        // load photo of place
        if (Common.currentResult!!.photos != null && Common!!.currentResult!!.photos!!.size > 0) {

            Picasso.get()
                    .load(getPhotosPlace(Common!!.currentResult!!.photos!![0].photo_reference, 1000))
                    .into(place_image)


        }

        // load rating
        if (Common!!.currentResult!!.rating != null)
            placeRatingBar.rating = Common!!.currentResult!!.rating.toFloat()
        else
            placeRatingBar.visibility = View.GONE



        // load opeinig hour
        if (Common!!.currentResult!!.opening_hours!=null)
            placeOpeningHour.text="Open now: "+Common!!.currentResult!!.opening_hours!!.open_now
        else
            placeOpeningHour.visibility=View.GONE

        // now use service to fetch addres  and  name

        mService.getPlaceDetail(getPlaceDetailURL(Common!!.currentResult!!.place_id))
                .enqueue(object :retrofit2.Callback<PlaceDetail>{
                    override fun onFailure(call: Call<PlaceDetail>?, t: Throwable?) {

                        Toast.makeText(baseContext,""+t!!.message,Toast.LENGTH_LONG).show()
                        Log.e("url",t.message)

                    }

                    override fun onResponse(call: Call<PlaceDetail>?, response: Response<PlaceDetail>?) {

                        mPlace=response!!.body()

                        placeName.text=mPlace!!.result!!.name
                        placeAddress.text=mPlace!!.result!!.formatted_address


                    }


                })




    }

    private fun getPlaceDetailURL(place_id: String?): String {

// google places deatil api

        val  url=StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?placeid=$place_id")
        url.append("&key=AIzaSyCuxqtUJ8ZHTZLv-IIumRR15Xa3zWN2gA0")

        return url.toString()

    }

    private fun getPhotosPlace(photo_reference: String?, maxWidth: Int):String {


        // google photo detail api
        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY


        val  url=StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&photoreference=$photo_reference")
        url.append("&key=AIzaSyCuxqtUJ8ZHTZLv-IIumRR15Xa3zWN2gA0")

        return url.toString()
    }
}
