package fpoly.ph34662.jpmart.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.HoaDon;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> {
    private Context context;
    private List<HoaDon> list;
    private DatabaseHelper db;

    public HoaDonAdapter(Context context, List<HoaDon> list) {
        this.context = context;
        this.list = list;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoaDon hd = list.get(position);
        holder.txtMaHD.setText(hd.getMaHD());
        holder.txtNgayMua.setText(hd.getNgayMua());
        holder.txtTenKH.setText(db.getTenKhachHang(hd.getMaKH()));
        
        // Hiển thị tên nhân viên
        if (hd.getMaNV() != null && !hd.getMaNV().isEmpty()) {
            fpoly.ph34662.jpmart.model.NhanVien nv = db.layNhanVienBangMaNV(hd.getMaNV());
            holder.txtTenNV.setText(nv != null ? nv.getHoTen() : "Không xác định");
        } else {
            holder.txtTenNV.setText("Admin");
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtTongTien.setText(format.format(hd.getTongTien()));

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa hóa đơn này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.xoaHoaDon(hd.getMaHD())) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaHD, txtNgayMua, txtTenKH, txtTenNV, txtTongTien;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaHD = itemView.findViewById(R.id.txtMaHD);
            txtTenNV = itemView.findViewById(R.id.txtTenNV);
            txtTenKH = itemView.findViewById(R.id.txtTenKH_HD);
            txtNgayMua = itemView.findViewById(R.id.txtNgayMua);
            txtTongTien = itemView.findViewById(R.id.txtTongTienHD);
            btnDelete = itemView.findViewById(R.id.btnDeleteHD);
        }
    }
}
