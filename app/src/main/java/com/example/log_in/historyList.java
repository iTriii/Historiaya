package com.example.log_in;

public class historyList extends User {

    private String reservedDate;
    private String selectedTour;
    private String selectedTouristNum;
    private String email;
    private String status;
    private String uid;
    private String imageUrl;

    public historyList() {
        // Default constructor required for calls to DataSnapshot.getValue(historyList.class)
    }

    // Constructor
    public historyList(String reservedDate, String selectedTour, String selectedTouristNum, String email, String status, String uid, String imageUrl) {
        this.reservedDate = reservedDate;
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
        this.email = email;
        this.status = status;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for each field

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
