package clinicleshrms.purplefish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

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

public class wfh_approval extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"WFH/WFH_Request_list.php";
    String json_string="";
    List<String> LallowanceId = new ArrayList<>();
    List<String> LallowanceName = new ArrayList<>();
    List<String> LallowanceApplierId = new ArrayList<>();
    List<String> LallowanceMobile = new ArrayList<>();
    List<String> Lfrom_date = new ArrayList<>();
    List<String> Lto_date = new ArrayList<>();
    List<String> Lno_of_days = new ArrayList<>();
    List<String> LallowanceRemarks = new ArrayList<>();
    CustomAdapter customAdapter;
    String allowance_status = "";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"WFH/WFH_Approval.php";
    String json_string2="";
    String sallowance_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wfh_approval);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wfh_approval), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
                LallowanceId.clear();
                LallowanceName.clear();
                LallowanceApplierId.clear();
                LallowanceMobile.clear();
                Lfrom_date.clear();
                Lto_date.clear();
                Lno_of_days.clear();
                LallowanceRemarks.clear();
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
                while(count<LallowanceName.size()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    LallowanceName.add(jsonObject1.getString("applier_name"));
                    LallowanceId.add(jsonObject1.getString("id"));
                    LallowanceMobile.add(jsonObject1.getString("mobile"));
                    LallowanceApplierId.add(jsonObject1.getString("applier_id"));
                    Lfrom_date.add(jsonObject1.getString("from_date"));
                    LallowanceRemarks.add(jsonObject1.getString("reason"));
                    Lto_date.add(jsonObject1.getString("to_date"));
                    Lno_of_days.add(jsonObject1.getString("no_of_days"));
                    count++;
                }
                if(LallowanceName.size()>0){
                    listView.setAdapter(customAdapter);
                }

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
                String post_data = URLEncoder.encode("wfh_status","UTF-8")+"="+URLEncoder.encode(allowance_status,"UTF-8")+"&"
                        +URLEncoder.encode("wfh_id","UTF-8")+"="+ URLEncoder.encode(sallowance_id,"UTF-8");
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
                    Toast.makeText(wfh_approval.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    new backgroundworker().execute();
                    LallowanceId.clear();
                    LallowanceName.clear();
                    LallowanceApplierId.clear();
                    LallowanceMobile.clear();
                    Lfrom_date.clear();
                    Lto_date.clear();
                    Lno_of_days.clear();
                    LallowanceRemarks.clear();
                    Toast.makeText(wfh_approval.this, "WFH Request Status Changed", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return LallowanceName.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_wfh_approval, null);

            ImageView allowance_approval = view.findViewById(R.id.imageView6);
            ImageView allowance_reject = view.findViewById(R.id.imageView4);

            TextView allowancer_name = view.findViewById(R.id.textView17);
            TextView allowancer_mobile = view.findViewById(R.id.textView18);
            TextView allowancer_remarks = view.findViewById(R.id.textView20);
            TextView tfrom_date = view.findViewById(R.id.textView21);
            TextView tto_date = view.findViewById(R.id.textView22);
            TextView tdays = view.findViewById(R.id.textView23);


            allowancer_name.setText(LallowanceName.get(i));
            allowancer_mobile.setText(LallowanceMobile.get(i));
            tfrom_date.setText(Lfrom_date.get(i));
            tto_date.setText(Lto_date.get(i));
            tdays.setText(Lno_of_days.get(i)+" Days");
            allowancer_remarks.setText(LallowanceRemarks.get(i));


            allowance_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allowance_status = "2";
                    sallowance_id = LallowanceId.get(i);
                    progressDialog.show();
                    new backgroundworker2().execute();
                }
            });
            allowance_approval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allowance_status = "1";
                    sallowance_id = LallowanceId.get(i);
                    progressDialog.show();
                    new backgroundworker2().execute();
                }
            });

            return view;
        }


    }

    public void intialise(){
        progressDialog = new ProgressDialog(wfh_approval.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(wfh_approval.this);
        listView = findViewById(R.id.listView);
        customAdapter = new CustomAdapter();

    }
}