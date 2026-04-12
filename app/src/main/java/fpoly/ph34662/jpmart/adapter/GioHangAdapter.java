package fpoly.ph34662.jpmart.adapter;

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
import fpoly.ph34662.jpmart.model.SanPham;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {
    private Context context;
    private List<SanPham> list;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public GioHangAdapter(Context context, List<SanPham> list, OnCartChangeListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sp = list.get(position);
        holder.txtTen.setText(sp.getTenSP());
        
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtGia.setText(format.format(sp.getGiaBan()));
        holder.txtSoLuong.setText(String.valueOf(sp.getSoLuongTrongGio()));

        // Nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            if (listener != null) {
                listener.onCartChanged();
            }
        });

        // Tăng số lượng
        holder.btnTang.setOnClickListener(v -> {
            if (sp.getSoLuongTrongGio() < sp.getSoLuong()) {
                sp.setSoLuongTrongGio(sp.getSoLuongTrongGio() + 1);
                holder.txtSoLuong.setText(String.valueOf(sp.getSoLuongTrongGio()));
                if (listener != null) listener.onCartChanged();
            } else {
                Toast.makeText(context, "Đã đạt giới hạn tồn kho!", Toast.LENGTH_SHORT).show();
            }
        });

        // Giảm số lượng
        holder.btnGiam.setOnClickListener(v -> {
            if (sp.getSoLuongTrongGio() > 1) {
                sp.setSoLuongTrongGio(sp.getSoLuongTrongGio() - 1);
                holder.txtSoLuong.setText(String.valueOf(sp.getSoLuongTrongGio()));
                if (listener != null) listener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtGia, txtSoLuong;
        ImageView btnDelete, btnTang, btnGiam, imgSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txtTenSPGioHang);
            txtGia = itemView.findViewById(R.id.txtGiaSPGioHang);
            txtSoLuong = itemView.findViewById(R.id.txtSoLuongGioHang);
            btnDelete = itemView.findViewById(R.id.btnXoaItemGioHang);
            btnTang = itemView.findViewById(R.id.btnTangSL);
            btnGiam = itemView.findViewById(R.id.btnGiamSL);
            imgSP = itemView.findViewById(R.id.imgSPGioHang);
        }
    }
}
