package fpoly.ph34662.jpmart.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.TopSanPhamAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.SanPham;

public class ThongKeSanPham extends AppCompatActivity {
    private TextInputEditText edtTuNgay, edtDenNgay, edtLimit;
    private RecyclerView rvTopSanPham;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_san_pham);

        db = new DatabaseHelper(this);
        initViews();
    }

    private void initViews() {
        edtTuNgay = findViewById(R.id.edtTuNgaySP);
        edtDenNgay = findViewById(R.id.edtDenNgaySP);
        edtLimit = findViewById(R.id.edtLimit);
        rvTopSanPham = findViewById(R.id.rvTopSanPham);
        Button btnThongKe = findViewById(R.id.btnThongKeSP);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
        edtTuNgay.setOnClickListener(v -> showDatePicker(edtTuNgay));
        edtDenNgay.setOnClickListener(v -> showDatePicker(edtDenNgay));

        btnThongKe.setOnClickListener(v -> handleThongKe());
    }

    private void handleThongKe() {
        String tuNgay = edtTuNgay.getText().toString();
        String denNgay = edtDenNgay.getText().toString();
        String limitStr = edtLimit.getText().toString();

        if (tuNgay.isEmpty() || denNgay.isEmpty() || limitStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int limit = Integer.parseInt(limitStr);
        List<SanPham> list = db.getTopSanPham(tuNgay, denNgay, limit);
        
        if (list.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu trong khoảng thời gian này!", Toast.LENGTH_SHORT).show();
        }

        TopSanPhamAdapter adapter = new TopSanPhamAdapter(list);
        rvTopSanPham.setLayoutManager(new LinearLayoutManager(this));
        rvTopSanPham.setAdapter(adapter);
    }

    private void showDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}