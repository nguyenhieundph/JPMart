package fpoly.ph34662.jpmart.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.database.DatabaseHelper;

public class ThongKeDoanhThu extends AppCompatActivity {
    private TextInputEditText edtTuNgay, edtDenNgay;
    private TextView txtDoanhThu;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_doanh_thu);

        db = new DatabaseHelper(this);

        edtTuNgay = findViewById(R.id.edtTuNgay);
        edtDenNgay = findViewById(R.id.edtDenNgay);
        txtDoanhThu = findViewById(R.id.txtDoanhThu);
        Button btnThongKe = findViewById(R.id.btnThongKe);
        ImageView btnBack = findViewById(R.id.btnBack);

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

        btnThongKe.setOnClickListener(v -> {
            String tuNgay = edtTuNgay.getText().toString();
            String denNgay = edtDenNgay.getText().toString();

            if (tuNgay.isEmpty() || denNgay.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ ngày!", Toast.LENGTH_SHORT).show();
                return;
            }

            double doanhThu = db.getDoanhThu(tuNgay, denNgay);

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            txtDoanhThu.setText(format.format(doanhThu));
        });
    }

    private void showDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            editText.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}