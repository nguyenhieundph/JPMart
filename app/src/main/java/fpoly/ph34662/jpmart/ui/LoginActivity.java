package fpoly.ph34662.jpmart.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import fpoly.ph34662.jpmart.MainActivity;
import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.Common;
import fpoly.ph34662.jpmart.model.DanhMuc;
import fpoly.ph34662.jpmart.model.HoaDon;
import fpoly.ph34662.jpmart.model.HoaDonChiTiet;
import fpoly.ph34662.jpmart.model.KhachHang;
import fpoly.ph34662.jpmart.model.NhanVien;
import fpoly.ph34662.jpmart.model.SanPham;


public class LoginActivity extends AppCompatActivity {
    private TextInputEditText edtUsername, edtPassword;
    private CheckBox cbkRemember;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ ID chính xác từ layout mới
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        cbkRemember = findViewById(R.id.cbkRemember);
        Button btnLogin = findViewById(R.id.btnLogin);

        db = new DatabaseHelper(this);
        
        // Khởi tạo dữ liệu mẫu nếu là lần đầu chạy
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean isInit = prefs.getBoolean("init", false);
        if (!isInit) {
            taoDuLieuHeThong();
            prefs.edit().putBoolean("init", true).apply();
        }

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        loadSavedAccount();
        
        btnLogin.setOnClickListener(view -> handleLogin());
    }

    private void loadSavedAccount() {
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        boolean isRemembered = sharedPreferences.getBoolean("remember", false);

        if (isRemembered) {
            edtUsername.setText(savedUsername);
            edtPassword.setText(savedPassword);
            cbkRemember.setChecked(true);
        }
    }

    private void handleLogin() {
        String maNhanVien = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (maNhanVien.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        NhanVien nhanVien = db.layNhanVienBangMaNV(maNhanVien);

        if (nhanVien != null) {
            if (password.equals(nhanVien.getMatKhau())) {
                saveAccountPreferences(maNhanVien, password);
                
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("CHUC_VU", nhanVien.getChucVu());
                startActivity(intent);
                Common.maNhanVien = maNhanVien;
                finish();
            } else {
                Toast.makeText(this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAccountPreferences(String user, String pass) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (cbkRemember.isChecked()) {
            editor.putString("username", user);
            editor.putString("password", pass);
            editor.putBoolean("remember", true);
        } else {
            editor.clear();
        }
        editor.apply();
    }

    private void taoDuLieuHeThong() {
        taoDuLieuNhanVien();
        taoDuLieuDanhMuc();
        taoDuLieuSanPham();
        taoDuLieuKhachHang();
        taoDuLieuHoaDon();
        taoDuLieuHoaDonChiTiet();
    }

    // Các phương thức tạo dữ liệu giữ nguyên (rút gọn để tránh quá dài)
    public void taoDuLieuSanPham() {
        db.themSanPham(new SanPham("SP001", "Nước ngọt Calpis", 12000, 50, "Lon", "2024-02-08", "DM001"));
        db.themSanPham(new SanPham("SP002", "Trà xanh Ito En", 10000, 40, "Lon", "2024-02-08", "DM001"));
        db.themSanPham(new SanPham("SP003", "Bánh Pocky", 25000, 30, "Hộp", "2024-02-07", "DM002"));
        db.themSanPham(new SanPham("SP004", "Sữa Meiji", 15000, 20, "Hộp", "2024-02-06", "DM003"));
        db.themSanPham(new SanPham("SP005", "Mì Udon", 5000, 100, "Gói", "2024-02-05", "DM004"));
    }

    public void taoDuLieuKhachHang() {
        db.themKhachHang(new KhachHang("KH001", "Nặc danh", "", "0000", ""));
        db.themKhachHang(new KhachHang("KH002", "Trần Thị Bích", "456 Đường XYZ, Hà Nội", "0988777666", "tranthibich@example.com"));
    }

    public void taoDuLieuDanhMuc() {
        db.themDanhMuc(new DanhMuc("DM001", "Đồ uống"));
        db.themDanhMuc(new DanhMuc("DM002", "Bánh kẹo"));
    }

    public void taoDuLieuNhanVien() {
        db.themNhanVien(new NhanVien("NV001", "Admin", "TP.HCM", 1, 25000000, "admin123"));
        db.themNhanVien(new NhanVien("NV002", "Staff", "TP.HCM", 0, 12000000, "staff123"));
    }

    public void taoDuLieuHoaDon() {
        db.themHoaDon(new HoaDon("HD001", "NV001", "KH001", "2024-01-05", 180000));
    }

    public void taoDuLieuHoaDonChiTiet() {
        db.themHDCT(new HoaDonChiTiet(1, "HD001", "SP001", 2, 12000));
    }
}