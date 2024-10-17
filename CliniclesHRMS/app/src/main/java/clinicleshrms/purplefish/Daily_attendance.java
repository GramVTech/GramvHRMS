package clinicleshrms.purplefish;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Daily_attendance extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Attendance/Attendance_List.php";
    String json_string="";
    List<String> Ldate = new ArrayList<>();
    List<String> Lerr_msg = new ArrayList<>();
    List<String> LId = new ArrayList<>();
    List<String> Lname = new ArrayList<>();
    List<String> Lmobile = new ArrayList<>();
    List<String> Lreporting_id = new ArrayList<>();
    List<String> Lleave_type = new ArrayList<>();
    List<String> Lcheck_status = new ArrayList<>();
    CustomAdapter customAdapter;
    TextView tdate;
    String master_string="";
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Attendance/Attendance_Insert.php";
    String json_string3="";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"get_remarks.php";
    String json_string2="";
    String suser_id = "",stdate = "",ssname="",ssmobile="";
    List<String> Lcheck_in_time = new ArrayList<>();
    List<String> Lcheck_out_time = new ArrayList<>();
    List<String> Lcheck_in_time_attendance = new ArrayList<>();
    List<String> Lcheck_out_time_attendance = new ArrayList<>();
    List<String> Lcheck_in_location = new ArrayList<>();
    List<String> Lcheck_out_location = new ArrayList<>();
    List<String> Lcheck_in_lat_lon = new ArrayList<>();
    List<String> Lcheck_out_lat_lon = new ArrayList<>();
    List<String> Lremarks = new ArrayList<>();
    List<String> Lwfh_location = new ArrayList<>();
    List<String> Lwfh_lat_lon = new ArrayList<>();
    List<String> Lcreated_on = new ArrayList<>();
    String work_status="",swork_status="",spost_data="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.daily_attendance), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.imageView14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Daily_attendance.this, Daily_attendance_Edit.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageView17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Daily_attendance.this, Daily_attendance_View.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View listItem;
                for (int i = 0; i < listView.getChildCount(); i++) {
                    listItem = listView.getAdapter().getView(i, null, null);
                    String temp = "";
                    TextView textView = listItem.findViewById(R.id.textView35);
                    Spinner leave_t_spin = listItem.findViewById(R.id.textView31);
                    temp = Ldate.get(0)+"@"+textView.getText().toString()+"@"+Lreporting_id.get(0)+"@"+leave_t_spin.getSelectedItem().toString();
                    master_string = master_string + temp + "@@@";
                }
                progressDialog.show();
                new backgroundworker3().execute();
            }
        });

        intialise();
        progressDialog.show();
        new backgroundworker().execute();
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
            int count =0;
            try{
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                while(count<jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    Ldate.add(jsonObject1.getString("date"));
                    Lerr_msg.add(jsonObject1.getString("err_msg"));
                    LId.add(jsonObject1.getString("id"));
                    Lname.add(jsonObject1.getString("name"));
                    Lmobile.add(jsonObject1.getString("mobile"));
                    Lreporting_id.add(jsonObject1.getString("reporting_id"));
                    Lleave_type.add(jsonObject1.getString("leave_type"));
                    Lcheck_status.add(jsonObject1.getString("check_status"));
                    count++;
                }
                if(LId.size()>0){
                    tdate.setText("ATTENDANCE DATE : "+Ldate.get(0));
                    listView.setAdapter(customAdapter);
                }else{
                    tdate.setText("No Attendance Pending");

                }

            }catch (Exception e){

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return LId.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_daily_attendance, null);

            ImageView task_view = view.findViewById(R.id.imageView11);
            TextView allowancer_name = view.findViewById(R.id.textView17);
            TextView allowancer_mobile = view.findViewById(R.id.textView18);
            TextView allowancer_id = view.findViewById(R.id.textView35);
            Spinner leave_type_spinner = view.findViewById(R.id.textView31);

            allowancer_id.setVisibility(View.INVISIBLE);

            allowancer_name.setText(Lname.get(i));
            allowancer_mobile.setText(Lmobile.get(i));
            allowancer_id.setText(LId.get(i));

            String[] leave_type_array = Lleave_type.get(i).split(",");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(Daily_attendance.this, android.R.layout.simple_spinner_item, leave_type_array);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            leave_type_spinner.setAdapter(adapter);

            task_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    suser_id = LId.get(i);
                    stdate = Ldate.get(i);
                    ssname = Lname.get(i);
                    ssmobile = Lmobile.get(i);
                    work_status = leave_type_spinner.getSelectedItem().toString();
                    if(work_status.equals("WORK FROM HOME")){
                        swork_status = "WORK FROM HOME";
                        try {
                            spost_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(suser_id,"UTF-8")+"&"
                                    +URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(stdate,"UTF-8")+"&"
                                    +URLEncoder.encode("status","UTF-8")+"="+ URLEncoder.encode(swork_status,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        progressDialog.show();
                        new backgroundworker2().execute();
                    }else{
                        swork_status = "DAILY ATTENDANCE";
                        try {
                            spost_data = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(suser_id,"UTF-8")+"&"
                                    +URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(stdate,"UTF-8")+"&"
                                    +URLEncoder.encode("status","UTF-8")+"="+ URLEncoder.encode(swork_status,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        progressDialog.show();
                        new backgroundworker2().execute();
                    }

                }
            });
            return view;
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
                Log.e("ASASAS",""+master_string);
                String post_data = URLEncoder.encode("total_attendance","UTF-8")+"="+URLEncoder.encode(master_string.substring(0,master_string.length()-3),"UTF-8");
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
                    Log.d("json_string3",""+json_string3);
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

            Ldate.clear();
            Lerr_msg.clear();
            LId.clear();
            Lname.clear();
            Lmobile.clear();
            Lreporting_id.clear();
            Lleave_type.clear();
            Lcheck_status.clear();
            master_string = "";

            Intent intent = new Intent(Daily_attendance.this, main_menu.class);
            startActivity(intent);
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
                String post_data = spost_data;
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
                int count =0;
                JSONObject jsonObject = new JSONObject(json_string2);
                JSONArray jsonArray = jsonObject.getJSONArray("products");

                if(swork_status.equals("WORK FROM HOME")){
                    while(count < jsonArray.length()){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                        Lcreated_on.add(jsonObject1.getString("created_on"));
                        Lwfh_lat_lon.add(jsonObject1.getString("lat_lon"));
                        Lwfh_location.add(jsonObject1.getString("location"));
                        Lremarks.add(jsonObject1.getString("remarks"));
                        count++;
                    }
                    if(Lremarks.size()>0){
                        CustomBottomSheet_WFH bottomSheet = new CustomBottomSheet_WFH(Daily_attendance.this,
                                Lremarks,
                                Lwfh_lat_lon,
                                Lwfh_location,
                                Lcreated_on,
                                ssname,
                                ssmobile,
                                stdate
                        );
                        bottomSheet.show(getSupportFragmentManager(), "CustomBottomSheet_WFH");
                    }

                }

                else{
                    while(count < jsonArray.length()){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                        Lcheck_in_time.add(jsonObject1.getString("check_in_time"));
                        Lcheck_out_time.add(jsonObject1.getString("check_out_time"));
                        Lcheck_in_time_attendance.add(jsonObject1.getString("check_in_time_attendance"));
                        Lcheck_out_time_attendance.add(jsonObject1.getString("check_out_time_attendance"));
                        Lcheck_in_location.add(jsonObject1.getString("check_in_location"));
                        Lcheck_out_location.add(jsonObject1.getString("check_out_location"));
                        Lcheck_in_lat_lon.add(jsonObject1.getString("check_in_lat_lon"));
                        Lcheck_out_lat_lon.add(jsonObject1.getString("check_out_lat_lon"));
                        Lremarks.add(jsonObject1.getString("remarks"));
                        count++;
                    }
                    if(Lremarks.size()>0){
                        CustomBottomSheet bottomSheet = new CustomBottomSheet(Daily_attendance.this,
                                Lremarks,
                                Lcheck_in_time,
                                Lcheck_out_time,
                                Lcheck_in_time_attendance,
                                Lcheck_out_time_attendance,
                                Lcheck_in_location,
                                Lcheck_out_location,
                                Lcheck_in_lat_lon,
                                Lcheck_out_lat_lon,
                                ssname,
                                ssmobile,
                                stdate
                        );
                        bottomSheet.show(getSupportFragmentManager(), "CustomBottomSheet");
                    }else{
                        Toast.makeText(Daily_attendance.this, "No Remarks Found", Toast.LENGTH_SHORT).show();
                    }

                }



            }catch (Exception e){

            }
        }
    }

    public void intialise(){
        progressDialog = new ProgressDialog(Daily_attendance.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(Daily_attendance.this);
        listView = findViewById(R.id.listView);
        listView.setDivider(null);
        customAdapter = new CustomAdapter();
        tdate = findViewById(R.id.textView24);
    }
}