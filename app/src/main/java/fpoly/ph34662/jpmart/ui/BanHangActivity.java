package fpoly.ph34662.jpmart.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.GioHangAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.Common;
import fpoly.ph34662.jpmart.model.HoaDon;
import fpoly.ph34662.jpmart.model.HoaDonChiTiet;
import fpoly.ph34662.jpmart.model.KhachHang;
import fpoly.ph34662.jpmart.model.SanPham;

public class BanHangActivity extends AppCompatActivity {

    private Spinner spnKhachHang;
    private RecyclerView rvGioHang;
    private TextView txtTongTien, txtGioHangTrong;
    private Button btnThanhToan;
    private ImageView btnBack;
    private DatabaseHelper db;
    private List<KhachHang> listKH;
    private KhachHang selectedKH;
    private GioHangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_hang);

        db = new DatabaseHelper(this);
        initViews();
        loadData();
        checkCartStatus();
        tinhTongTien();

        btnBack.setOnClickListener(v -> finish());

        spnKhachHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKH = listKH.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnThanhToan.setOnClickListener(v -> handleThanhToan());
    }

    private void initViews() {
        spnKhachHang = findViewById(R.id.spnKhachHangBH);
        rvGioHang = findViewById(R.id.rvGioHang);
        txtTongTien = findViewById(R.id.txtTongTienBH);
        txtGioHangTrong = findViewById(R.id.txtGioHangTrong);
        btnThanhToan = findViewById(R.id.btnThanhToanBH);
        btnBack = findViewById(R.id.btnBackBH);
    }

    private void loadData() {
        listKH = db.getAllKhachHang();
        ArrayAdapter<KhachHang> adapterKH = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listKH);
        adapterKH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKhachHang.setAdapter(adapterKH);

        adapter = new GioHangAdapter(this, Common.cart, () -> {
            checkCartStatus();
            tinhTongTien();
        });
        rvGioHang.setLayoutManager(new LinearLayoutManager(this));
        rvGioHang.setAdapter(adapter);
    }

    private void checkCartStatus() {
        if (Common.cart.isEmpty()) {
            txtGioHangTrong.setVisibility(View.VISIBLE);
            rvGioHang.setVisibility(View.GONE);
        } else {
            txtGioHangTrong.setVisibility(View.GONE);
            rvGioHang.setVisibility(View.VISIBLE);
        }
    }

    private void tinhTongTien() {
        double tong = 0;
        for (SanPham sp : Common.cart) {
            tong += (sp.getGiaBan() * sp.getSoLuongTrongGio());
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        txtTongTien.setText("Tổng tiền: " + format.format(tong));
    }

    private void handleThanhToan() {
        if (Common.cart.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedKH == null) {
            Toast.makeText(this, "Vui lòng chọn khách hàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        String maHD = "HD" + System.currentTimeMillis();
        String ngayMua = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        double tongTien = 0;
        for (SanPham sp : Common.cart) {
            tongTien += (sp.getGiaBan() * sp.getSoLuongTrongGio());
        }

        db.themHoaDon(new HoaDon(maHD, Common.maNhanVien, selectedKH.getMaKH(), ngayMua, tongTien));

        for (SanPham sp : Common.cart) {
            db.themHDCT(new HoaDonChiTiet(0, maHD, sp.getMaSP(), sp.getSoLuongTrongGio(), sp.getGiaBan()));
            db.truSoLuongSanPham(sp.getMaSP(), sp.getSoLuongTrongGio());
        }

        Common.clearCart();
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
        finish();
    }
}
