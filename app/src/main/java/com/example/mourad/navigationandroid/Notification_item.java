package com.example.mourad.navigationandroid;

/**
 * Created by abdelle on 3/10/18.
 */
public class Notification_item {
    private static int countItem;

    public Notification_item() {
    }

    static void setCountItem(int countItem) {
        Notification_item.countItem = countItem;
    }

    static int getCountItem() {
        return countItem;
    }
}
