package com.iCropal.iPhobia.Utility.Transmittors;


import com.google.firebase.database.DataSnapshot;
import com.iCropal.iPhobia.DataModel.AppUser;
import com.iCropal.iPhobia.DataModel.DataBase;
import com.iCropal.iPhobia.Ui.Home.Home;

public class RuntimeData {
    public static DataBase dataBase;
    public static Home home;
    public static PreferenceManger referenceManger;
    public static int previousValue = -1;
    public static DataSnapshot pDetails;
    public static AppUser appUser;


    public static void cleanse() {
        dataBase = null;
        home = null;
        referenceManger = null;
        previousValue = -1;
        pDetails = null;
        appUser = null;
    }
}
