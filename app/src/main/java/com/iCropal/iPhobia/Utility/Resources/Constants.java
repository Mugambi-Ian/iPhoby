package com.iCropal.iPhobia.Utility.Resources;

public class Constants {
    public static final String API_Url = "https://iphoby.azurewebsites.net/api/BmpDataRecords?phoneNumber=";
    public static final String Phobia_Url = "https://iphoby.azurewebsites.net/api/Phobias";
    public static final String Sync_Url = "https://iphoby.azurewebsites.net/api/SyncDevice";
    public static final String EndS_Url = "https://iphoby.azurewebsites.net/api/CallBack?phoneNumber=";
    public static String User_Database = "User_Database";
    public static String Phobia = "Phobia";
    public static String UserPhoneNumber = "UserPhoneNumber";
    public static String UserName = "UserName", TnDP = "tbDp", DateOfBirth = "dateOfBirth", AcDp = "acDp";
    public static String UserId = "UserId";
    public static String changeDate = "changeDate";

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    public static String titleCaseConversion(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
        return converted.toString();
    }
}
