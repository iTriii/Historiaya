package com.example.log_in.adapters;

public class upcoming_reservation_of_user {
    private String reservedDate, selectedTime, selectedTour, FirstName, LastName, selectedTouristNum;

    public upcoming_reservation_of_user() {
    }

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getselectedTime() {
        return selectedTime;
    }

    public void setselectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getSelectedTour() {
        return selectedTour;
    }

    public void setSelectedTour(String selectedTour) {
        this.selectedTour = selectedTour;
    }


    public upcoming_reservation_of_user(String reservedDate, String selectedTime, String selectedTour, String FirstName, String LastName, String selectedTouristNum) {
        this.reservedDate = reservedDate;
        this.selectedTime = selectedTime;
        this.selectedTour = selectedTour;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.selectedTouristNum = selectedTouristNum;

    }
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }
    public String getLastName() {
        return LastName;
    }
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    public String getselectedTouristNum() {
        return selectedTouristNum;
    }
    public void setselectedTouristNum(String selectedTouristNum) {
        this.selectedTouristNum = selectedTouristNum;
    }

}
