package com.example.log_in;
//FOR UPDATE ONLY

public class User {
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_EMAIL = "Email";
    public static final String FIELD_STATUS = "status1";
    public static final String FIELD_CANCELLATION_STATUS = "";
    public static String FIELD_STATUS1 = "status1";
    public static String FIELD_STATUS2 = "status2";
    public static String FIELD_STATUS3 = "status3";
    public static String FIELD_STATUS4 = "status4";
    public static String FIELD_STATUS5 = "status5";

    private boolean cancelled1;

    private String Email, reservedDate1, selectedTour1, selectedTouristNum1, status1, userId, cancellationStatus1, Date;
    private String reservedDate2, selectedTour2, selectedTouristNum2, status2,  cancellationStatus2;
    private String reservedDate3, selectedTour3, selectedTouristNum3, status3,  cancellationStatus3;
    private String reservedDate4, selectedTour4, selectedTouristNum4, status4,  cancellationStatus4;
    private String reservedDate5, selectedTour5, selectedTouristNum5, status5,  cancellationStatus5;

    private String reservedDate, selectedTour, selectedTouristNum, status,  cancellationStatus;
    private int totalAmount;
    private int totalAmount3;
    private int totalAmount4;

    public int getTotalAmount5() {
        return totalAmount5;
    }

    public void setTotalAmount5(int totalAmount5) {
        this.totalAmount5 = totalAmount5;
    }

    private int totalAmount5;
    private Object selectRefundOption2;

