package fpoly.ph34662.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.model.SanPham;

public class TopSanPhamAdapter extends RecyclerView.Adapter<TopSanPhamAdapter.ViewHolder> {
    private List<SanPham> list;

    public TopSanPhamAdapter(List<SanPham> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_san_pham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPham sp = list.get(position);
        holder.txtRank.setText(String.valueOf(position + 1));
        holder.txtTenSP.setText(sp.getTenSP());
        holder.txtMaSP.setText("Mã SP: " + sp.getMaSP());
        holder.txtSoLuongBan.setText("Đã bán: " + sp.getSoLuong());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRank, txtTenSP, txtMaSP, txtSoLuongBan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRank = itemView.findViewById(R.id.txtRank);
            txtTenSP = itemView.findViewById(R.id.txtTenSP);
            txtMaSP = itemView.findViewById(R.id.txtMaSP);
            txtSoLuongBan = itemView.findViewById(R.id.txtSoLuongBan);
        }
    }
}