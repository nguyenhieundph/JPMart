package fpoly.ph34662.jpmart.model;

public class HoaDonChiTiet {
    private int maHDCT;
    private String maHD;
    private String maSP;
    private int soLuong;
    private double giaBan;

    public HoaDonChiTiet() {}

    public HoaDonChiTiet(int maHDCT, String maHD, String maSP, int soLuong, double giaBan) {
        this.maHDCT = maHDCT;
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
    }

    public int getMaHDCT() { return maHDCT; }
    public void setMaHDCT(int maHDCT) { this.maHDCT = maHDCT; }
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public String getMaSP() { return maSP; }
    public void setMaSP(String maSP) { this.maSP = maSP; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getGiaBan() { return giaBan; }
    public void setGiaBan(double giaBan) { this.giaBan = giaBan; }
}