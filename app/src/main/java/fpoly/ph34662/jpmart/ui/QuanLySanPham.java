package fpoly.ph34662.jpmart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.SanPhamAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.Common;
import fpoly.ph34662.jpmart.model.DanhMuc;
import fpoly.ph34662.jpmart.model.SanPham;

public class QuanLySanPham extends AppCompatActivity {
    private RecyclerView rvSanPham;
    private DatabaseHelper db;
    private SanPhamAdapter adapter;
    private List<SanPham> listFull;
    private TextView txtCartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        db = new DatabaseHelper(this);
        rvSanPham = findViewById(R.id.rvSanPham);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddSanPham);
        EditText edtSearch = findViewById(R.id.edtSearchSP);
        txtCartBadge = findViewById(R.id.txtCartBadge);
        View btnCart = findViewById(R.id.btnCart);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        loadData();
        updateBadge();

        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        if (btnCart != null) {
            btnCart.setOnClickListener(v -> {
                // Cho phép vào giỏ hàng ngay cả khi trống
                startActivity(new Intent(this, BanHangActivity.class));
            });
        }

        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> showDialogSanPham(null));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
    }

    private void updateBadge() {
        if (txtCartBadge != null) {
            int totalQty = 0;
            for (SanPham sp : Common.cart) {
                totalQty += sp.getSoLuongTrongGio();
            }
            txtCartBadge.setText(String.valueOf(totalQty));
            txtCartBadge.setVisibility(totalQty > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void loadData() {
        listFull = db.getAllSanPham();
        adapter = new SanPhamAdapter(this, new ArrayList<>(listFull), new SanPhamAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(SanPham sp) {
                showDialogSanPham(sp);
            }

            @Override
            public void onAddToCart(SanPham sp) {
                boolean isExist = false;
                for (SanPham item : Common.cart) {
                    if (item.getMaSP().equals(sp.getMaSP())) {
                        if (item.getSoLuongTrongGio() < item.getSoLuong()) {
                            item.setSoLuongTrongGio(item.getSoLuongTrongGio() + 1);
                        } else {
                        }
                        isExist = true;
                        break;
                    }
                }
                
                if (!isExist) {
                    if (sp.getSoLuong() > 0) {
                        sp.setSoLuongTrongGio(1);
                        Common.cart.add(sp);
                    } else {
                    }
                }
                updateBadge();
            }
        });
        rvSanPham.setLayoutManager(new LinearLayoutManager(this));
        rvSanPham.setAdapter(adapter);
    }

    private void filter(String text) {
        List<SanPham> filteredList = new ArrayList<>();
        for (SanPham item : listFull) {
            if (item.getTenSP().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.updateList(filteredList);
    }

    private void showDialogSanPham(SanPham sp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_san_pham, null);
        builder.setView(view);

        TextInputEditText edtMa = view.findViewById(R.id.edtMaSP);
        TextInputEditText edtTen = view.findViewById(R.id.edtTenSP);
        TextInputEditText edtGia = view.findViewById(R.id.edtGiaBan);
        TextInputEditText edtSoLuong = view.findViewById(R.id.edtSoLuong);
        TextInputEditText edtDonVi = view.findViewById(R.id.edtDonViTinh);
        Spinner spnDM = view.findViewById(R.id.spnDanhMuc);

        List<DanhMuc> listDM = db.getAllDanhMuc();
        List<String> listTenDM = new ArrayList<>();
        for (DanhMuc dm : listDM) listTenDM.add(dm.getTenDM());
        ArrayAdapter<String> dmAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTenDM);
        dmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDM.setAdapter(dmAdapter);

        if (sp != null) {
            builder.setTitle("Sửa sản phẩm");
            if (edtMa != null) {
                edtMa.setText(sp.getMaSP());
                edtMa.setEnabled(false);
            }
            if (edtTen != null) edtTen.setText(sp.getTenSP());
            if (edtGia != null) edtGia.setText(String.valueOf(sp.getGiaBan()));
            if (edtSoLuong != null) edtSoLuong.setText(String.valueOf(sp.getSoLuong()));
            if (edtDonVi != null) edtDonVi.setText(sp.getDonViTinh());
            
            for (int i = 0; i < listDM.size(); i++) {
                if (listDM.get(i).getMaDM().equals(sp.getMaDM())) {
                    spnDM.setSelection(i);
                    break;
                }
            }
        } else {
            builder.setTitle("Thêm sản phẩm mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            try {
                String ma = edtMa != null && edtMa.getText() != null ? edtMa.getText().toString().trim() : "";
                String ten = edtTen != null && edtTen.getText() != null ? edtTen.getText().toString().trim() : "";
                String giaStr = edtGia != null && edtGia.getText() != null ? edtGia.getText().toString().trim() : "";
                String slStr = edtSoLuong != null && edtSoLuong.getText() != null ? edtSoLuong.getText().toString().trim() : "";
                String dv = edtDonVi != null && edtDonVi.getText() != null ? edtDonVi.getText().toString().trim() : "";

                if (ma.isEmpty() || ten.isEmpty() || giaStr.isEmpty() || slStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                double gia = Double.parseDouble(giaStr);
                int sl = Integer.parseInt(slStr);
                String maDM = listDM.isEmpty() ? "" : listDM.get(spnDM.getSelectedItemPosition()).getMaDM();

                if (sp == null) {
                    db.themSanPham(new SanPham(ma, ten, gia, sl, dv, "2024-01-01", maDM));
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    db.suaSanPham(new SanPham(ma, ten, gia, sl, dv, sp.getNgayNhap(), maDM));
                    Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                }
                loadData();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }
}
