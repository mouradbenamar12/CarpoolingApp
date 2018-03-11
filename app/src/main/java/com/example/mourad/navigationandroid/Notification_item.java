package com.example.mourad.navigationandroid;


class Notification_item {

    private static int countItem;

    Notification_item(){
    }

    Notification_item(int countItem){
        Notification_item.countItem=countItem;
    }

    public static void setCountItem(int countItem) {
        Notification_item.countItem = countItem;
    }

    public static int getCountItem() {
        return countItem;
    }

}
