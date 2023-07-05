package com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp;

import com.example.swipeorslideanddraganddropitemsinrecyclerviewtemp.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: naftalikomarovski
 * @Date: 2023/06/30
 */
public class DataManager {
    private static List<Item> itemList;

    public static List<Item> getItemList() {

        if (itemList == null) {
            createList();
        }

        return itemList;
    }

    private static void createList() {
        itemList = new ArrayList<>();

        for (int x = 0; x < 20; x++) {
            itemList.add(new Item(x, "Title " + x, "Content of item " + x));
        }
    }
}
