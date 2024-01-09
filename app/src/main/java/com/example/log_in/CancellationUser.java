package com.example.log_in;

public class CancellationUser extends User {
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_EMAIL = "Email";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_CANCELLATION_STATUS = "";

    private boolean cancelled;

    private String Email, reservedDate, selectedTour, selectedTouristNum, status, userId, cancellationStatus, Date;
    private int totalAmount;

    private boolean cancellation;


    public CancellationUser() {

    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public CancellationUser(boolean cancelled, String email, String reservedDate, String selectedTour, String selectedTouristNum, String status, String userId, String cancellationStatus, int totalAmount, boolean cancellation) {
        this.cancelled = cancelled;
        Email = email;
        this.reservedDate = reservedDate;
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
        this.status = status;
        this.userId = userId;
        this.cancellationStatus = cancellationStatus;
        this.totalAmount = totalAmount;
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
        this.reservedDate = reservedDate;
        this.selectedTour = selectedTour;
        this.selectedTouristNum = selectedTouristNum;
        this.status = status;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.cancellationStatus = cancellationStatus;

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
        return "Upcoming".equals(reservedDate);
    }

    public String getUid() {
        return this.userId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }


    public boolean iscancellationStatus() {
        return Boolean.parseBoolean(cancellationStatus);
    }

}