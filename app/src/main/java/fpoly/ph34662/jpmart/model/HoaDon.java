package fpoly.ph34662.jpmart.model;

public class HoaDon {
    private String maHD;
    private String maNV;
    private String maKH;
    private String ngayMua;
    private double tongTien;

    public HoaDon() {}

    public HoaDon(String maHD, String maNV, String maKH, String ngayMua, double tongTien) {
        this.maHD = maHD;
        this.maNV = maNV;
        this.maKH = maKH;
        this.ngayMua = ngayMua;
        this.tongTien = tongTien;
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getNgayMua() { return ngayMua; }
    public void setNgayMua(String ngayMua) { this.ngayMua = ngayMua; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
}