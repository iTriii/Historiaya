package com.example.log_in;
public class Booking {
    private String selectedTour;
    private String selectedTouristNum;
    private String reservedDate;
    private double totalAmount;
    private String selectedTime;
    private String status;

    public Booking(String selectedTour, String selectedTouristNum, String reservedDate, double totalAmount, String selectedTime) {
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
        this.reservedDate = reservedDate;
        this.totalAmount = totalAmount;
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

    public String getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(String reservedDate) {
        this.reservedDate = reservedDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}