package com.example.mohammed.ScanNSav;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;



public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    private Context mContext;
    List<savedItem> dataList;

    public SavedAdapter(List<savedItem> dataList, Context mContext)
    {
        this.dataList=dataList;
        this.mContext=mContext;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_items2,parent,false);

        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        savedItem list = dataList.get(position);

        holder.price.setText("£"+list.getPrice());
        holder.barcode.setText(list.getBarcode());
        holder.name.setText(list.getName());
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


        public TextView price,name,barcode;
        public ImageView im;
        public ViewHolder(View itemView) {
            super(itemView);
            price =  itemView.findViewById(R.id.price);
            name =  itemView.findViewById(R.id.name);
            barcode=itemView.findViewById(R.id.barcode);
            im=itemView.findViewById(R.id.img);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {



        }
    }




}

