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
import fpoly.ph34662.jpmart.model.KhachHang;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private Context context;
    private List<KhachHang> list;
    private DatabaseHelper db;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(KhachHang kh);
    }

    public KhachHangAdapter(Context context, List<KhachHang> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_khach_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang kh = list.get(position);
        holder.txtTenKH.setText(kh.getHoTen());
        holder.txtMaKH.setText("Mã: " + kh.getMaKH());
        holder.txtSDT.setText("SĐT: " + kh.getSoDienThoai());

        holder.imgEdit.setOnClickListener(v -> listener.onEditClick(kh));

        holder.imgDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa khách hàng này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.xoaKhachHang(kh.getMaKH())) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            Toast.makeText(context, "Đã xóa khách hàng!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Không thể xóa khách hàng này!", Toast.LENGTH_SHORT).show();
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
        TextView txtTenKH, txtMaKH, txtSDT;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenKH = itemView.findViewById(R.id.txtTenKH);
            txtMaKH = itemView.findViewById(R.id.txtMaKH);
            txtSDT = itemView.findViewById(R.id.txtSDT);
            imgEdit = itemView.findViewById(R.id.imgEditKH);
            imgDelete = itemView.findViewById(R.id.imgDeleteKH);
        }
    }
}