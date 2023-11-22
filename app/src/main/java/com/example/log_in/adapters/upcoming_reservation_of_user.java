package com.example.log_in.adapters;

public class upcoming_reservation_of_user {
    private  String reservedDate, selectedTime, selectedTour, selectedTouristNum;


    public upcoming_reservation_of_user (){

    }


    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
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

    public upcoming_reservation_of_user(String reservedDate, String selectedTime, String selectedTour, String selectedTouristNum) {
        this.reservedDate = reservedDate;
        this.selectedTime = selectedTime;
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
    }
}
