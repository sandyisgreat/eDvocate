package com.advocate.database;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table USER_TABLE.
 */
public class UserTable {

    private String mobileNumber;
    private String userIdLawyer;
    private String emailId;
    /** Not-null value. */
    private String lawyerId;
    private String firstName;
    private String lastName;
    private String category;
    private String firmNames;

    public UserTable() {
    }

    public UserTable(String userIdLawyer) {
        this.userIdLawyer = userIdLawyer;
    }

    public UserTable(String mobileNumber, String userIdLawyer, String emailId, String lawyerId, String firstName, String lastName, String category, String firmNames) {
        this.mobileNumber = mobileNumber;
        this.userIdLawyer = userIdLawyer;
        this.emailId = emailId;
        this.lawyerId = lawyerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.category = category;
        this.firmNames = firmNames;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserIdLawyer() {
        return userIdLawyer;
    }

    public void setUserIdLawyer(String userIdLawyer) {
        this.userIdLawyer = userIdLawyer;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    /** Not-null value. */
    public String getLawyerId() {
        return lawyerId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFirmNames() {
        return firmNames;
    }

    public void setFirmNames(String firmNames) {
        this.firmNames = firmNames;
    }

}
