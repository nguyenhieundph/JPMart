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
import fpoly.ph34662.jpmart.model.SanPham;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private Context context;
    private List<SanPham> list;
    private DatabaseHelper db;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(SanPham sp);
    }

    public SanPhamAdapter(Context context, List<SanPham> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sp = list.get(position);
        holder.txtTenSP.setText(sp.getTenSP());
        holder.txtMaSP.setText("Mã: " + sp.getMaSP());
        holder.txtSoLuongSP.setText("Số lượng: " + sp.getSoLuong() + " " + sp.getDonViTinh());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtGiaSP.setText("Giá: " + format.format(sp.getGiaBan()));

        holder.imgEdit.setOnClickListener(v -> listener.onEditClick(sp));

        holder.imgDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.xoaSanPham(sp.getMaSP())) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
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
        TextView txtTenSP, txtMaSP, txtGiaSP, txtSoLuongSP;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtMaSP = itemView.findViewById(R.id.txtMaSP);
            txtGiaSP = itemView.findViewById(R.id.txtGiaSP);
            txtSoLuongSP = itemView.findViewById(R.id.txtSoLuongSP);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}