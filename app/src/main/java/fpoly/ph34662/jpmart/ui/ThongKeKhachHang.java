package fpoly.ph34662.jpmart.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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
import fpoly.ph34662.jpmart.adapter.TopKhachHangAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.KhachHang;

public class ThongKeKhachHang extends AppCompatActivity {
    private TextInputEditText edtTuNgay, edtDenNgay, edtLimit;
    private RecyclerView rvTopKhachHang;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_khach_hang);

        db = new DatabaseHelper(this);
        initViews();
    }

    private void initViews() {
        edtTuNgay = findViewById(R.id.edtTuNgayKH);
        edtDenNgay = findViewById(R.id.edtDenNgayKH);
        edtLimit = findViewById(R.id.edtLimitKH);
        rvTopKhachHang = findViewById(R.id.rvTopKhachHang);
        Button btnThongKe = findViewById(R.id.btnThongKeKH);
        ImageView btnBack = findViewById(R.id.btnBackKH);

        btnBack.setOnClickListener(v -> finish());

        // Xử lý: Ấn 1 lần lấy focus (nhập tay), ấn lần 2 (khi đã có focus) hiện lịch
        edtTuNgay.setOnClickListener(v -> {
            if (v.isFocused()) {
                showDatePicker(edtTuNgay);
            }
        });

        edtDenNgay.setOnClickListener(v -> {
            if (v.isFocused()) {
                showDatePicker(edtDenNgay);
            }
        });

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

        try {
            int limit = Integer.parseInt(limitStr);
            List<KhachHang> list = db.getTopKhachHang(tuNgay, denNgay, limit);

            if (list.isEmpty()) {
                Toast.makeText(this, "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
            }

            TopKhachHangAdapter adapter = new TopKhachHangAdapter(list);
            rvTopKhachHang.setLayoutManager(new LinearLayoutManager(this));
            rvTopKhachHang.setAdapter(adapter);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
