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
import fpoly.ph34662.jpmart.ui.LoginActivity;
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

        // --- THỐNG KÊ ---
        View cardDoanhThu = findViewById(R.id.cardDoanhThu);
        if (cardDoanhThu != null) {
            cardDoanhThu.setOnClickListener(v -> startActivity(new Intent(this, ThongKeDoanhThu.class)));
        }

        View cardTopSP = findViewById(R.id.cardTopSP);
        if (cardTopSP != null) {
            cardTopSP.setOnClickListener(v -> startActivity(new Intent(this, ThongKeSanPham.class)));
        }

        View cardTopKH = findViewById(R.id.cardTopKH);
        if (cardTopKH != null) {
            cardTopKH.setOnClickListener(v -> startActivity(new Intent(this, ThongKeKhachHang.class)));
        }

        // --- QUẢN LÝ ---
        // (Bạn có thể thêm các sự kiện click cho Sản phẩm, Khách hàng, Hóa đơn... tại đây)

        // --- NGƯỜI DÙNG ---
        View cardDoiMK = findViewById(R.id.cardDoiMatKhau);
        if (cardDoiMK != null) {
            cardDoiMK.setOnClickListener(v -> showChangePassDialog());
        }

        View cardLogout = findViewById(R.id.cardDangXuat);
        if (cardLogout != null) {
            cardLogout.setOnClickListener(v -> showLogoutDialog());
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