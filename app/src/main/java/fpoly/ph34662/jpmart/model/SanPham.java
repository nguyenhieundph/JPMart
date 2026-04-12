package fpoly.ph34662.jpmart.model;

import androidx.annotation.NonNull;

public class SanPham {
    private String maSP;
    private String tenSP;
    private double giaBan;
    private int soLuong; // Số lượng tồn kho
    private String donViTinh;
    private String ngayNhap;
    private String maDM;
    private int soLuongTrongGio = 0; // Số lượng chọn mua

    public SanPham() {}

    public SanPham(String maSP, String tenSP, double giaBan, int soLuong, String donViTinh, String ngayNhap, String maDM) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.donViTinh = donViTinh;
        this.ngayNhap = ngayNhap;
        this.maDM = maDM;
    }

    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }
    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
    public String getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(String ngayNhap) { this.ngayNhap = ngayNhap; }
    public String getMaDM() { return maDM; }
    public void setMaDM(String maDM) { this.maDM = maDM; }

    public int getSoLuongTrongGio() { return soLuongTrongGio; }
    public void setSoLuongTrongGio(int soLuongTrongGio) { this.soLuongTrongGio = soLuongTrongGio; }

    @NonNull
    @Override
    public String toString() {
        return tenSP + " - " + giaBan + "đ (Kho: " + soLuong + ")";
    }
}