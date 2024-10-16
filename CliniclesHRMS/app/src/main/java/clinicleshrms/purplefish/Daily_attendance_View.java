package clinicleshrms.purplefish;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.List;

public class Daily_attendance_View extends AppCompatActivity {

    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Attendance/Getting_list_of_Attendance_Month_and_Year.php";
    String json_string="";
    ProgressDialog progressDialog;
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Attendance/Attendance_View.php";
    String json_string3="";
    Spinner month_spinner;
    ListView listView;
    CustomAdapter customAdapter;
    List<String> month_year = new ArrayList<>();
    String smonth_year="";
    List<String> Ldateee = new ArrayList<>();
    List<String> Lwork_type = new ArrayList<>();
    EditText emobile;
    String smobile ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.daily_attendance_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intialise();
        progressDialog.show();
        new backgroundworker().execute();

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smobile = emobile.getText().toString();
                smonth_year = month_spinner.getSelectedItem().toString();
                progressDialog.show();
                new backgroundworker3().execute();
            }
        });
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
                JSONArray allowanceArray = jsonObject.getJSONArray("month_year");
                for (int i = 0; i < allowanceArray.length(); i++) {
                    month_year.add(allowanceArray.getString(i));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Daily_attendance_View.this, android.R.layout.simple_spinner_item, month_year);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                month_spinner.setAdapter(adapter);
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
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(smobile,"UTF-8")+"&"
                        +URLEncoder.encode("month_year","UTF-8")+"="+ URLEncoder.encode(smonth_year,"UTF-8");
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
                int count = 0;
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count<jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    Ldateee.add(jsonObject1.getString("date"));
                    Lwork_type.add(jsonObject1.getString("working_type"));
                    count++;
                }
                if(Ldateee.size()>0){
                    listView.setAdapter(customAdapter);
                }
            }catch (Exception e){

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Ldateee.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return getCount();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view != null){
                return view;
            }
            view = getLayoutInflater().inflate(R.layout.custom_layout_my_attendance, null);
            TextView tdateee = view.findViewById(R.id.textView18);
            TextView tworking_type = view.findViewById(R.id.textView31);
            tworking_type.setText(Lwork_type.get(i));
            tdateee.setText(Ldateee.get(i));
            return view;
        }
    }

    public void intialise(){

        emobile = findViewById(R.id.editTextText);

        progressDialog = new ProgressDialog(Daily_attendance_View.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        month_spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listView);
        listView.setDivider(null);
        sessionMaintance = new SessionMaintance(Daily_attendance_View.this);

    }
}