package fpoly.ph34662.jpmart.model;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static String maNhanVien = "";
    // Giỏ hàng tạm thời
    public static List<SanPham> cart = new ArrayList<>();
    
    public static void clearCart() {
        cart.clear();
    }
}
