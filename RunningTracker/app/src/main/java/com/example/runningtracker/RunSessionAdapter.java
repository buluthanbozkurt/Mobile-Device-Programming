package com.example.runningtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RunSessionAdapter extends RecyclerView.Adapter<RunSessionAdapter.RunSessionViewHolder>{

    private List<RunSession> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public RunSessionAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RunSessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RunSessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RunSessionViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<RunSession> newData) {
        if (data != null) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        } else {
            data = newData;
        }
    }

    class RunSessionViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView typeView;
        TextView notesView;
        TextView ratingView;

        RunSessionViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            notesView = itemView.findViewById(R.id.notesView);
            ratingView = itemView.findViewById(R.id.ratingView);
        }

        void bind(final RunSession runSession) {

            if (runSession != null) {
                nameView.setText(runSession.getName());
                notesView.setText(runSession.getNotes());
                ratingView.setText(runSession.getRating());
            }
        }

    }
}
