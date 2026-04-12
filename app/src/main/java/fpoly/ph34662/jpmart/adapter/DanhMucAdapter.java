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
import fpoly.ph34662.jpmart.model.DanhMuc;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {
    private Context context;
    private List<DanhMuc> list;
    private DatabaseHelper db;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(DanhMuc dm);
    }

    public DanhMucAdapter(Context context, List<DanhMuc> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc dm = list.get(position);
        holder.txtTenDM.setText(dm.getTenDM());
        holder.txtMaDM.setText("Mã: " + dm.getMaDM());

        holder.imgEdit.setOnClickListener(v -> listener.onEditClick(dm));

        holder.imgDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa danh mục này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (db.xoaDanhMuc(dm.getMaDM())) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            Toast.makeText(context, "Đã xóa danh mục!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi: Danh mục có thể đang chứa sản phẩm!", Toast.LENGTH_SHORT).show();
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
        TextView txtTenDM, txtMaDM;
        ImageView imgEdit, imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenDM = itemView.findViewById(R.id.txtTenDM);
            txtMaDM = itemView.findViewById(R.id.txtMaDM);
            imgEdit = itemView.findViewById(R.id.imgEditDM);
            imgDelete = itemView.findViewById(R.id.imgDeleteDM);
        }
    }
}
