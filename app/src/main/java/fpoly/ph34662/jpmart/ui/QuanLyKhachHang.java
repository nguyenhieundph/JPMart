package fpoly.ph34662.jpmart.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.KhachHangAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.KhachHang;

public class QuanLyKhachHang extends AppCompatActivity {
    private RecyclerView rvKhachHang;
    private KhachHangAdapter adapter;
    private DatabaseHelper db;
    private List<KhachHang> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khach_hang);

        db = new DatabaseHelper(this);
        rvKhachHang = findViewById(R.id.rvKhachHang);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddKhachHang);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        loadData();

        fabAdd.setOnClickListener(v -> showDialogKhachHang(null));
    }

    private void loadData() {
        list = db.getAllKhachHang();
        adapter = new KhachHangAdapter(this, list, kh -> showDialogKhachHang(kh));
        rvKhachHang.setLayoutManager(new LinearLayoutManager(this));
        rvKhachHang.setAdapter(adapter);
    }

    private void showDialogKhachHang(KhachHang kh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_khach_hang, null);
        builder.setView(view);

        TextInputEditText edtMa = view.findViewById(R.id.edtMaKH);
        TextInputEditText edtTen = view.findViewById(R.id.edtTenKH);
        TextInputEditText edtSDT = view.findViewById(R.id.edtSDTKH);
        TextInputEditText edtDiaChi = view.findViewById(R.id.edtDiaChiKH);
        TextInputEditText edtEmail = view.findViewById(R.id.edtEmailKH);

        if (kh != null) {
            builder.setTitle("Sửa khách hàng");
            edtMa.setText(kh.getMaKH());
            edtMa.setEnabled(false);
            edtTen.setText(kh.getHoTen());
            edtSDT.setText(kh.getSoDienThoai());
            edtDiaChi.setText(kh.getDiaChi());
            edtEmail.setText(kh.getEmail());
        } else {
            builder.setTitle("Thêm khách hàng mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String dc = edtDiaChi.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (kh == null) {
                db.themKhachHang(new KhachHang(ma, ten, dc, sdt, email));
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                db.suaKhachHang(new KhachHang(ma, ten, dc, sdt, email));
                Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
            }
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }
}
