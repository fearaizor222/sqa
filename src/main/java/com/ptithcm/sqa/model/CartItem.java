package com.ptithcm.sqa.model;

import java.math.BigDecimal;

public class CartItem {

    private int mathuoc;
    private String tenthuoc;
    private String hinhanh;
    private int soluong;
    private double gia;
    private int mahoadon;

    public CartItem(int mathuoc, String tenthuoc, String hinhanh, int soluong, double gia, int mahoadon) {
        this.mathuoc = mathuoc;
        this.tenthuoc = tenthuoc;
        this.hinhanh = hinhanh;
        this.soluong = soluong;
        this.gia = gia;
        this.mahoadon = mahoadon;
    }

    public int getMathuoc() {
        return mathuoc;
    }

    public void setMathuoc(int mathuoc) {
        this.mathuoc = mathuoc;
    }

    public String getTenthuoc() {
        return tenthuoc;
    }

    public void setTenthuoc(String tenthuoc) {
        this.tenthuoc = tenthuoc;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getMahoadon() {
        return mahoadon;
    }

    public void setMahoadon(int mahoadon) {
        this.mahoadon = mahoadon;
    }

}
