package fpoly.ph34662.jpmart.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpoly.ph34662.jpmart.R;
import fpoly.ph34662.jpmart.adapter.HoaDonAdapter;
import fpoly.ph34662.jpmart.database.DatabaseHelper;
import fpoly.ph34662.jpmart.model.HoaDon;

public class HoaDonActivity extends AppCompatActivity {
    private RecyclerView rvHoaDon;
    private HoaDonAdapter adapter;
    private DatabaseHelper db;
    private List<HoaDon> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);

        db = new DatabaseHelper(this);
        rvHoaDon = findViewById(R.id.rvHoaDon);
        findViewById(R.id.btnBackHD).setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        list = db.getAllHoaDon();
        adapter = new HoaDonAdapter(this, list);
        rvHoaDon.setLayoutManager(new LinearLayoutManager(this));
        rvHoaDon.setAdapter(adapter);
    }
}
