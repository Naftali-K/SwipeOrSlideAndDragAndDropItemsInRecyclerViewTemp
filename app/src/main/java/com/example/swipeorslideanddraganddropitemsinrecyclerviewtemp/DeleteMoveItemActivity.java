package com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.adapters.RecyclerViewAdapter;
import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.model.Item;

import java.util.Collections;
import java.util.List;

/**
 * https://youtu.be/yua1exHtFB4
 */

public class DeleteMoveItemActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Item> itemList;

    private ItemTouchHelper touchHelper;

    private int draggedItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_move_item);
        setReferences();

        itemList = DataManager.getItemList();

        adapter = new RecyclerViewAdapter(itemList);
        recyclerView.setAdapter(adapter);

        touchHelper  = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.START
//                        ItemTouchHelper.END | ItemTouchHelper.START
                );
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                draggedItemIndex = viewHolder.getAdapterPosition();
                int targetIndex = target.getAdapterPosition();

                Collections.swap(itemList, draggedItemIndex, targetIndex);
                adapter.notifyItemMoved(draggedItemIndex, targetIndex);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.END:
                        break;
                    case  ItemTouchHelper.START:
                        itemList.remove(viewHolder.getAdapterPosition());
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        break;
                }
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void setReferences() {
        recyclerView = findViewById(R.id.recycler_view);
    }
}