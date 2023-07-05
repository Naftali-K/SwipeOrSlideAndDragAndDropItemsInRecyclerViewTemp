package com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.R;
import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: naftalikomarovski
 * @Date: 2023/06/30
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Item> itemList;
    private List<Item> itemListAll;

    public RecyclerViewAdapter(List<Item> itemList) {
        this.itemList = itemList;
        this.itemListAll = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(itemList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // from Filterable - import android.widget.Filterable;
    @Override
    public Filter getFilter() {

        return filter;
    }

    // import android.widget.Filter
    private Filter filter = new Filter() {
        // run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Item> filteredItemList = new ArrayList<>();

            if (charSequence.toString().isEmpty()){
                filteredItemList.addAll(itemListAll);
            } else {
                for (Item item: itemListAll) {
                    if (item.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredItemList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredItemList;

            return filterResults;
        }

        // run on a UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            itemList.clear();
            itemList.addAll((Collection<? extends Item>) filterResults.values);
            notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView, contentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            contentTextView = itemView.findViewById(R.id.content_text_view);
        }

        void bind(Item item, int position) {
            titleTextView.setText(item.getTitle());
            contentTextView.setText(item.getContent());
        }
    }
}
