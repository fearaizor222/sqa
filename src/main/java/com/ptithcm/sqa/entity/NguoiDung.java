package com.ptithcm.sqa.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nguoidung")
public class NguoiDung implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manguoidung", nullable = false)
    private Long maNguoiDung;

    @Column(name = "tennguoidung", nullable = false, length = 255)
    private String tenNguoiDung;

    @Column(name = "sodienthoai", unique = true, length = 20)
    private String soDienThoai;

    @Column(name = "diachi", columnDefinition = "text")
    private String diaChi;

    @Column(name = "matkhau", nullable = false, columnDefinition = "text")
    private String matKhau;

    @Enumerated(EnumType.STRING)
    @Column(name = "vaitro", nullable = false)
    private UserRole vaiTro;

    public NguoiDung() {
    }

    public NguoiDung(String tenNguoiDung, String soDienThoai, String diaChi, String matKhau, UserRole vaiTro) {
        this.tenNguoiDung = tenNguoiDung;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public void setMaNguoiDung(Long maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public Long getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setVaiTro(UserRole vaiTro) {
        this.vaiTro = vaiTro;
    }

    public UserRole getVaiTro() {
        return vaiTro;
    }

    @Override
    public String toString() {
        return "NguoiDung{" +
                "maNguoiDung=" + maNguoiDung +
                ", tenNguoiDung='" + tenNguoiDung + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", vaiTro=" + vaiTro +
                '}';
    }
    

    public static enum UserRole {
        nvgh,
        ql,
        khachhang,
    }
}
