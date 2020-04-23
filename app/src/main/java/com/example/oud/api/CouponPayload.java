package com.example.oud.api;

public class CouponPayload {

    private String couponId;

    public CouponPayload(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
