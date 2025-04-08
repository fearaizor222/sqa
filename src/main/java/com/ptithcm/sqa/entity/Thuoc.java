package com.ptithcm.sqa.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "thuoc")
public class Thuoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mathuoc")
    private int maThuoc;

    @Column(name = "tenthuoc")
    private String tenThuoc;

    @Column(name = "gia")
    private double gia;

    @Column(name = "soluong")
    private int soLuong;

    @Column(name = "tuoisudung")
    private String tuoiSuDung;

    @Column(name = "thanhphan", columnDefinition = "TEXT")
    private String thanhPhan;

    @Column(name = "luutru", columnDefinition = "TEXT")
    private String luuTru;

    @Column(name = "tacdungphu", columnDefinition = "TEXT")
    private String tacDungPhu;

    @Column(name = "quycach", columnDefinition = "TEXT")
    private String quyCach;

    @Column(name = "hinhanh", columnDefinition = "TEXT")
    private String hinhAnh;

    @Column(name = "nhasanxuat")
    private String nhaSanXuat;

    @Column(name = "mota", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ten_thuoc")
    private String tenThuoc2;

    // Default constructor
    public Thuoc() {
    }

    // Constructor with all parameters
    public Thuoc(String tenThuoc, int soLuong, double gia, String tuoiSuDung, String thanhPhan, String luuTru,
            String tacDungPhu, String quyCach, String hinhAnh, String nhaSanXuat, String moTa, String tenThuoc2) {
        this.tenThuoc = tenThuoc;
        this.soLuong = soLuong;
        this.gia = gia;
        this.tuoiSuDung = tuoiSuDung;
        this.thanhPhan = thanhPhan;
        this.luuTru = luuTru;
        this.tacDungPhu = tacDungPhu;
        this.quyCach = quyCach;
        this.hinhAnh = hinhAnh;
        this.nhaSanXuat = nhaSanXuat;
        this.moTa = moTa;
        this.tenThuoc2 = tenThuoc2;
    }

    // Getters and setters
    public int getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getTuoiSuDung() {
        return tuoiSuDung;
    }

    public void setTuoiSuDung(String tuoiSuDung) {
        this.tuoiSuDung = tuoiSuDung;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public String getLuuTru() {
        return luuTru;
    }

    public void setLuuTru(String luuTru) {
        this.luuTru = luuTru;
    }

    public String getTacDungPhu() {
        return tacDungPhu;
    }

    public void setTacDungPhu(String tacDungPhu) {
        this.tacDungPhu = tacDungPhu;
    }

    public String getQuyCach() {
        return quyCach;
    }

    public void setQuyCach(String quyCach) {
        this.quyCach = quyCach;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTenThuoc2() {
        return tenThuoc2;
    }

    public void setTenThuoc2(String tenThuoc2) {
        this.tenThuoc2 = tenThuoc2;
    }
}
