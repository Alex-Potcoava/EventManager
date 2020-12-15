package com.example.ej_05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<String> data;
    private LayoutInflater inflater;
    OnButtonClickListener onButtonClickListener;

    public CustomAdapter(OnButtonClickListener onButtonClickListener, ArrayList<String> data) {
        this.data = data;
        Collections.reverse(this.data);
        this.onButtonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recycler_view, parent, false);
        return new ViewHolder(view, onButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView buttonRemove;
        OnButtonClickListener onButtonClickListener;

        public ViewHolder(@NonNull View itemView, final OnButtonClickListener onButtonClickListener) {
            super(itemView);
            this.onButtonClickListener = onButtonClickListener;
            title = itemView.findViewById(R.id.titleEvent);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onButtonClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onButtonClickListener.addMarkerEvent(position);
                        }
                    }
                }
            });
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onButtonClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onButtonClickListener.removeEvent(position);
                            SaveList.writeListInPref(itemView.getContext(), data);
                        }
                    }
                }
            });
        }
    }
    public interface OnButtonClickListener{
        void removeEvent(int position);
        void addMarkerEvent(int position);
    }
}

