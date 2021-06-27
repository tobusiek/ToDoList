package eLeader.to_do;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<DBPosition> toDoList;
    private final ItemClicked activity;

    public interface ItemClicked {
        void onItemClicked(int index);
    }

    public RecyclerViewAdapter(Context context, List<DBPosition> toDoList) {
        Collections.reverse(toDoList);
        this.toDoList = toDoList;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskNameRV, taskDateRV;
        private final ImageView taskImageRV;
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            taskNameRV = itemView.findViewById(R.id.taskNameRV);
            taskDateRV = itemView.findViewById(R.id.taskDateRV);
            taskImageRV = itemView.findViewById(R.id.taskImageRV);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(toDoList.indexOf((DBPosition)v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_position_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.itemView.setTag(toDoList.get(position));
        viewHolder.taskNameRV.setText(toDoList.get(position).getTask_name());
        viewHolder.taskDateRV.setText(toDoList.get(position).getTask_date());

        switch (toDoList.get(position).getTask_cat()) {
            case "Praca":
                viewHolder.taskImageRV.setImageResource(R.drawable.ic_baseline_work);
                viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.job_color));
                break;

            case "Zakupy":
                viewHolder.taskImageRV.setImageResource(R.drawable.ic_baseline_shopping);
                viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.shopping_color));
                break;

            case "Inne":
                viewHolder.taskImageRV.setImageResource(R.drawable.ic_baseline_note);
                viewHolder.cardView.setCardBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.note_color));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
