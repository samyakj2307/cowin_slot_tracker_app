package com.samyak.cowin_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PincodeDataAdapter extends RecyclerView.Adapter<PincodeDataAdapter.PincodeDataViewHolder> {

    Context context;

    private ArrayList<PincodeData> pincodeData = new ArrayList<PincodeData>();


    @NonNull
    @Override
    public PincodeDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PincodeDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PincodeDataViewHolder holder, int position) {
        holder.getPincode_text().setText(pincodeData.get(position).getPincode());
        holder.getSlot_tracking_text().setText(pincodeData.get(position).getSlot_tracking());
    }

    @Override
    public int getItemCount() {
        return pincodeData.size();
    }

    public ArrayList<PincodeData> getPincodeData() {
        return pincodeData;
    }

    public void setPincodeData(ArrayList<PincodeData> pincodeDataArrayList) {
        this.pincodeData = pincodeDataArrayList;
        notifyDataSetChanged();
    }

    public class PincodeDataViewHolder extends RecyclerView.ViewHolder {
        TextView pincode_text;
        TextView slot_tracking_text;

        public PincodeDataViewHolder(@NonNull View itemView) {
            super(itemView);

            pincode_text = itemView.findViewById(R.id.pincode_text);
            slot_tracking_text = itemView.findViewById(R.id.tracking_slot_text);
        }

        public TextView getPincode_text() {
            return pincode_text;
        }

        public TextView getSlot_tracking_text() {
            return slot_tracking_text;
        }

    }
}
