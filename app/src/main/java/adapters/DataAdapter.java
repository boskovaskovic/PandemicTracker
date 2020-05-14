package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bvinstruments.pandemictracker.R;

import java.text.NumberFormat;
import java.util.List;

import model.DataSet;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {


    List<DataSet> data;

    public DataAdapter(List<DataSet> data) {
        this.data = data;

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item, parent, false);
        return new DataViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTitle());
        holder.value.setText(NumberFormat.getInstance().format(Long.parseLong(data.get(position).getResult())));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView value;


        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.state_item_title);
            value = itemView.findViewById(R.id.state_item_value);

        }
    }


}
