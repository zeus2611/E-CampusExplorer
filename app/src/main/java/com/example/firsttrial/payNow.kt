package com.example.firsttrial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.firsttrial.Fragment.FirsttFragment
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class payNow : AppCompatActivity(), PaymentResultListener {

    lateinit var pay: Button
    lateinit var card: CardView
    lateinit var success: TextView
    lateinit var failed: TextView
    // **********
   // lateinit var distance : String
//************
    var distance: Double = 0.0
    var distance1: Double = 0.0

    var payt:Double = 0.0
    val ratePerKilometer = 5
    val minimumPaymentAmount = 100 // 1 INR in paise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_now)

        pay =findViewById(R.id.paym)
        pay.setOnClickListener {
            makePayment()
        }
        distance = intent.getDoubleExtra("distance",0.0)   //*********
        distance1 = intent.getDoubleExtra("distance1",0.0)   //*********



        // Find the TextView in the layout to display the distance
        val distanceTextView = findViewById<TextView>(R.id.totalDistanceTextView)
//        val CostTextView = findViewById<TextView>(R.id.totalcost)

        // Set the distance value to the TextView
        distanceTextView.text = "Total Distance: %.2f km".format(distance)
        // Set a minimum payment amount of 1 INR (100 paise)

        payt = distance * ratePerKilometer * 100

        if (payt < minimumPaymentAmount) {
            payt = minimumPaymentAmount.toDouble()
            Toast.makeText(this, "We can't accept payment below 1 Rupee, so you have to pay default payment i.e 1 Rupee!", Toast.LENGTH_LONG).show()
        }
//        CostTextView.text = "Total Cost: %.2f Rupees".format(payt)
    }

    private fun makePayment() {

        if (distance == 0.0) {
            Toast.makeText(this, "Distance is 0 km, payment cannot be progressed!", Toast.LENGTH_LONG).show()
            return
        }

        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","EVPAY")
            options.put("description","SUBSCRIBE")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
          //  options.put("Distance", (distance*500).toInt())  //pass amount in currency subunits Rs. * 100
            options.put("amount", (payt).toInt())

            val prefill = JSONObject()
            prefill.put("email","")
            prefill.put("contact","")

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successful $p0 ", Toast.LENGTH_LONG).show()
        pay=findViewById(R.id.paym)
        pay.visibility = View.GONE
        success = findViewById(R.id.success)
        success.visibility= View.VISIBLE

        // Navigate to the first fragment after a delay of 6 seconds
//        Handler().postDelayed({
//            val fragmentManager = supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.r1, FirsttFragment()) // Replace with your first fragment class
//            fragmentTransaction.addToBackStack(null) // Optional: If you want to add the fragment to the back stack
//            fragmentTransaction.commit()
//        }, 6000) // 6 seconds delay (in milliseconds)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Error $p1 ",Toast.LENGTH_LONG).show()
        pay=findViewById(R.id.paym)
        pay.visibility = View.GONE
        failed = findViewById(R.id.failed)
        failed.visibility=View.VISIBLE
    }


}

