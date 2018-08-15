package com.petsadoption.tal.petsadoption.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsadoption.tal.petsadoption.Activity.PetContentActivity;
import com.petsadoption.tal.petsadoption.Fragment.NewPetsFragment;
import com.petsadoption.tal.petsadoption.Objects.Pet;
import com.petsadoption.tal.petsadoption.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tal on 3/31/2017.
 */

public class NewPetsAdapter extends RecyclerView.Adapter<NewPetsAdapter.myViewHolder> {

    private List<Pet> petList;
    Context context;


    public NewPetsAdapter(Context context,List<Pet>petList){
        this.petList = petList;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout,parent,false));
    }



    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {

       final Pet model = petList.get(position);

        holder.name.setText(model.getName());
        holder.age.setText(model.getAge());
        holder.type.setText(model.getType());
        Glide.with(context).load(model.getUriKey()).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.petImageRv.setImageBitmap(resource);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PetContentActivity.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("age",model.getAge());
                intent.putExtra("type",model.getType());
                intent.putExtra("city",model.getPetCity());
                intent.putExtra("info",model.getPetMoreInfo());
                intent.putExtra("image",model.getUriKey());
                intent.putExtra("fullName",model.getFullName());
                intent.putExtra("phoneNumber",model.getPhoneNumber());

                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return petList.size();
    }


    class  myViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView type;
        ImageView petImageRv;


        public myViewHolder(View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            type = itemView.findViewById(R.id.type);
            petImageRv = itemView.findViewById(R.id.petImageRv);

        }

    }


}

