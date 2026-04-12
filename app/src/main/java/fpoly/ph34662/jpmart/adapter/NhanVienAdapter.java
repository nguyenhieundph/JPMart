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

import java.util.List;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.NhanVien;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {
    private Context context;
    private List<NhanVien> list;
    private DatabaseHelper db;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(NhanVien nv);
    }

    public NhanVienAdapter(Context context, List<NhanVien> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nhan_vien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien nv = list.get(position);
        holder.txtTenNV.setText(nv.getHoTen());
        holder.txtMaNV.setText("Mã NV: " + nv.getMaNV());
        holder.txtChucVu.setText(nv.getChucVu() == 1 ? "Quản lý" : "Nhân viên");

        holder.imgEdit.setOnClickListener(v -> listener.onEditClick(nv));

        holder.imgDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa nhân viên này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.xoaNhanVien(nv.getMaNV())) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa nhân viên!", Toast.LENGTH_SHORT).show();
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
        TextView txtTenNV, txtMaNV, txtChucVu;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenNV = itemView.findViewById(R.id.txtTenNV);
            txtMaNV = itemView.findViewById(R.id.txtMaNV);
            txtChucVu = itemView.findViewById(R.id.txtChucVu);
            imgEdit = itemView.findViewById(R.id.imgEditNV);
            imgDelete = itemView.findViewById(R.id.imgDeleteNV);
        }
    }
}