package clinicleshrms.purplefish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Allawance_Claim extends AppCompatActivity {

    private Uri imageUri;
    private ArrayList<String> base64Images = new ArrayList<>();
    TextView tfilename;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Allowance/Allowance_list.php";
    String json_string="";
    List<String> allowanceList = new ArrayList<>();
    Spinner allowance_Spinner;
    EditText eallowance_Amount,emultiLineEditText;
    String sallowance_amount="",sallowance_type="",sremarks="",sencoded_result="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Allowance/Allowance_Request.php";
    String json_string2="";
    private ActivityResultLauncher<Intent> cameraLauncher;

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
                sallowance_amount = eallowance_Amount.getText().toString();
                sallowance_type = allowance_Spinner.getSelectedItem().toString();
                sremarks = emultiLineEditText.getText().toString();
                progressDialog.show();
                new backgroundworker2().execute();
            }
        });



        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Allawance_Claim.this, Allowance_history.class);
                startActivity(intent);
            }
        });

        progressDialog.show();
        new backgroundworker().execute();

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                handleCapturedImage(imageUri);
            }
        });
    }

    private void openCamera() {
        File photoFile = new File(getExternalFilesDir(null), generateFileName());
        imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // Specify the output URI
        cameraLauncher.launch(cameraIntent);
    }

    private String generateFileName() {
        return "IMG_" + System.currentTimeMillis() + ".jpg";
    }

    private void handleCapturedImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap fullsize = BitmapFactory.decodeStream(inputStream);

            if (fullsize != null) {
                Bitmap resizedBitmap = resizeBitmap(fullsize, 800, 800);
                String base64String = convertToBase64(resizedBitmap, 50);
                base64Images.add(base64String);
                String stemp_file_name = tfilename.getText().toString();
                stemp_file_name += "\n" + generateTemporaryImageName() + "\n";
                tfilename.setText(stemp_file_name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);
        int newWidth = Math.round(scale * width);
        int newHeight = Math.round(scale * height);
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private String convertToBase64(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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
        sencoded_result = finalBase64String.toString();
    }

    public void intialise(){
        tfilename = findViewById(R.id.textView6);
        progressDialog = new ProgressDialog(Allawance_Claim.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(Allawance_Claim.this);
        allowance_Spinner = findViewById(R.id.spinner2);
        eallowance_Amount = findViewById(R.id.editTextTextdob);
        emultiLineEditText = findViewById(R.id.multiLineEditText);

    }

    public class backgroundworker extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string=bufferedReader.readLine())!=null)
                {
                    sb.append(json_string+"\n");
                    Log.d("json_string",""+json_string);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb.toString());
                return sb.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            try{
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray allowanceArray = jsonObject.getJSONArray("allowance_type");
                for (int i = 0; i < allowanceArray.length(); i++) {
                    allowanceList.add(allowanceArray.getString(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Allawance_Claim.this, android.R.layout.simple_spinner_item, allowanceList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                allowance_Spinner.setAdapter(adapter);
            }catch (Exception e){

            }
        }
    }

    public class backgroundworker2 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                        +URLEncoder.encode("base64_encoded_data","UTF-8")+"="+ URLEncoder.encode(sencoded_result,"UTF-8")+"&"
                        +URLEncoder.encode("allowance_type","UTF-8")+"="+ URLEncoder.encode(sallowance_type,"UTF-8")+"&"
                        +URLEncoder.encode("allowance_amount","UTF-8")+"="+ URLEncoder.encode(sallowance_amount,"UTF-8")+"&"
                        +URLEncoder.encode("remarks","UTF-8")+"="+ URLEncoder.encode(sremarks,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string2=bufferedReader.readLine())!=null)
                {
                    sb2.append(json_string2+"\n");
                    Log.d("json_string2",""+json_string2);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb2.toString());
                return sb2.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            progressDialog.dismiss();
            try{
                if(json_string2.equals("NO")){
                    Toast.makeText(Allawance_Claim.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Allawance_Claim.this, "Allowance Claimed", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    }
}