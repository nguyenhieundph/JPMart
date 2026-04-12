package fpoly.ph34662.jpmart.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.NhanVienAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.NhanVien;

public class QuanLyNhanVien extends AppCompatActivity {
    private RecyclerView rvNhanVien;
    private NhanVienAdapter adapter;
    private DatabaseHelper db;
    private List<NhanVien> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nhan_vien);

        db = new DatabaseHelper(this);
        rvNhanVien = findViewById(R.id.rvNhanVien);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddNhanVien);
        findViewById(R.id.btnBackNV).setOnClickListener(v -> finish());

        loadData();

        fabAdd.setOnClickListener(v -> showDialogNhanVien(null));
    }

    private void loadData() {
        list = db.getAllNhanVien();
        adapter = new NhanVienAdapter(this, list, nv -> showDialogNhanVien(nv));
        rvNhanVien.setLayoutManager(new LinearLayoutManager(this));
        rvNhanVien.setAdapter(adapter);
    }

    private void showDialogNhanVien(NhanVien nv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_nhan_vien, null);
        builder.setView(view);

        TextInputEditText edtMa = view.findViewById(R.id.edtMaNV);
        TextInputEditText edtTen = view.findViewById(R.id.edtTenNV);
        TextInputEditText edtDiaChi = view.findViewById(R.id.edtDiaChiNV);
        TextInputEditText edtLuong = view.findViewById(R.id.edtLuongNV);
        TextInputEditText edtMatKhau = view.findViewById(R.id.edtMatKhauNV);
        Spinner spnChucVu = view.findViewById(R.id.spnChucVu);

        String[] dsChucVu = {"Nhân viên", "Quản lý"};
        ArrayAdapter<String> adapterCV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dsChucVu);
        adapterCV.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChucVu.setAdapter(adapterCV);

        if (nv != null) {
            builder.setTitle("Sửa nhân viên");
            edtMa.setText(nv.getMaNV());
            edtMa.setEnabled(false);
            edtTen.setText(nv.getHoTen());
            edtDiaChi.setText(nv.getDiaChi());
            edtLuong.setText(String.valueOf(nv.getLuong()));
            edtMatKhau.setText(nv.getMatKhau());
            spnChucVu.setSelection(nv.getChucVu());
        } else {
            builder.setTitle("Thêm nhân viên mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();
            String dc = edtDiaChi.getText().toString().trim();
            String luongStr = edtLuong.getText().toString().trim();
            String mk = edtMatKhau.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty() || luongStr.isEmpty() || mk.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            double luong = Double.parseDouble(luongStr);
            int chucVu = spnChucVu.getSelectedItemPosition();

            if (nv == null) {
                db.themNhanVien(new NhanVien(ma, ten, dc, chucVu, luong, mk));
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                db.suaNhanVien(new NhanVien(ma, ten, dc, chucVu, luong, mk));
                Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
            }
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }
}
