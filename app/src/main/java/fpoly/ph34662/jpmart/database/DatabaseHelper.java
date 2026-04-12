package fpoly.ph34662.jpmart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fpoly.ph34662.jpmart.model.DanhMuc;
import fpoly.ph34662.jpmart.model.HoaDon;
import fpoly.ph34662.jpmart.model.HoaDonChiTiet;
import fpoly.ph34662.jpmart.model.KhachHang;
import fpoly.ph34662.jpmart.model.NhanVien;
import fpoly.ph34662.jpmart.model.SanPham;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JPMart.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NhanVien (" +
                "maNV TEXT PRIMARY KEY, " +
                "hoTen TEXT, " +
                "diaChi TEXT, " +
                "chucVu INTEGER, " +
                "luong REAL, " +
                "matKhau TEXT)");

        db.execSQL("CREATE TABLE DanhMuc (" +
                "maDM TEXT PRIMARY KEY, " +
                "tenDM TEXT)");

        db.execSQL("CREATE TABLE SanPham (" +
                "maSP TEXT PRIMARY KEY, " +
                "tenSP TEXT, " +
                "giaBan REAL, " +
                "soLuong INTEGER, " +
                "donViTinh TEXT, " +
                "ngayNhap TEXT, " +
                "maDM TEXT, " +
                "FOREIGN KEY(maDM) REFERENCES DanhMuc(maDM))");

        db.execSQL("CREATE TABLE KhachHang (" +
                "maKH TEXT PRIMARY KEY, " +
                "hoTen TEXT, " +
                "diaChi TEXT, " +
                "soDienThoai TEXT, " +
                "email TEXT)");

        db.execSQL("CREATE TABLE HoaDon (" +
                "maHD TEXT PRIMARY KEY, " +
                "maNV TEXT, " +
                "maKH TEXT, " +
                "ngayMua TEXT, " +
                "tongTien REAL, " +
                "FOREIGN KEY(maNV) REFERENCES NhanVien(maNV), " +
                "FOREIGN KEY(maKH) REFERENCES KhachHang(maKH))");

        db.execSQL("CREATE TABLE HoaDonChiTiet (" +
                "maHDCT INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "maHD TEXT, " +
                "maSP TEXT, " +
                "soLuong INTEGER, " +
                "giaBan REAL, " +
                "FOREIGN KEY(maHD) REFERENCES HoaDon(maHD), " +
                "FOREIGN KEY(maSP) REFERENCES SanPham(maSP))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS HoaDonChiTiet");
        db.execSQL("DROP TABLE IF EXISTS HoaDon");
        db.execSQL("DROP TABLE IF EXISTS KhachHang");
        db.execSQL("DROP TABLE IF EXISTS SanPham");
        db.execSQL("DROP TABLE IF EXISTS DanhMuc");
        db.execSQL("DROP TABLE IF EXISTS NhanVien");
        onCreate(db);
    }

    // --- QUẢN LÝ NHÂN VIÊN ---
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM NhanVien", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new NhanVien(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getDouble(4), cursor.getString(5)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void themNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maNV", nv.getMaNV()); v.put("hoTen", nv.getHoTen()); v.put("diaChi", nv.getDiaChi());
        v.put("chucVu", nv.getChucVu()); v.put("luong", nv.getLuong()); v.put("matKhau", nv.getMatKhau());
        db.insert("NhanVien", null, v);
    }

    public boolean suaNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("hoTen", nv.getHoTen()); v.put("diaChi", nv.getDiaChi());
        v.put("chucVu", nv.getChucVu()); v.put("luong", nv.getLuong()); v.put("matKhau", nv.getMatKhau());
        return db.update("NhanVien", v, "maNV=?", new String[]{nv.getMaNV()}) > 0;
    }

    public boolean xoaNhanVien(String maNV) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("NhanVien", "maNV=?", new String[]{maNV}) > 0;
    }

    // --- QUẢN LÝ DANH MỤC ---
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DanhMuc", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new DanhMuc(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void themDanhMuc(DanhMuc dm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maDM", dm.getMaDM()); v.put("tenDM", dm.getTenDM());
        db.insert("DanhMuc", null, v);
    }

    public boolean suaDanhMuc(DanhMuc dm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("tenDM", dm.getTenDM());
        return db.update("DanhMuc", v, "maDM=?", new String[]{dm.getMaDM()}) > 0;
    }

    public boolean xoaDanhMuc(String maDM) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("DanhMuc", "maDM=?", new String[]{maDM}) > 0;
    }

    // --- QUẢN LÝ SẢN PHẨM ---
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM SanPham", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new SanPham(cursor.getString(0), cursor.getString(1), cursor.getDouble(2),
                        cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void themSanPham(SanPham sp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maSP", sp.getMaSP()); v.put("tenSP", sp.getTenSP()); v.put("giaBan", sp.getGiaBan());
        v.put("soLuong", sp.getSoLuong()); v.put("donViTinh", sp.getDonViTinh());
        v.put("ngayNhap", sp.getNgayNhap()); v.put("maDM", sp.getMaDM());
        db.insert("SanPham", null, v);
    }

    public boolean suaSanPham(SanPham sp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("tenSP", sp.getTenSP()); v.put("giaBan", sp.getGiaBan()); v.put("soLuong", sp.getSoLuong());
        v.put("donViTinh", sp.getDonViTinh()); v.put("maDM", sp.getMaDM());
        return db.update("SanPham", v, "maSP=?", new String[]{sp.getMaSP()}) > 0;
    }

    public boolean truSoLuongSanPham(String maSP, int soLuongMua) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE SanPham SET soLuong = soLuong - " + soLuongMua + " WHERE maSP = ?", new String[]{maSP});
        return true;
    }

    public boolean xoaSanPham(String maSP) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("SanPham", "maSP=?", new String[]{maSP}) > 0;
    }

    // --- QUẢN LÝ KHÁCH HÀNG ---
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM KhachHang", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new KhachHang(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void themKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maKH", kh.getMaKH()); v.put("hoTen", kh.getHoTen()); v.put("diaChi", kh.getDiaChi());
        v.put("soDienThoai", kh.getSoDienThoai()); v.put("email", kh.getEmail());
        db.insert("KhachHang", null, v);
    }

    public boolean suaKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("hoTen", kh.getHoTen()); v.put("diaChi", kh.getDiaChi());
        v.put("soDienThoai", kh.getSoDienThoai()); v.put("email", kh.getEmail());
        return db.update("KhachHang", v, "maKH=?", new String[]{kh.getMaKH()}) > 0;
    }

    public boolean xoaKhachHang(String maKH) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("KhachHang", "maKH=?", new String[]{maKH}) > 0;
    }

    // --- QUẢN LÝ HÓA ĐƠN ---
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HoaDon", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(new HoaDon(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean xoaHoaDon(String maHD) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("HoaDonChiTiet", "maHD=?", new String[]{maHD});
        return db.delete("HoaDon", "maHD=?", new String[]{maHD}) > 0;
    }

    public String getTenKhachHang(String maKH) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("KhachHang", new String[]{"hoTen"}, "maKH=?", new String[]{maKH}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String ten = cursor.getString(0);
            cursor.close();
            return ten;
        }
        return "Nặc danh";
    }

    public void themHoaDon(HoaDon hd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maHD", hd.getMaHD()); v.put("maNV", hd.getMaNV()); v.put("maKH", hd.getMaKH());
        v.put("ngayMua", hd.getNgayMua()); v.put("tongTien", hd.getTongTien());
        db.insert("HoaDon", null, v);
    }

    public void themHDCT(HoaDonChiTiet hdct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("maHD", hdct.getMaHD()); v.put("maSP", hdct.getMaSP());
        v.put("soLuong", hdct.getSoLuong()); v.put("giaBan", hdct.getGiaBan());
        db.insert("HoaDonChiTiet", null, v);
    }

    // --- THỐNG KÊ ---
    public List<SanPham> getTopSanPham(String tuNgay, String denNgay, int limit) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sp.maSP, sp.tenSP, SUM(hdct.soLuong) as totalSold FROM SanPham sp JOIN HoaDonChiTiet hdct ON sp.maSP = hdct.maSP JOIN HoaDon hd ON hdct.maHD = hd.maHD WHERE hd.ngayMua BETWEEN ? AND ? GROUP BY sp.maSP ORDER BY totalSold DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{tuNgay, denNgay, String.valueOf(limit)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham();
                sp.setMaSP(cursor.getString(0)); sp.setTenSP(cursor.getString(1)); sp.setSoLuong(cursor.getInt(2));
                list.add(sp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<KhachHang> getTopKhachHang(String tuNgay, String denNgay, int limit) {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT kh.maKH, kh.hoTen, SUM(hd.tongTien) as totalSpent FROM KhachHang kh JOIN HoaDon hd ON kh.maKH = hd.maKH WHERE hd.ngayMua BETWEEN ? AND ? GROUP BY kh.maKH ORDER BY totalSpent DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{tuNgay, denNgay, String.valueOf(limit)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                KhachHang kh = new KhachHang();
                kh.setMaKH(cursor.getString(0)); kh.setHoTen(cursor.getString(1)); kh.setSoDienThoai(String.valueOf(cursor.getDouble(2)));
                list.add(kh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public double getDoanhThu(String tuNgay, String denNgay) {
        SQLiteDatabase db = this.getReadableDatabase();
        double doanhThu = 0;
        String query = "SELECT SUM(tongTien) FROM HoaDon WHERE ngayMua BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{tuNgay, denNgay});
        if (cursor != null && cursor.moveToFirst()) {
            doanhThu = cursor.getDouble(0);
            cursor.close();
        }
        return doanhThu;
    }

    public NhanVien layNhanVienBangMaNV(String maNV) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("NhanVien", null, "maNV=?", new String[]{maNV}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            NhanVien nv = new NhanVien(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getDouble(4), cursor.getString(5));
            cursor.close();
            return nv;
        }
        return null;
    }

    public boolean updatePassword(String maNV, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("matKhau", newPass);
        return db.update("NhanVien", v, "maNV=?", new String[]{maNV}) > 0;
    }
}