package com.ptithcm.sqa.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "chitiethoadon")
@IdClass(ChiTietHoaDonId.class)
public class ChiTietHoaDon implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "mahoadon", nullable = false)
    private HoaDon hoaDon;

    @Id
    @ManyToOne
    @JoinColumn(name = "mathuoc", nullable = false)
    private Thuoc thuoc;

    @Column(name = "soluong", nullable = false)
    private Integer soLuong;

    @Column(name = "giaban", nullable = false)
    private double giaBan;

    @Column(name = "ngaylap", nullable = false)
    private LocalDateTime ngayLap = LocalDateTime.now();

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon, Thuoc thuoc, Integer soLuong, double giaBan, LocalDateTime ngaylap) {
        this.hoaDon = hoaDon;
        this.thuoc = thuoc;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.ngayLap = ngaylap;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getThanhTien() {
        return giaBan * soLuong;
    }
}