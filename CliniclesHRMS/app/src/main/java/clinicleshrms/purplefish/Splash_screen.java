package clinicleshrms.purplefish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_screen extends AppCompatActivity {

    SessionMaintance sessionMaintance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intialise();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sessionMaintance.get_user_mail().equals("")) {
                    Intent intent = new Intent(Splash_screen.this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Splash_screen.this, main_menu.class);
                    startActivity(intent);
                }
            }
        }, 3000);
    }

    public void intialise(){
        sessionMaintance = new SessionMaintance(Splash_screen.this);
    }
}