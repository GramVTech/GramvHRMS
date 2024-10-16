package clinicleshrms.purplefish;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Daily_attendance_Edit extends AppCompatActivity {

    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Attendance/Attendance_Edit.php";
    String json_string="";
    ProgressDialog progressDialog;
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Attendance/Attendance_Edit_Insert.php";
    String json_string3="";
    Spinner month_spinner;
    EditText emobile,edate;
    String smobile ="",sdate="";
    TextView tname,tmobile;
    Spinner leave_type_spinner;
    String sid="",sleave_type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.daily_att_edit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edate.setText(showDatePicker());
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smobile = emobile.getText().toString();
                sdate = edate.getText().toString();
                progressDialog.show();
                new backgroundworker().execute();
            }
        });

        findViewById(R.id.imageView11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sleave_type = leave_type_spinner.getSelectedItem().toString();
                progressDialog.show();
                new backgroundworker3().execute();
            }
        });
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
                String post_data = URLEncoder.encode("leave_type","UTF-8")+"="+URLEncoder.encode(sleave_type,"UTF-8")+"&"
                        +URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(sid,"UTF-8");
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
            if(result.equals("YES")){
                Toast.makeText(Daily_attendance_Edit.this, "Edited Successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Daily_attendance_Edit.this, "Contact Admin", Toast.LENGTH_SHORT).show();
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
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(smobile,"UTF-8")+"&"
                        +URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(sdate,"UTF-8");
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
                int count = 0;
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                sid = jsonObject1.getString("id");

                tname.setText(jsonObject1.getString("name"));
                tname.setText(jsonObject1.getString("mobile"));

                ArrayList<String> leave_type_list = new ArrayList<>();
                leave_type_list.add("PRESENT");
                leave_type_list.add("ABSENT");
                leave_type_list.add("WORK FROM HOME");
                leave_type_list.add("OFF");
                leave_type_list.add("PAID LEAVE");
                leave_type_list.add("HALF PAID LEAVE");

                leave_type_list.add(0,jsonObject1.getString("working_type"));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Daily_attendance_Edit.this, android.R.layout.simple_spinner_item, leave_type_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                leave_type_spinner.setAdapter(adapter);



            }catch (Exception e){

            }
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
        return selectedDate[0].toString();
    }

    public void intialise(){

        emobile = findViewById(R.id.editTextText);
        edate = findViewById(R.id.editTextTextdob);
        tname = findViewById(R.id.textView17);
        tmobile = findViewById(R.id.textView18);
        leave_type_spinner = findViewById(R.id.textView31);


        progressDialog = new ProgressDialog(Daily_attendance_Edit.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        sessionMaintance = new SessionMaintance(Daily_attendance_Edit.this);

    }
}