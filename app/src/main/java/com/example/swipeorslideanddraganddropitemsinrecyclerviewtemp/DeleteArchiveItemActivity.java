package com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.adapters.RecyclerViewAdapter;
import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * https://youtu.be/rcSNkSJ624U - swipe Left/Right
 * https://youtu.be/H9D_HoOeKWM - move Up/Down
 * https://youtu.be/CTvzoVtKoJ8 - search
 * https://github.com/xabaras/RecyclerViewSwipeDecorator
 */

public class DeleteArchiveItemActivity extends AppCompatActivity {

    private static final String TAG = "Test_code";

    private RecyclerView recyclerView;
    private List<Item> itemList;
    private RecyclerViewAdapter adapter;
    private ItemTouchHelper.SimpleCallback simpleCallback;
    private ItemTouchHelper itemTouchHelper;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_archive_item);
        setReferences();

        itemList = DataManager.getItemList();

        adapter = new RecyclerViewAdapter(itemList);
        recyclerView.setAdapter(adapter);

        simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int itemPosition = viewHolder.getAdapterPosition();
                int targetItemPosition = target.getAdapterPosition();

                Collections.swap(itemList, itemPosition, targetItemPosition);

                adapter.notifyItemMoved(itemPosition, targetItemPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int itemPosition = viewHolder.getAdapterPosition();
                Log.d(TAG, "onSwiped: Position: " + itemPosition);
                Item item = itemList.get(itemPosition);
                Log.d(TAG, "onSwiped: Item: " + item.toString());
                String itemTitle = item.getTitle();

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        itemList.remove(itemPosition);
                        adapter.notifyItemRemoved(itemPosition);

                        Snackbar.make(recyclerView, "Sure remove item: " + itemTitle, Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d(TAG, "onClick: Back Item: " + item.toString());
                                        itemList.add(itemPosition, item);
                                        adapter.notifyItemInserted(itemPosition);
                                    }
                                }).show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        itemList.remove(itemPosition);
                        adapter.notifyItemRemoved(itemPosition);

                        Snackbar.make(recyclerView, "Sure back item: " + itemTitle, Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d(TAG, "onClick: Back Item: " + item.toString());
                                        itemList.add(itemPosition, item);
                                        adapter.notifyItemInserted(itemPosition);
                                    }
                                }).show();
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.teal_700))
                        .addSwipeLeftActionIcon(R.drawable.icon_delete)
                        .addSwipeLeftLabel("Delete")
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.purple_200))
                        .addSwipeRightActionIcon(R.drawable.icon_archive)
                        .addSwipeRightLabel("Archive")
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
            }
        });
    }

    private void setReferences() {
        recyclerView = findViewById(R.id.recycler_view);
        searchEditText = findViewById(R.id.search_edit_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_top_side, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        //import androidx.appcompat.widget.SearchView;
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}