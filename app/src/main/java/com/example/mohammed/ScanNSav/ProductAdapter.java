package com.example.mohammed.ScanNSav;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context mContext;
    String url,longi,lati;
    List<Items> dataList;

    public ProductAdapter(List<Items> dataList, Context mContext,String longi,String lati)
    {
        this.dataList=dataList;
        this.mContext=mContext;
        this.longi=longi;
        this.lati=lati;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_items,parent,false);

        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        Items list = dataList.get(position);

        holder.price.setText("£"+list.getPrice());
        try
        {
            double dis=distance(Double.valueOf(lati),Double.valueOf(longi),Double.valueOf(list.getLati()),Double.valueOf(list.getLongi()));
            Log.e("val",""+dis);
            float km= (float) (dis * 1.609344);
            double roundOff = (double) Math.round(km*100)/100;
            holder.direction.setText(String.valueOf(roundOff)+" Km");
        }
        catch (Exception e){
            e.printStackTrace();

            Toast.makeText(mContext, "longi:"+longi+" lati:"+lati, Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, "Server response null"+e, Toast.LENGTH_SHORT).show();
        }


        Glide
                .with(mContext)
                .load(list.getStore())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.DATA))
                .into(holder.im);







    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        public TextView price;
        public TextView direction;
        public ImageView im;
        public ViewHolder(View itemView) {
            super(itemView);
            price =  itemView.findViewById(R.id.price);
            direction =  itemView.findViewById(R.id.direction);
            im=itemView.findViewById(R.id.img);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            Intent i=new Intent(mContext,MapActivity.class);
            i.putExtra("longi",dataList.get(getAdapterPosition()).getLongi());
            i.putExtra("lati",dataList.get(getAdapterPosition()).getLati());
            mContext.startActivity(i);

        }
    }


    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }

}

