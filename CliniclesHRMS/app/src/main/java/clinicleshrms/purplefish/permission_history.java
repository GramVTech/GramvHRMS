package clinicleshrms.purplefish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

public class permission_history extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDialog;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Permission/Permission_History.php";
    String json_string="";
    List<String> LallowanceId = new ArrayList<>();
    List<String> LallowanceName = new ArrayList<>();
    List<String> LallowanceApplierId = new ArrayList<>();
    List<String> LallowanceMobile = new ArrayList<>();
    List<String> Lpermission_date = new ArrayList<>();
    List<String> LallowanceRemarks = new ArrayList<>();
    List<String> LallowancePhotos = new ArrayList<>();
    CustomAdapter customAdapter;
    List<String> Lallowance_status = new ArrayList<>();
    private String[] urlArray;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.permission_history), (v, insets) -> {
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
                Lpermission_date.clear();
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
                while(count<jsonArray.length()){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(count);
                    LallowanceName.add(jsonObject1.getString("applier_name"));
                    LallowanceId.add(jsonObject1.getString("id"));
                    LallowanceMobile.add(jsonObject1.getString("mobile"));
                    LallowanceApplierId.add(jsonObject1.getString("applier_id"));
                    Lpermission_date.add(jsonObject1.getString("permission_date"));
                    LallowanceRemarks.add(jsonObject1.getString("reason"));
                    LallowancePhotos.add(jsonObject1.getString("photos"));
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_permission_history, null);

            ImageView allowance_image = view.findViewById(R.id.imageView9);
            ImageView allowance_reject_or_app = view.findViewById(R.id.imageView3);

            TextView allowancer_name = view.findViewById(R.id.textView17);
            TextView allowancer_mobile = view.findViewById(R.id.textView18);
            TextView tpermission_date = view.findViewById(R.id.textView21);
            TextView allowancer_remarks = view.findViewById(R.id.textView25);

            allowancer_name.setText(LallowanceName.get(i));
            allowancer_mobile.setText(LallowanceMobile.get(i));
            tpermission_date.setText(Lpermission_date.get(i));
            allowancer_remarks.setText(LallowanceRemarks.get(i));

            if(Lallowance_status.get(i).equals("0")){
                allowance_reject_or_app.setImageResource(R.mipmap.clock);
            } else if(Lallowance_status.get(i).equals("1")){
                allowance_reject_or_app.setImageResource(R.mipmap.accept);
            }else{
                allowance_reject_or_app.setImageResource(R.mipmap.reject);
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
            ImageView imageView = new ImageView(permission_history.this);
            imageView.setAdjustViewBounds(true);
            Picasso.get().load(Url_interface.url+"Permission/"+urlArray[currentIndex]).into(imageView);
            AlertDialog.Builder builder = new AlertDialog.Builder(permission_history.this);
            builder.setView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url_interface.url+"Permission/"+urlArray[currentIndex]));
                    Toast.makeText(permission_history.this, "", Toast.LENGTH_SHORT).show();
                    startActivity(browserIntent);

                }
            });
            builder.setPositiveButton("Next", (dialog, which) -> {
                currentIndex++;
                dialog.dismiss();
                if (currentIndex < urlArray.length) {
                    showImagesOneByOne();
                }else{
                    currentIndex = 0;
                }
            });

            builder.setNegativeButton("Pre", (dialog, which) -> {
                currentIndex--;
                dialog.dismiss();
                if (currentIndex >= 0 && currentIndex < urlArray.length) {
                    showImagesOneByOne();
                }else{
                    currentIndex = 0;
                }
            });

            builder.setNeutralButton("Ok", (dialog, which) -> {
                dialog.dismiss();
                currentIndex = 0;
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void intialise(){
        progressDialog = new ProgressDialog(permission_history.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);
        sessionMaintance = new SessionMaintance(permission_history.this);
        listView = findViewById(R.id.listView);
        listView.setDivider(null);
        customAdapter = new CustomAdapter();

    }
}