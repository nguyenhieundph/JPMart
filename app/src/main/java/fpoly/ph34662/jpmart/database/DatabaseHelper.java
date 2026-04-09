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

    // --- Thống kê Top Khách hàng ---
    public List<KhachHang> getTopKhachHang(String tuNgay, String denNgay, int limit) {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT kh.maKH, kh.hoTen, SUM(hd.tongTien) as totalSpent " +
                "FROM KhachHang kh " +
                "JOIN HoaDon hd ON kh.maKH = hd.maKH " +
                "WHERE hd.ngayMua BETWEEN ? AND ? " +
                "GROUP BY kh.maKH " +
                "ORDER BY totalSpent DESC LIMIT ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{tuNgay, denNgay, String.valueOf(limit)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                KhachHang kh = new KhachHang();
                kh.setMaKH(cursor.getString(0));
                kh.setHoTen(cursor.getString(1));
                kh.setSoDienThoai(String.valueOf(cursor.getDouble(2))); // Dùng tạm trường SĐT để lưu tổng tiền
                list.add(kh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // --- Methods for SanPham/NhanVien/HoaDon giữ nguyên ---
    public List<SanPham> getTopSanPham(String tuNgay, String denNgay, int limit) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sp.maSP, sp.tenSP, SUM(hdct.soLuong) as totalSold " +
                "FROM SanPham sp " +
                "JOIN HoaDonChiTiet hdct ON sp.maSP = hdct.maSP " +
                "JOIN HoaDon hd ON hdct.maHD = hd.maHD " +
                "WHERE hd.ngayMua BETWEEN ? AND ? " +
                "GROUP BY sp.maSP " +
                "ORDER BY totalSold DESC LIMIT ?";
        Cursor cursor = db.rawQuery(query, new String[]{tuNgay, denNgay, String.valueOf(limit)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SanPham sp = new SanPham();
                sp.setMaSP(cursor.getString(0));
                sp.setTenSP(cursor.getString(1));
                sp.setSoLuong(cursor.getInt(2)); 
                list.add(sp);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public void themNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maNV", nv.getMaNV());
        values.put("hoTen", nv.getHoTen());
        values.put("diaChi", nv.getDiaChi());
        values.put("chucVu", nv.getChucVu());
        values.put("luong", nv.getLuong());
        values.put("matKhau", nv.getMatKhau());
        db.insert("NhanVien", null, values);
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
        ContentValues values = new ContentValues();
        values.put("matKhau", newPass);
        return db.update("NhanVien", values, "maNV=?", new String[]{maNV}) > 0;
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

    public void themDanhMuc(DanhMuc dm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maDM", dm.getMaDM());
        values.put("tenDM", dm.getTenDM());
        db.insert("DanhMuc", null, values);
    }

    public void themSanPham(SanPham sp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maSP", sp.getMaSP());
        values.put("tenSP", sp.getTenSP());
        values.put("giaBan", sp.getGiaBan());
        values.put("soLuong", sp.getSoLuong());
        values.put("donViTinh", sp.getDonViTinh());
        values.put("ngayNhap", sp.getNgayNhap());
        values.put("maDM", sp.getMaDM());
        db.insert("SanPham", null, values);
    }

    public double layDonGiaSanPham(String maSP) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("SanPham", new String[]{"giaBan"}, "maSP=?", new String[]{maSP}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            double gia = cursor.getDouble(0);
            cursor.close();
            return gia;
        }
        return 0;
    }

    public void themKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maKH", kh.getMaKH());
        values.put("hoTen", kh.getHoTen());
        values.put("diaChi", kh.getDiaChi());
        values.put("soDienThoai", kh.getSoDienThoai());
        values.put("email", kh.getEmail());
        db.insert("KhachHang", null, values);
    }

    public void themHoaDon(HoaDon hd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maHD", hd.getMaHD());
        values.put("maNV", hd.getMaNV());
        values.put("maKH", hd.getMaKH());
        values.put("ngayMua", hd.getNgayMua());
        values.put("tongTien", hd.getTongTien());
        db.insert("HoaDon", null, values);
    }

    public void capNhatTongTienHoaDon(String maHD, double tongTien) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tongTien", tongTien);
        db.update("HoaDon", values, "maHD=?", new String[]{maHD});
    }

    public void themHDCT(HoaDonChiTiet hdct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("maHD", hdct.getMaHD());
        values.put("maSP", hdct.getMaSP());
        values.put("soLuong", hdct.getSoLuong());
        values.put("giaBan", hdct.getGiaBan());
        db.insert("HoaDonChiTiet", null, values);
    }
}