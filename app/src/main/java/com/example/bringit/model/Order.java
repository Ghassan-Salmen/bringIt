package com.example.bringit.model;



public class Order {

    private String memberId;
    private String orderTitle;
    private String orderPlace;
    private String date;
    private String time;
    private String memberName;
    private String orderId;




    public Order() {
    }

    public Order(String memberId, String orderTitle, String orderPlace, String date, String time, String memberName, String orderId) {
        this.memberId = memberId;
        this.orderTitle = orderTitle;
        this.orderPlace = orderPlace;
        this.date = date;
        this.time = time;
        this.memberName = memberName;
        this.orderId = orderId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderPlace() {
        return orderPlace;
    }

    public void setOrderPlace(String orderPlace) {
        this.orderPlace = orderPlace;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
