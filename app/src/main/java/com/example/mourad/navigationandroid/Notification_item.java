package com.example.mourad.navigationandroid;


class Notification_item {
    private static int countItem;


    static void setCountItem(int countItem) {
        Notification_item.countItem = countItem;
    }

    static int getCountItem() {
        return countItem;
    }
}
