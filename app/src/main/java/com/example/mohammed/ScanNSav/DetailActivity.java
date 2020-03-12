package com.example.mohammed.ScanNSav;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    ImageView im,plus;
    TextView name,bar;
    List<Items> listOfDatas=new ArrayList<>();
    List<Double> list=new ArrayList<>();
    DBHelper dbHelper;
    ProductAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView vieeinfo,facts,kcal,kj,carbo,sugar,salt;
    LinearLayout stores;
    int val=0;
    ScrollView nut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        dbHelper=new DBHelper(this);

        stores=findViewById(R.id.stores);
        nut=findViewById(R.id.nt);

        vieeinfo=findViewById(R.id.view);
        facts=findViewById(R.id.facts);
        kcal=findViewById(R.id.kcal);
        kj=findViewById(R.id.kj);
        carbo=findViewById(R.id.carbo);
        sugar=findViewById(R.id.sugar);
        salt=findViewById(R.id.salt);

        im=findViewById(R.id.main);
        name=findViewById(R.id.name);
        bar=findViewById(R.id.bar);
        plus=findViewById(R.id.pluss);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<listOfDatas.size();i++)
                {
                    list.add(listOfDatas.get(i).getPrice());
                }
                if(list!=null)
                {
                    int indexOfMinimum = list.indexOf(Collections.min(list));

                    if(dbHelper.ValidateRecord(getIntent().getStringExtra("barcode")))
                    {
                        Toast.makeText(DetailActivity.this, "Already in Database", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dbHelper.insertNote(getIntent().getStringExtra("barcode"),getIntent().getStringExtra("name"),
                                String.valueOf(listOfDatas.get(indexOfMinimum).getPrice()),listOfDatas.get(indexOfMinimum).getStore());

                        Toast.makeText(DetailActivity.this, ""+dbHelper.numberOfRows(), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        name.setText("Name: "+getIntent().getStringExtra("name"));
        bar.setText("Barcode: "+getIntent().getStringExtra("barcode"));
        facts.setText(getIntent().getStringExtra("facts"));
        kcal.setText(getIntent().getStringExtra("kcal"));
        kj.setText(getIntent().getStringExtra("kj"));
        carbo.setText(getIntent().getStringExtra("carbo"));
        sugar.setText(getIntent().getStringExtra("sugar"));
        salt.setText(getIntent().getStringExtra("salt"));




        Glide
                .with(this)
                .load(getIntent().getStringExtra("img"))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(im);

        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        vieeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(val==0)
                {
                    val=1;
                    vieeinfo.setText("Hide product Information");
                    stores.setVisibility(View.GONE);
                    nut.setVisibility(View.VISIBLE);
                }
                else
                {
                    val=0;
                    vieeinfo.setText("View product Information");
                    stores.setVisibility(View.VISIBLE);
                    nut.setVisibility(View.GONE);
                }

            }
        });



        match(getIntent().getStringExtra("barcode"));

    }
//////////////done

    @Override
    protected void onResume() {
        super.onResume();

        if(val==1)
        {
            vieeinfo.setText("Hide product Information");
            stores.setVisibility(View.GONE);
            nut.setVisibility(View.VISIBLE);
        }
        else
        {
            vieeinfo.setText("View product Information");
            stores.setVisibility(View.VISIBLE);
            nut.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }

    public void match(final String code) {


        final android.app.AlertDialog loading=new ProgressDialog(DetailActivity.this);
        loading.setMessage("Checking...");
        loading.show();

        Map<String,String> params=new Hashtable<String,String>();
        params.put("barcode",code);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://softsolutions.erstechno.com/ProductScanner/getDetail.php",params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                loading.dismiss();


                parseData(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_LONG).show();

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


        for (int i = 0; i <students.length(); i++) {

            try {
                JSONObject student = students.getJSONObject(i);

                {

                    Items items=new Items();

                    items.setStore(student.getString("storeimg"));
                    items.setPrice(Double.valueOf(student.getString("price")));
                    items.setLongi(student.getString("longitude"));
                    items.setLati(student.getString("latitude"));

                    listOfDatas.add(items);
                }


                adapter=new ProductAdapter(listOfDatas,DetailActivity.this,getIntent().getStringExtra("longi"),getIntent().getStringExtra("lati"));
                recyclerView.setAdapter(adapter);



            } catch (JSONException e) {
            }

        }

        ///////////
    }






}
