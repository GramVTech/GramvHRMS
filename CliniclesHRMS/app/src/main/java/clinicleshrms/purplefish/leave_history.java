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

public class leave_history extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Allowance/Allowance_Request_list.php";
    String json_string="";
    List<String> LallowanceId = new ArrayList<>();
    List<String> LallowanceName = new ArrayList<>();
    List<String> LallowanceApplierId = new ArrayList<>();
    List<String> LallowanceMobile = new ArrayList<>();
    List<String> LallowanceType = new ArrayList<>();
    List<String> Lfrom_date = new ArrayList<>();
    List<String> Lto_date = new ArrayList<>();
    List<String> Lno_of_days = new ArrayList<>();
    List<String> LallowanceRemarks = new ArrayList<>();
    List<String> LallowancePhotos = new ArrayList<>();
    CustomAdapter customAdapter;
    List<String> Lallowance_status = new ArrayList<>();
    private String[] urlArray;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.leave_history), (v, insets) -> {
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
                LallowanceType.clear();
                Lfrom_date.clear();
                Lto_date.clear();
                Lno_of_days.clear();
                LallowanceRemarks.clear();
                LallowancePhotos.clear();
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
                    LallowanceType.add(jsonObject1.getString("leave_type"));
                    Lfrom_date.add(jsonObject1.getString("from_date"));
                    LallowanceRemarks.add(jsonObject1.getString("reason"));
                    LallowancePhotos.add(jsonObject1.getString("image_path"));
                    Lto_date.add(jsonObject1.getString("to_date"));
                    Lno_of_days.add(jsonObject1.getString("no_of_days"));
                    Lallowance_status.add(jsonObject1.getString("status"));
                    count++;
                }
                if(LallowanceName.size()>0){
                    listView.setAdapter(customAdapter);
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_leave_history, null);

            ImageView allowance_image = view.findViewById(R.id.imageView6);
            ImageView allowance_reject_or_app = view.findViewById(R.id.imageView5);
            TextView allowancer_name = view.findViewById(R.id.textView17);
            TextView allowancer_mobile = view.findViewById(R.id.textView18);
            TextView allowancer_type = view.findViewById(R.id.textView19);
            TextView tfrom_date = view.findViewById(R.id.textView21);
            TextView tto_date = view.findViewById(R.id.textView22);
            TextView tdays = view.findViewById(R.id.textView23);
            TextView allowancer_remarks = view.findViewById(R.id.textView20);

            allowancer_name.setText(LallowanceName.get(i));
            allowancer_mobile.setText(LallowanceMobile.get(i));
            allowancer_type.setText("Type : "+LallowanceType.get(i));
            tfrom_date.setText(Lfrom_date.get(i));
            tto_date.setText(Lto_date.get(i));
            tdays.setText(Lno_of_days.get(i)+" Days");
            allowancer_remarks.setText(LallowanceRemarks.get(i));

            if(Lallowance_status.get(i).equals("1")){
                allowance_reject_or_app.setBackgroundResource(R.mipmap.accept);
            }else{
                allowance_reject_or_app.setBackgroundResource(R.mipmap.reject);
            }

            allowance_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    urlArray = new String[0];
                    urlArray = LallowancePhotos.get(i).split(",");
                    showImagesOneByOne();
                }
            });

            return view;
        }


    }

    private void showImagesOneByOne() {
        if (currentIndex < urlArray.length) {
            ImageView imageView = new ImageView(leave_history.this);
            imageView.setAdjustViewBounds(true);
            Picasso.get().load(urlArray[currentIndex]).into(imageView);
            AlertDialog.Builder builder = new AlertDialog.Builder(leave_history.this);
            builder.setView(imageView);
            builder.setPositiveButton("Next", (dialog, which) -> {
                currentIndex++;
                dialog.dismiss();
                if (currentIndex < urlArray.length) {
                    showImagesOneByOne();
                }
            });

            builder.setNegativeButton("Pre", (dialog, which) -> {
                currentIndex--;
                dialog.dismiss();
                if (currentIndex >= 0 && currentIndex < urlArray.length) {
                    showImagesOneByOne();
                }
            });

            builder.setNeutralButton("Ok", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void intialise(){
        progressDialog = new ProgressDialog(leave_history.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(leave_history.this);
        listView = findViewById(R.id.listView);
        customAdapter = new CustomAdapter();

    }
}