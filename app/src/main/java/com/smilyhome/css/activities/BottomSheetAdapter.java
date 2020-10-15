package com.smilyhome.css.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smilyhome.css.R;
import com.smilyhome.css.activities.interfaces.IBottomNavigationItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.BottomSheetViewHolder> {

    private IBottomNavigationItemClickListener mListener;
    private List<String> mBottomNavigationList = new ArrayList<>();

    public void setListener(IBottomNavigationItemClickListener listener) {
        mListener = listener;
    }

    public void setBottomNavigationList(List<String> bottomNavigationList) {
        mBottomNavigationList = bottomNavigationList;
    }

    @NonNull
    @Override
    public BottomSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bottom_sheet_recycler_item, parent, false);
        return new BottomSheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetViewHolder holder, int position) {
        holder.bottomSheetCategoryTextView.setText(mBottomNavigationList.get(position));
    }

    @Override
    public int getItemCount() {
        return mBottomNavigationList.size();
    }

    class BottomSheetViewHolder extends RecyclerView.ViewHolder {

        private TextView bottomSheetCategoryTextView;

        public BottomSheetViewHolder(@NonNull View itemView) {
            super(itemView);
            bottomSheetCategoryTextView = itemView.findViewById(R.id.bottomSheetCategoryTextView);
            View bottomSheetContainer = itemView.findViewById(R.id.bottomSheetContainer);
            bottomSheetContainer.setOnClickListener(view -> mListener.onItemClicked(getAdapterPosition()));
        }
    }
}
