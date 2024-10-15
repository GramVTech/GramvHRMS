package clinicleshrms.purplefish;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class My_Salary_Slip extends AppCompatActivity {

    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"Salary/my_salary.php";
    String json_string="";
    ProgressDialog progressDialog;
    TextView tname,tmobile,tnet,tacwd,tcwd,tcs,texp,tadv,thold,tinc,tts,tsr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_salary_slip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_salary_slip), (v, insets) -> {
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
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                tname.setText(jsonObject1.getString("name"));
                tmobile.setText(sessionMaintance.get_user_mail());
                tnet.setText(jsonObject1.getString("basic_pay"));
                tacwd.setText(jsonObject1.getString("present"));
                tcwd.setText(jsonObject1.getString("absent"));
                tcs.setText(jsonObject1.getString("cal"));
                texp.setText(jsonObject1.getString("allowance"));
                tadv.setText(jsonObject1.getString("advanceValue"));
                thold.setText(jsonObject1.getString("holdValue"));
                tinc.setText(jsonObject1.getString("incentive"));
                tts.setText(jsonObject1.getString("total_salary"));
                tsr.setText(jsonObject1.getString("remarks"));

            }catch (Exception e){

            }
        }
    }

    public void intialise(){

        progressDialog = new ProgressDialog(My_Salary_Slip.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        tname = findViewById(R.id.textView28);
        tmobile = findViewById(R.id.textView30);
        tnet = findViewById(R.id.textView3x0);
        tacwd = findViewById(R.id.textView3y0);
        tcwd = findViewById(R.id.textView3z0);
        tcs = findViewById(R.id.textView3a0);
        texp = findViewById(R.id.textView3b0);
        tadv = findViewById(R.id.textView3c0);
        thold = findViewById(R.id.textView3d0);
        tinc = findViewById(R.id.textView3e0);
        tts = findViewById(R.id.textView3f0);
        tsr = findViewById(R.id.textView3g0);

        sessionMaintance = new SessionMaintance(My_Salary_Slip.this);

    }
}