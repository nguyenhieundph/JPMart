package fpoly.ph34662.jpmart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.Common;
import fpoly.ph34662.jpmart.model.NhanVien;
import fpoly.ph34662.jpmart.ui.BanHangActivity;
import fpoly.ph34662.jpmart.ui.HoaDonActivity;
import fpoly.ph34662.jpmart.ui.LoginActivity;
import fpoly.ph34662.jpmart.ui.QuanLyDanhMuc;
import fpoly.ph34662.jpmart.ui.QuanLyKhachHang;
import fpoly.ph34662.jpmart.ui.QuanLyNhanVien;
import fpoly.ph34662.jpmart.ui.QuanLySanPham;
import fpoly.ph34662.jpmart.ui.ThongKeDoanhThu;
import fpoly.ph34662.jpmart.ui.ThongKeKhachHang;
import fpoly.ph34662.jpmart.ui.ThongKeSanPham;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        // --- CHỨC NĂNG CHÍNH ---

        // --- THỐNG KÊ (Chỉ Admin mới được vào) ---
        setupClick(R.id.cardDoanhThu, ThongKeDoanhThu.class);
        setupClick(R.id.cardTopSP, ThongKeSanPham.class);
        setupClick(R.id.cardTopKH, ThongKeKhachHang.class);

        // --- QUẢN LÝ ---
        setupClick(R.id.cardSanPham, QuanLySanPham.class);
        setupClick(R.id.cardKhachHang, QuanLyKhachHang.class);
        setupClick(R.id.cardHoaDon, HoaDonActivity.class);
        setupClick(R.id.cardDanhMuc, QuanLyDanhMuc.class);
        
        // Quản lý nhân viên cũng chỉ dành cho Admin
        setupClick(R.id.cardNhanVien, QuanLyNhanVien.class);

        // --- NGƯỜI DÙNG ---
        View cardDoiMK = findViewById(R.id.cardDoiMatKhau);
        if (cardDoiMK != null) cardDoiMK.setOnClickListener(v -> showChangePassDialog());

        View cardLogout = findViewById(R.id.cardDangXuat);
        if (cardLogout != null) cardLogout.setOnClickListener(v -> showLogoutDialog());
    }

    /**
     * Thiết lập sự kiện click cho các chức năng
     * Kiểm tra quyền hạn trước khi chuyển màn hình
     */
    private void setupClick(int id, Class<?> target) {
        View view = findViewById(id);
        if (view != null) {
            view.setOnClickListener(v -> {
                NhanVien nv = db.layNhanVienBangMaNV(Common.maNhanVien);
                if (nv != null) {
                    // Nếu là tài khoản Staff (chucVu = 0)
                    if (nv.getChucVu() == 0) {
                        // Danh sách các lớp bị hạn chế đối với Staff
                        if (target == ThongKeDoanhThu.class || 
                            target == ThongKeSanPham.class || 
                            target == ThongKeKhachHang.class ||
                            target == QuanLyNhanVien.class) {
                            
                            Toast.makeText(this, "Tài khoản nhân viên không được sử dụng chức năng này!", Toast.LENGTH_SHORT).show();
                            return; // Không cho phép chuyển màn hình
                        }
                    }
                }
                // Nếu là Admin hoặc chức năng không bị hạn chế thì cho phép vào
                startActivity(new Intent(this, target));
            });
        }
    }

    private void showChangePassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextInputEditText edtOldPass = view.findViewById(R.id.edtOldPass);
        TextInputEditText edtNewPass = view.findViewById(R.id.edtNewPass);
        TextInputEditText edtConfirmPass = view.findViewById(R.id.edtConfirmPass);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        if (btnCancel != null) btnCancel.setOnClickListener(v -> dialog.dismiss());

        if (btnUpdate != null) {
            btnUpdate.setOnClickListener(v -> {
                String oldPass = edtOldPass != null && edtOldPass.getText() != null ? edtOldPass.getText().toString().trim() : "";
                String newPass = edtNewPass != null && edtNewPass.getText() != null ? edtNewPass.getText().toString().trim() : "";
                String confirmPass = edtConfirmPass != null && edtConfirmPass.getText() != null ? edtConfirmPass.getText().toString().trim() : "";

                if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                NhanVien nv = db.layNhanVienBangMaNV(Common.maNhanVien);
                if (nv != null && nv.getMatKhau().equals(oldPass)) {
                    if (newPass.equals(confirmPass)) {
                        if (db.updatePassword(Common.maNhanVien, newPass)) {
                            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(this, "Lỗi cập nhật!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        dialog.show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn thoát?")
                .setPositiveButton("Đăng xuất", (d, which) -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", (d, which) -> d.dismiss())
                .show();
    }
}
