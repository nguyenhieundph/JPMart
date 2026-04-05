package fpoly.ph34662.jpmart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpoly.ph34662.jpmart.R;

public class WelcomeAtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_ativity);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeAtivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 1500);
    }
}