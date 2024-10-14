package clinicleshrms.purplefish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Allawance_Claim extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 101;
    private ArrayList<String> base64Images = new ArrayList<>();
    TextView tfilename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allawance_claim);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.allowance_claim), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intialise();

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    openCamera();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCapturedPhotos();
            }
        });



        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Allawance_Claim.this, Allowance_history.class);
                startActivity(intent);
            }
        });
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String base64String = convertToBase64(photo);
            base64Images.add(base64String);
            String stemp_file_name = tfilename.getText().toString();
            stemp_file_name = "Your Uploading Files : \n"+stemp_file_name+"\n";
            tfilename.setText(stemp_file_name);
        }
    }

    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String generateTemporaryImageName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return "IMG_" + timeStamp + ".jpg";
        // return "IMG_" + UUID.randomUUID().toString() + ".jpg";
    }

    private void processCapturedPhotos() {
        StringBuilder finalBase64String = new StringBuilder();
        for (int i = 0; i < base64Images.size(); i++) {
            finalBase64String.append(base64Images.get(i));
            if (i != base64Images.size() - 1) {
                finalBase64String.append("@@@");
            }
        }
        String result = finalBase64String.toString();
        Toast.makeText(this, "Base64 Result: " + result, Toast.LENGTH_SHORT).show();
    }

    public void intialise(){
        tfilename = findViewById(R.id.textView6);
    }
}