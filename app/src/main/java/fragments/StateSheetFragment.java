package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bvinstruments.pandemictracker.databinding.StateBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import adapters.StateAdapter;
import data.RequestedData;

public class StateSheetFragment extends BottomSheetDialogFragment {


    public StateAdapter.StateCallback callback;
    private StateBottomSheetBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {

        binding = StateBottomSheetBinding.inflate(inflater);
        setupRecycler();


        return binding.getRoot();


    }


    private void setupRecycler() {


        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        binding.stateList.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        binding.stateList.addItemDecoration(dividerItemDecoration);


        StateAdapter adapter = new StateAdapter(RequestedData.stateList);
        adapter.callback = callback;

        binding.stateList.setAdapter(adapter);

    }


}
