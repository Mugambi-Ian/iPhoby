package com.iCropal.iPhobia.DataModel;

public class AppUser {
    private String phoneNumber;
    private String userName;
    private String dateOfBirth;
    private String tnDp;
    private String acDp;
    private String userId;
    private String userGender;

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AppUser(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTnDp() {
        return tnDp;
    }

    public void setTnDp(String tnDp) {
        this.tnDp = tnDp;
    }

    public String getAcDp() {
        return acDp;
    }

    public void setAcDp(String acDp) {
        this.acDp = acDp;
    }
}
