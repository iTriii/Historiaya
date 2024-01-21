package com.example.log_in;

public class CancellationUser extends User {
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_EMAIL = "Email";
    public static final String FIELD_STATUS1 = "status1";
    public static final String FIELD_CANCELLATION_STATUS = "";

    private boolean cancelled1;

    private String Email, reservedDate1, selectedTour1, selectedTouristNum1, status1, userId, cancellationStatus, Date;
    private int totalAmount1;

    private boolean cancellation;


    public CancellationUser() {

    }

    public void setCancelled(boolean cancelled1) {
        this.cancelled1 = cancelled1;
    }

    public CancellationUser(boolean cancelled1, String email, String reservedDate1, String selectedTour1, String selectedTouristNum1, String status1, String userId, String cancellationStatus, int totalAmount1, boolean cancellation) {
        this.cancelled1 = cancelled1;
        Email = email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.cancellationStatus = cancellationStatus;
        this.totalAmount1 = totalAmount1;
        this.cancellation = cancellation;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus = cancellationStatus;
    }

    public void setCancellation(boolean cancellation) {
        this.cancellation = cancellation;
    }

    public CancellationUser(String Email, String reservedDate, String cancellationStatus, String selectedTour, String selectedTouristNum, String status, String userId, int totalAmount) {
        this.Email = Email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.totalAmount1 = totalAmount1;
        this.cancellationStatus = cancellationStatus;

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getReservedDate1() {
        return reservedDate1;
    }

    public void setReservedDate1(String reservedDate) {
        this.reservedDate1 = reservedDate;
    }

    public String getSelectedTour1() {
        return selectedTour1;
    }

    public void setSelectedTour1(String selectedTour) {
        this.selectedTour1 = selectedTour;
    }

    public String getSelectedTouristNum1() {
        return selectedTouristNum1;
    }

    public void setSelectedTouristNum1(String selectedTouristNum) {
        this.selectedTouristNum1 = selectedTouristNum;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status) {
        this.status1 = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isUpcoming() {
        return "Upcoming".equals(reservedDate1);
    }

    public String getUid() {
        return this.userId;
    }

    public int getTotalAmount1() {
        return totalAmount1;
    }

    public void setTotalAmount1(int totalAmount) {
        this.totalAmount1 = totalAmount1;
    }


    public boolean iscancellationStatus() {
        return Boolean.parseBoolean(cancellationStatus);
    }

}