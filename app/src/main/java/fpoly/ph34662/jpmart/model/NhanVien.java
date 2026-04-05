package fpoly.ph34662.jpmart.model;

public class NhanVien {
    private String maNV;
    private String hoTen;
    private String diaChi;
    private int chucVu; // 1: Admin, 0: Nhan Vien
    private double luong;
    private String matKhau;

    public NhanVien() {}

    public NhanVien(String maNV, String hoTen, String diaChi, int chucVu, double luong, String matKhau) {
        this.maNV = maNV;
        this.hoTen = hoTen;
        this.diaChi = diaChi;
        this.chucVu = chucVu;
        this.luong = luong;
        this.matKhau = matKhau;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public int getChucVu() { return chucVu; }
    public void setChucVu(int chucVu) { this.chucVu = chucVu; }
    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
}