    public User(boolean cancelled1, String email, String reservedDate1, String selectedTour1, String selectedTouristNum1, String status1, String userId, String cancellationStatus1, String date, String reservedDate2, String selectedTour2, String selectedTouristNum2, String status2, String cancellationStatus2, String reservedDate3, String selectedTour3, String selectedTouristNum3, String status3, String cancellationStatus3, String reservedDate4, String selectedTour4, String selectedTouristNum4, String status4, String cancellationStatus4, String reservedDate5, String selectedTour5, String selectedTouristNum5, String status5, String cancellationStatus5, int totalAmount, int totalAmount3, int totalAmount4, int totalAmount5, Object selectRefundOption2, int totalAmount1, int totalAmount2, boolean cancellation) {
        this.cancelled1 = cancelled1;
        Email = email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.cancellationStatus1 = cancellationStatus1;
        Date = date;
        this.reservedDate2 = reservedDate2;
        this.selectedTour2 = selectedTour2;
        this.selectedTouristNum2 = selectedTouristNum2;
        this.status2 = status2;
        this.cancellationStatus2 = cancellationStatus2;
        this.reservedDate3 = reservedDate3;
        this.selectedTour3 = selectedTour3;
        this.selectedTouristNum3 = selectedTouristNum3;
        this.status3 = status3;
        this.cancellationStatus3 = cancellationStatus3;
        this.reservedDate4 = reservedDate4;
        this.selectedTour4 = selectedTour4;
        this.selectedTouristNum4 = selectedTouristNum4;
        this.status4 = status4;
        this.cancellationStatus4 = cancellationStatus4;
        this.reservedDate5 = reservedDate5;
        this.selectedTour5 = selectedTour5;
        this.selectedTouristNum5 = selectedTouristNum5;
        this.status5 = status5;
        this.cancellationStatus5 = cancellationStatus5;
        this.totalAmount = totalAmount;
        this.totalAmount3 = totalAmount3;
        this.totalAmount4 = totalAmount4;
        this.totalAmount5 = totalAmount5;
        this.selectRefundOption2 = selectRefundOption2;
        this.totalAmount1 = totalAmount1;
        this.totalAmount2 = totalAmount2;
        this.cancellation = cancellation;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellationStatus() {
        return cancellationStatus;
    }

    public User(boolean cancelled1, String email, String reservedDate1, String selectedTour1, String selectedTouristNum1, String status1, String userId, String cancellationStatus1, String date, String reservedDate, String selectedTour, String selectedTouristNum, String status, String cancellationStatus, String reservedDate2, String selectedTour2, String selectedTouristNum2, String status2, String cancellationStatus2, String reservedDate3, String selectedTour3, String selectedTouristNum3, String status3, String cancellationStatus3, String reservedDate4, String selectedTour4, String selectedTouristNum4, String status4, String cancellationStatus4, String reservedDate5, String selectedTour5, String selectedTouristNum5, String status5, String cancellationStatus5, int totalAmount1, int totalAmount2, boolean cancellation) {
        this.cancelled1 = cancelled1;
        Email = email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.cancellationStatus1 = cancellationStatus1;
        Date = date;
//        this.reservedDate = reservedDate;
//        this.selectedTour = selectedTour;
//        this.selectedTouristNum = selectedTouristNum;
//        this.status = status;
//        this.cancellationStatus = cancellationStatus;
        this.reservedDate2 = reservedDate2;
        this.selectedTour2 = selectedTour2;
        this.selectedTouristNum2 = selectedTouristNum2;
        this.status2 = status2;
        this.cancellationStatus2 = cancellationStatus2;
        this.reservedDate3 = reservedDate3;
        this.selectedTour3 = selectedTour3;
        this.selectedTouristNum3 = selectedTouristNum3;
        this.status3 = status3;
        this.cancellationStatus3 = cancellationStatus3;
        this.reservedDate4 = reservedDate4;
        this.selectedTour4 = selectedTour4;
        this.selectedTouristNum4 = selectedTouristNum4;
        this.status4 = status4;
        this.cancellationStatus4 = cancellationStatus4;
        this.reservedDate5 = reservedDate5;
        this.selectedTour5 = selectedTour5;
        this.selectedTouristNum5 = selectedTouristNum5;
        this.status5 = status5;
        this.cancellationStatus5 = cancellationStatus5;
        this.totalAmount1 = totalAmount1;
        this.totalAmount2 = totalAmount2;
        this.cancellation = cancellation;
    }


    public String getReservedDate3() {
        return reservedDate3;
    }

    public void setReservedDate3(String reservedDate3) {
        this.reservedDate3 = reservedDate3;
    }

    public String getSelectedTour3() {
        return selectedTour3;
    }

    public void setSelectedTour3(String selectedTour3) {
        this.selectedTour3 = selectedTour3;
    }

    public String getSelectedTouristNum3() {
        return selectedTouristNum3;
    }

    public void setSelectedTouristNum3(String selectedTouristNum3) {
        this.selectedTouristNum3 = selectedTouristNum3;
    }

    public String getStatus3() {
        return status3;
    }

    public void setStatus3(String status3) {
        this.status3 = status3;
    }

    public String getCancellationStatus3() {
        return cancellationStatus3;
    }

    public void setCancellationStatus3(String cancellationStatus3) {
        this.cancellationStatus3 = cancellationStatus3;
    }

    public String getReservedDate4() {
        return reservedDate4;
    }

    public void setReservedDate4(String reservedDate4) {
        this.reservedDate4 = reservedDate4;
    }

    public String getSelectedTour4() {
        return selectedTour4;
    }

    public void setSelectedTour4(String selectedTour4) {
        this.selectedTour4 = selectedTour4;
    }

    public String getSelectedTouristNum4() {
        return selectedTouristNum4;
    }

    public void setSelectedTouristNum4(String selectedTouristNum4) {
        this.selectedTouristNum4 = selectedTouristNum4;
    }

    public String getStatus4() {
        return status4;
    }

    public void setStatus4(String status4) {
        this.status4 = status4;
    }

    public String getCancellationStatus4() {
        return cancellationStatus4;
    }

    public void setCancellationStatus4(String cancellationStatus4) {
        this.cancellationStatus4 = cancellationStatus4;
    }

    public String getReservedDate5() {
        return reservedDate5;
    }

    public void setReservedDate5(String reservedDate5) {
        this.reservedDate5 = reservedDate5;
    }

    public String getSelectedTour5() {
        return selectedTour5;
    }

    public void setSelectedTour5(String selectedTour5) {
        this.selectedTour5 = selectedTour5;
    }

    public String getSelectedTouristNum5() {
        return selectedTouristNum5;
    }

    public void setSelectedTouristNum5(String selectedTouristNum5) {
        this.selectedTouristNum5 = selectedTouristNum5;
    }

    public String getStatus5() {
        return status5;
    }

    public void setStatus5(String status5) {
        this.status5 = status5;
    }

    public String getCancellationStatus5() {
        return cancellationStatus5;
    }

    public void setCancellationStatus5(String cancellationStatus5) {
        this.cancellationStatus5 = cancellationStatus5;
    }

    public User(boolean cancelled1, String email, String reservedDate1, String selectedTour1, String selectedTouristNum1, String status1, String userId, String cancellationStatus1, String date, String reservedDate2, String selectedTour2, String selectedTouristNum2, String status2, String cancellationStatus2, String reservedDate3, String selectedTour3, String selectedTouristNum3, String status3, String cancellationStatus3, String reservedDate4, String selectedTour4, String selectedTouristNum4, String status4, String cancellationStatus4, String reservedDate5, String selectedTour5, String selectedTouristNum5, String status5, String cancellationStatus5, int totalAmount1, int totalAmount2, boolean cancellation) {
        this.cancelled1 = cancelled1;
        Email = email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.cancellationStatus1 = cancellationStatus1;
        Date = date;
        this.reservedDate2 = reservedDate2;
        this.selectedTour2 = selectedTour2;
        this.selectedTouristNum2 = selectedTouristNum2;
        this.status2 = status2;
        this.cancellationStatus2 = cancellationStatus2;
        this.reservedDate3 = reservedDate3;
        this.selectedTour3 = selectedTour3;
        this.selectedTouristNum3 = selectedTouristNum3;
        this.status3 = status3;
        this.cancellationStatus3 = cancellationStatus3;
        this.reservedDate4 = reservedDate4;
        this.selectedTour4 = selectedTour4;
        this.selectedTouristNum4 = selectedTouristNum4;
        this.status4 = status4;
        this.cancellationStatus4 = cancellationStatus4;
        this.reservedDate5 = reservedDate5;
        this.selectedTour5 = selectedTour5;
        this.selectedTouristNum5 = selectedTouristNum5;
        this.status5 = status5;
        this.cancellationStatus5 = cancellationStatus5;
        this.totalAmount1 = totalAmount1;
        this.totalAmount2 = totalAmount2;
        this.cancellation = cancellation;
    }

    private int totalAmount1;
    private int totalAmount2;

    public static String getFieldStatus1() {
        return FIELD_STATUS1;
    }

    public static void setFieldStatus1(String fieldStatus1) {
        FIELD_STATUS1 = fieldStatus1;
    }

    public static String getFieldStatus2() {
        return FIELD_STATUS2;
    }

    public static void setFieldStatus2(String fieldStatus2) {
        FIELD_STATUS2 = fieldStatus2;
    }

    public static String getFieldStatus3() {
        return FIELD_STATUS3;
    }

    public static void setFieldStatus3(String fieldStatus3) {
        FIELD_STATUS3 = fieldStatus3;
    }

    public static String getFieldStatus4() {
        return FIELD_STATUS4;
    }

    public static void setFieldStatus4(String fieldStatus4) {
        FIELD_STATUS4 = fieldStatus4;
    }

    public static String getFieldStatus5() {
        return FIELD_STATUS5;
    }

    public static void setFieldStatus5(String fieldStatus5) {
        FIELD_STATUS5 = fieldStatus5;
    }

    public boolean isCancelled1() {
        return cancelled1;
    }

    public void setCancelled1(boolean cancelled1) {
        this.cancelled1 = cancelled1;
    }

    public void setSelectedTouristNum1(String selectedTouristNum1) {
        this.selectedTouristNum1 = selectedTouristNum1;
    }

    public String getCancellationStatus1() {
        return cancellationStatus1;
    }

    public void setCancellationStatus1(String cancellationStatus1) {
        this.cancellationStatus1 = cancellationStatus1;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getReservedDate2() {
        return reservedDate2;
    }

    public void setReservedDate2(String reservedDate2) {
        this.reservedDate2 = reservedDate2;
    }

    public String getSelectedTour2() {
        return selectedTour2;
    }

    public void setSelectedTour2(String selectedTour2) {
        this.selectedTour2 = selectedTour2;
    }

    public String getSelectedTouristNum2() {
        return selectedTouristNum2;
    }

    public void setSelectedTouristNum2(String selectedTouristNum2) {
        this.selectedTouristNum2 = selectedTouristNum2;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getCancellationStatus2() {
        return cancellationStatus2;
    }

    public void setCancellationStatus2(String cancellationStatus2) {
        this.cancellationStatus2 = cancellationStatus2;
    }

    public int getTotalAmount2() {
        return totalAmount2;
    }

    public void setTotalAmount2(int totalAmount2) {
        this.totalAmount2 = totalAmount2;
    }

    public boolean isCancellation() {
        return cancellation;
    }

    public User(boolean cancelled1, String email, String reservedDate1, String selectedTour1, String selectedTouristNum1, String status1, String userId, String cancellationStatus1, String date, String reservedDate2, String selectedTour2, String selectedTouristNum2, String status2, String cancellationStatus2, int totalAmount1, int totalAmount2, boolean cancellation) {
        this.cancelled1 = cancelled1;
        Email = email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.cancellationStatus1 = cancellationStatus1;
        Date = date;
        this.reservedDate2 = reservedDate2;
        this.selectedTour2 = selectedTour2;
        this.selectedTouristNum2 = selectedTouristNum2;
        this.status2 = status2;
        this.cancellationStatus2 = cancellationStatus2;
        this.totalAmount1 = totalAmount1;
        this.totalAmount2 = totalAmount2;
        this.cancellation = cancellation;
    }

    public User(String date) {
        Date = date;
    }

    private boolean cancellation;


    public User() {

    }

    public void setCancelled(boolean cancelled) {
        this.cancelled1 = cancelled1;
    }

    public User(boolean cancelled1, String email, String reservedDate1, String selectedTour1, String selectedTouristNum1, String status1, String userId, String cancellationStatus, int totalAmount1, boolean cancellation) {
        this.cancelled1 = cancelled1;
        Email = email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.cancellationStatus1 = cancellationStatus;
        this.totalAmount1 = totalAmount1;
        this.cancellation = cancellation;
    }

    public void setCancellationStatus(String cancellationStatus) {
        this.cancellationStatus1 = cancellationStatus;
    }

    public void setCancellation(boolean cancellation) {
        this.cancellation = cancellation;
    }

    public User(String Email, String reservedDate1, String cancellationStatus, String selectedTour1, String selectedTouristNum1, String status1, String userId, int totalAmount1) {
        this.Email = Email;
        this.reservedDate1 = reservedDate1;
        this.selectedTour1 = selectedTour1;
        this.selectedTouristNum1 = selectedTouristNum1;
        this.status1 = status1;
        this.userId = userId;
        this.totalAmount1 = totalAmount1;
        this.cancellationStatus1 = cancellationStatus;

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

    public void setReservedDate1(String reservedDate1) {
        this.reservedDate1 = reservedDate1;
    }

    public String getSelectedTour1() {
        return selectedTour1;
    }

    public void setSelectedTour1(String selectedTour1) {
        this.selectedTour1 = selectedTour1;
    }

    public String getSelectedTouristNum1() {
        return selectedTouristNum1;
    }

    public void setSelectedTouristNum(String selectedTouristNum) {
        this.selectedTouristNum1 = selectedTouristNum;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
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

    public void setTotalAmount1(int totalAmount1) {
        this.totalAmount1 = totalAmount1;
    }


    public boolean iscancellationStatus() {
        return Boolean.parseBoolean(cancellationStatus1);
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalAmount3() {
        return totalAmount3;
    }

    public void setTotalAmount3(int totalAmount3) {
        this.totalAmount3 = totalAmount3;
    }

    public int getTotalAmount4() {
        return totalAmount4;
    }

    public void setTotalAmount4(int totalAmount4) {
        this.totalAmount4 = totalAmount4;
    }

    public String getSelectRefundOption() {
        return getSelectRefundOption();
    }

    public Object getSelectRefundOption1() {
        return getSelectRefundOption1();
    }

    public Object getSelectRefundOption2() {
        return selectRefundOption2;
    }

    public void setSelectRefundOption2(Object selectRefundOption2) {
        this.selectRefundOption2 = selectRefundOption2;
    }

}