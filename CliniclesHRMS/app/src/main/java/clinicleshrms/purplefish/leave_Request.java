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

public class leave_Request extends AppCompatActivity {

    private static final int IMAGE_CAPTURE_CODE = 101;
    private ArrayList<String> base64Images = new ArrayList<>();
    TextView tfilename;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Leave/Leave_Type_For_Leave_Request.php";
    String json_string="";
    List<String> leaveList = new ArrayList<>();
    Spinner leave_Spinner;
    EditText e_from_date,e_multiLineEditText,e_to_date,e_reporting_person;
    String sfrom_date="",sto_date="",sremarks="",sreporting_person_id="",sencoded_result="",sleave_type="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Leave/Leave_Request.php";
    String json_string2="";
    TextView tno_of_days;
    String sdaysBetween = "";
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Reporting_Person.php";
    String json_string3="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.leave_request), (v, insets) -> {
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

        findViewById(R.id.imageView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(leave_Request.this, leave_history.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCapturedPhotos();
                sfrom_date = e_from_date.getText().toString();
                sto_date = e_to_date.getText().toString();
                sremarks = e_multiLineEditText.getText().toString();
                sleave_type = leave_Spinner.getSelectedItem().toString();

                progressDialog.show();
                new backgroundworker2().execute();
            }
        });

        e_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_from_date.setText(showDatePicker());
            }
        });

        e_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                e_to_date.setText(showDatePicker());
                calc();
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
                        +URLEncoder.encode("from_date","UTF-8")+"="+ URLEncoder.encode(sfrom_date,"UTF-8")+"&"
                        +URLEncoder.encode("to_date","UTF-8")+"="+ URLEncoder.encode(sto_date,"UTF-8")+"&"
                        +URLEncoder.encode("no_of_days","UTF-8")+"="+ URLEncoder.encode(sdaysBetween,"UTF-8")+"&"
                        +URLEncoder.encode("leave_type","UTF-8")+"="+ URLEncoder.encode(sleave_type,"UTF-8")+"&"
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
                    Toast.makeText(leave_Request.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(leave_Request.this, "Leave Request Submitted Successfully", Toast.LENGTH_SHORT).show();
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

    public void calc(){
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        LocalDate date1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date1 = LocalDate.parse(e_from_date.getText().toString(), formatter);
            LocalDate date2 = LocalDate.parse(e_to_date.getText().toString(), formatter);
            long daysBetween = ChronoUnit.DAYS.between(date1, date2);
            sdaysBetween = String.valueOf(daysBetween);
            tno_of_days.setText("No Of Days : "+sdaysBetween);
        }

    }

    public String showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final String[] selectedDate = {""};
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate[0] = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
        return selectedDate[0];
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
                JSONArray allowanceArray = jsonObject.getJSONArray("leave_type");
                for (int i = 0; i < allowanceArray.length(); i++) {
                    leaveList.add(allowanceArray.getString(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(leave_Request.this, android.R.layout.simple_spinner_item, leaveList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                leave_Spinner.setAdapter(adapter);
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
        progressDialog = new ProgressDialog(leave_Request.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(leave_Request.this);
        e_from_date =findViewById(R.id.editTextTextdob);
        e_to_date =findViewById(R.id.editTextTextdob2);
        e_multiLineEditText =findViewById(R.id.multiLineEditText);
        leave_Spinner = findViewById(R.id.spinner);
        e_reporting_person = findViewById(R.id.editTextText);
        tno_of_days = findViewById(R.id.textView14);

    }
}