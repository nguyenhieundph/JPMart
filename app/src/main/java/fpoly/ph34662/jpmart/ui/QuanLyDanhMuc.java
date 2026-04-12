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
import fpoly.ph34662.jpmart.adapter.DanhMucAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.DanhMuc;

public class QuanLyDanhMuc extends AppCompatActivity {
    private RecyclerView rvDanhMuc;
    private DanhMucAdapter adapter;
    private DatabaseHelper db;
    private List<DanhMuc> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        db = new DatabaseHelper(this);
        rvDanhMuc = findViewById(R.id.rvDanhMuc);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddDanhMuc);
        findViewById(R.id.btnBackDM).setOnClickListener(v -> finish());

        loadData();

        fabAdd.setOnClickListener(v -> showDialogDanhMuc(null));
    }

    private void loadData() {
        list = db.getAllDanhMuc();
        adapter = new DanhMucAdapter(this, list, dm -> showDialogDanhMuc(dm));
        rvDanhMuc.setLayoutManager(new LinearLayoutManager(this));
        rvDanhMuc.setAdapter(adapter);
    }

    private void showDialogDanhMuc(DanhMuc dm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_danh_muc, null);
        builder.setView(view);

        TextInputEditText edtMa = view.findViewById(R.id.edtMaDM);
        TextInputEditText edtTen = view.findViewById(R.id.edtTenDM);

        if (dm != null) {
            builder.setTitle("Sửa danh mục");
            edtMa.setText(dm.getMaDM());
            edtMa.setEnabled(false);
            edtTen.setText(dm.getTenDM());
        } else {
            builder.setTitle("Thêm danh mục mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String ma = edtMa.getText().toString().trim();
            String ten = edtTen.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dm == null) {
                db.themDanhMuc(new DanhMuc(ma, ten));
                Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            } else {
                db.suaDanhMuc(new DanhMuc(ma, ten));
                Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
            }
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }
}
