package com.example.log_in;

public class User {

    private String Email, reservedDate, selectedTour, selectedTouristNum, status;

    public User() {}

    public User(String Email, String reservedDate, String selectedTour, String selectedTouristNum, String status) {
        this.Email = Email;
        this.reservedDate = reservedDate;
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
        this.status = status;
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

}
