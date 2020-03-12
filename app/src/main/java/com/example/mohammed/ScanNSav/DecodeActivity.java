package com.example.mohammed.ScanNSav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class DecodeActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
        private ZBarScannerView mScannerView;

        @Override
        public void onCreate(Bundle state) {
                super.onCreate(state);
                mScannerView = new ZBarScannerView(this);    // Programmatically initialize the scanner view
                setContentView(mScannerView);                // Set the scanner view as the content view
        }

        @Override
        public void onResume() {
                super.onResume();
                mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();          // Start camera on resume
        }

        @Override
        public void onPause() {
                super.onPause();
                mScannerView.stopCamera();           // Stop camera on pause
        }

        @Override
        public void handleResult(Result rawResult) {
                // Do something with the result here
                Log.v("mohammed", rawResult.getContents()); // Prints scan results
                Log.v("mohammed", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

                match(rawResult.getContents());
               // match(rawResult.getContents());
                // If you would like to resume scanning, call this method below:
                mScannerView.resumeCameraPreview(this);
        }


        public void match(final String code) {

                mScannerView.stopCamera();

                final android.app.AlertDialog loading=new ProgressDialog(DecodeActivity.this);
                loading.setMessage("Checking...");
                loading.show();

                Map<String,String> params=new Hashtable<String,String>();
                params.put("barcode",code);

                CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://softsolutions.erstechno.com/ProductScanner/getProduct.php",params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                                loading.dismiss();


                                parseData(response);
                        }

                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),"Server not responding try again", Toast.LENGTH_LONG).show();

                        }
                });




                jsonRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                                return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                                return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                });
                MySingleton.getInstance(this).addToRequestQueue(jsonRequest);    }

        private void parseData(JSONObject response) {
                JSONArray students = null;
                try {
                        students= response.getJSONArray("data");
                }catch (JSONException e){}

                assert students != null;


                if(students.length()==0)
                {
                        Toast.makeText(this, "Not found Try again", Toast.LENGTH_SHORT).show();
                        finish();
                }
                for (int i = 0; i <students.length(); i++) {

                        try {
                                JSONObject student = students.getJSONObject(i);

                                {

                                        Intent j=new Intent(DecodeActivity.this,DetailActivity.class);
                                        j.putExtra("name",student.getString("name"));
                                        j.putExtra("barcode",student.getString("barcode"));
                                        j.putExtra("img",student.getString("pro_img"));
                                        j.putExtra("longi",getIntent().getStringExtra("longi"));
                                        j.putExtra("lati",getIntent().getStringExtra("lati"));

                                        startActivity(j);
                                }





                        } catch (JSONException e) {
                        }

                }

                ///////////
        }
}