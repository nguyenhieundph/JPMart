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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.HoaDonAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.Common;
import fpoly.ph34662.jpmart.model.HoaDon;
import fpoly.ph34662.jpmart.model.HoaDonChiTiet;
import fpoly.ph34662.jpmart.model.KhachHang;
import fpoly.ph34662.jpmart.model.SanPham;

public class HoaDonActivity extends AppCompatActivity {
    private RecyclerView rvHoaDon;
    private HoaDonAdapter adapter;
    private DatabaseHelper db;
    private List<HoaDon> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);

        db = new DatabaseHelper(this);
        rvHoaDon = findViewById(R.id.rvHoaDon);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddHoaDon);
        findViewById(R.id.btnBackHD).setOnClickListener(v -> finish());

        loadData();

        fabAdd.setOnClickListener(v -> showDialogLapHoaDon());
    }

    private void loadData() {
        list = db.getAllHoaDon();
        adapter = new HoaDonAdapter(this, list);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(this));
        rvHoaDon.setAdapter(adapter);
    }

    private void showDialogLapHoaDon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_lap_hoa_don, null);
        builder.setView(view);

        Spinner spnKH = view.findViewById(R.id.spnKhachHangHD);
        Spinner spnSP = view.findViewById(R.id.spnSanPhamHD);
        TextInputEditText edtSoLuong = view.findViewById(R.id.edtSoLuongHD);

        // Load Khách hàng
        List<KhachHang> listKH = db.getAllKhachHang();
        List<String> tenKH = new ArrayList<>();
        for (KhachHang kh : listKH) tenKH.add(kh.getHoTen());
        ArrayAdapter<String> adapterKH = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenKH);
        spnKH.setAdapter(adapterKH);

        // Load Sản phẩm
        List<SanPham> listSP = db.getAllSanPham();
        List<String> tenSP = new ArrayList<>();
        for (SanPham sp : listSP) tenSP.add(sp.getTenSP() + " (" + sp.getGiaBan() + ")");
        ArrayAdapter<String> adapterSP = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenSP);
        spnSP.setAdapter(adapterSP);

        builder.setPositiveButton("Lập hóa đơn", (dialog, which) -> {
            if (listKH.isEmpty() || listSP.isEmpty()) {
                Toast.makeText(this, "Cần có khách hàng và sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            String slStr = edtSoLuong.getText().toString();
            if (slStr.isEmpty()) return;

            int soLuong = Integer.parseInt(slStr);
            KhachHang selectedKH = listKH.get(spnKH.getSelectedItemPosition());
            SanPham selectedSP = listSP.get(spnSP.getSelectedItemPosition());

            // Tạo mã HD tự động theo timestamp
            String maHD = "HD" + System.currentTimeMillis();
            String ngayMua = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            double tongTien = selectedSP.getGiaBan() * soLuong;

            // Lưu vào DB
            db.themHoaDon(new HoaDon(maHD, Common.maNhanVien, selectedKH.getMaKH(), ngayMua, tongTien));
            db.themHDCT(new HoaDonChiTiet(0, maHD, selectedSP.getMaSP(), soLuong, selectedSP.getGiaBan()));

            Toast.makeText(this, "Lập hóa đơn thành công!", Toast.LENGTH_SHORT).show();
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }
}
