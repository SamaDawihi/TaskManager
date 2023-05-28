package com.example.taskmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.EventViewHolder>{

    customButtonListener customListner;
    public interface customButtonListener {
        public void onButtonClickListner(int position,EventModel value);
        public void onButtonClickListnerview(int position,EventModel value);
    }
    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    List<EventModel> eventModelList;
    Context context;
    MyTasksDB myTasksDB;
    public AdapterEvent(List<EventModel> eventModelList , Context context, MyTasksDB myTasksDB)
    {
        this.eventModelList=eventModelList;
        this.context=context;
        this.myTasksDB=myTasksDB;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_event, parent, false);
        return new EventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.name.setText(eventModelList.get(position).getName().toString());
        holder.date.setText(eventModelList.get(position).getDateTime().toString());
        if (eventModelList.get(position).getPriority() == 1)
        {
            holder.priority.setText( "LOW");

        }
        if (eventModelList.get(position).getPriority() == 2)
        {
            holder.priority.setText( "Medium");

        }
        if (eventModelList.get(position).getPriority() == 3)
        {
            holder.priority.setText( "High");

        }
        if (eventModelList.get(position).getPriority() == 4)
        {
            holder.priority.setText( "Urgent");

        }
        //   holder.priority.setText(   eventModelList.get(position).getPriority()+"");
        holder.note.setText(eventModelList.get(position).getNote().toString());
        holder.reminder.setText(eventModelList.get(position).getReminderUnit().toString() + "  "+ eventModelList.get(position).getReminderDuration()+" ");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customListner != null) {
                    customListner.onButtonClickListnerview(position,eventModelList.get(position));
                }
            }
        });
        holder.imageView_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///go to update event

                if (customListner != null) {
                    customListner.onButtonClickListner(position,eventModelList.get(position));
                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return eventModelList.size();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,date,priority,note,reminder;
        CardView cardView;
        ImageView imageView_update;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date_event);
            priority=itemView.findViewById(R.id.priority1);
            note=itemView.findViewById(R.id.note_event);
            reminder=itemView.findViewById(R.id.reminder);
            cardView=itemView.findViewById(R.id.cardevent);
            imageView_update=itemView.findViewById(R.id.image_update);

        }
    }
}
