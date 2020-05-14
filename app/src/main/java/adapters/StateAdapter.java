package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bvinstruments.pandemictracker.R;

import java.util.List;

import db.State;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateItemHolderView> {


    public StateCallback callback;
    private List<State> list;

    public StateAdapter(List<State> list) {
        this.list = list;

    }

    @NonNull
    @Override
    public StateItemHolderView onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list_item, parent, false);
        return new StateItemHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateItemHolderView holder, int position) {

        holder.state_name.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface StateCallback {

        void text(String data);

    }

    public class StateItemHolderView extends RecyclerView.ViewHolder {


        protected TextView state_name;

        public StateItemHolderView(@NonNull View itemView) {
            super(itemView);
            state_name = itemView.findViewById(R.id.state_list_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast.makeText(v.getContext(),state_name.getText().toString(),Toast.LENGTH_LONG).show();
                    callback.text(state_name.getText().toString());

                }
            });

        }


    }


}
