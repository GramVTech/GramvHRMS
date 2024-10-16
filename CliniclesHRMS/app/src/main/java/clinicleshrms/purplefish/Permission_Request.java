package clinicleshrms.purplefish;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Permission_Request extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 101;
    private ArrayList<String> base64Images = new ArrayList<>();
    TextView tfilename;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Permission/No_of_Avail_Permissions.php";
    String json_string="";
    String sno_of_permission="";
    EditText e_from_date,e_multiLineEditText,e_no_of_permission,e_reporting_person;
    String sfrom_date="",sremarks="",sreporting_person_id="",sencoded_result="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Permission/Permission_Request.php";
    String json_string2="";
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Reporting_Person.php";
    String json_string3="";
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.permission_request), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Permission_Request.this, permission_history.class);
                startActivity(intent);
            }
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
                sfrom_date = e_from_date.getText().toString();
                sremarks = e_multiLineEditText.getText().toString();
                if(Integer.parseInt(sno_of_permission) > 0) {
                    progressDialog.show();
                    new backgroundworker2().execute();
                }else{
                    Toast.makeText(Permission_Request.this, "Permission for this Month Exceeded. !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        e_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(new DatePickerCallback() {
                    @Override
                    public void onDateSelected(String selectedDate) {
                        // Set the selected date in the EditText
                        e_from_date.setText(selectedDate);
                    }
                });
            }
        });

        progressDialog.show();
        new backgroundworker().execute();
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
                        +URLEncoder.encode("permission_date","UTF-8")+"="+ URLEncoder.encode(sfrom_date,"UTF-8")+"&"
                        +URLEncoder.encode("reason","UTF-8")+"="+ URLEncoder.encode(sremarks,"UTF-8")+"&"
                        +URLEncoder.encode("reporting_person_id","UTF-8")+"="+ URLEncoder.encode(sreporting_person_id,"UTF-8");
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
                    Toast.makeText(Permission_Request.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Permission_Request.this, "Permission Request Submitted Successfully", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    }

    public class backgroundworker3 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url3);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb3=new StringBuffer();
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
                while((json_string3=bufferedReader.readLine())!=null)
                {
                    sb3.append(json_string3+"\n");
                    Log.d("json_string2",""+json_string3);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb3.toString());
                return sb3.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string3 = result;
            progressDialog.dismiss();
            try{
                JSONObject jsonObject = new JSONObject(json_string3);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                sreporting_person_id = jsonObject1.getString("reporting_person_id");
                e_reporting_person.setText(jsonObject1.getString("reporting_person_name"));
            }catch (Exception e){

            }
        }
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
                sno_of_permission = json_string;
                e_no_of_permission.setText(sno_of_permission);
                progressDialog.show();
                new backgroundworker3().execute();
            }catch (Exception e){

            }
        }
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
            stemp_file_name = stemp_file_name +"\n"+ generateTemporaryImageName()+"\n";
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
        sencoded_result = finalBase64String.toString();
    }

    public void intialise(){
        tfilename = findViewById(R.id.textView6);
        progressDialog = new ProgressDialog(Permission_Request.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(Permission_Request.this);
        e_from_date =findViewById(R.id.editTextTextdob);
        e_multiLineEditText =findViewById(R.id.multiLineEditText);
        e_reporting_person = findViewById(R.id.editTextText);
        e_no_of_permission = findViewById(R.id.editTextTextdob2);
        calendar = Calendar.getInstance();
    }

    private void showDatePickerDialog(DatePickerCallback callback) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Permission_Request.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        String selectedDate = dateFormat.format(calendar.getTime());
                        callback.onDateSelected(selectedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    interface DatePickerCallback {
        void onDateSelected(String selectedDate);
    }
}