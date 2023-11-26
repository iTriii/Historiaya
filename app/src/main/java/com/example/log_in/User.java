package com.example.log_in;

public class User {
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_EMAIL = "Email";
    public static final String FIELD_STATUS = "status";
    public String String;

    private String Email, reservedDate, selectedTour, selectedTouristNum, status, userId;

    public User() {
    }



    public User(String Email, String reservedDate, String selectedTour, String selectedTouristNum, String status, String userId, Long totalAmount) {
        this.Email = Email;
        this.reservedDate = reservedDate;
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
        this.status = status;
        this.userId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getSelectedTour() {
        return selectedTour;
    }

    public void setSelectedTour(String selectedTour) {
        this.selectedTour = selectedTour;
    }

    public String getSelectedTouristNum() {
        return selectedTouristNum;
    }

    public void setSelectedTouristNum(String selectedTouristNum) {
        this.selectedTouristNum = selectedTouristNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isUpcoming() {
        return "Upcoming".equals(status);
    }

    public String getUid() {
        return this.userId;
    }
}
