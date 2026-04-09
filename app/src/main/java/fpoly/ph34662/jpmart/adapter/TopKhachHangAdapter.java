package fpoly.ph34662.jpmart.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.model.KhachHang;

public class TopKhachHangAdapter extends RecyclerView.Adapter<TopKhachHangAdapter.ViewHolder> {
    private List<KhachHang> list;

    public TopKhachHangAdapter(List<KhachHang> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_khach_hang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhachHang kh = list.get(position);
        holder.txtRankKH.setText(String.valueOf(position + 1));
        holder.txtTenKH.setText(kh.getHoTen());
        holder.txtMaKH.setText("Mã KH: " + kh.getMaKH());
        
        // Chuyển đổi chuỗi tổng tiền đã lưu tạm trong trường SĐT sang định dạng VNĐ
        double totalSpent = Double.parseDouble(kh.getSoDienThoai());
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtTongChiTieu.setText(format.format(totalSpent));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRankKH, txtTenKH, txtMaKH, txtTongChiTieu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRankKH = itemView.findViewById(R.id.txtRankKH);
            txtTenKH = itemView.findViewById(R.id.txtTenKH);
            txtMaKH = itemView.findViewById(R.id.txtMaKH);
            txtTongChiTieu = itemView.findViewById(R.id.txtTongChiTieu);
        }
    }
}