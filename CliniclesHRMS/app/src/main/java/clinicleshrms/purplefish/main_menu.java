package clinicleshrms.purplefish;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

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
import java.util.List;
import java.util.Locale;

public class main_menu extends AppCompatActivity {

    EasyLocationProvider easyLocationProvider;
    String Saddress="",slat_lon="";
    ProgressDialog progressDialog;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    int permission_All = 1;
    TextView Txt_leave_notf,Txt_per_notf,Txt_all_notf,Txt_wfh_notf;
    SessionMaintance sessionMaintance;
    StringBuffer sb = new StringBuffer();
    String json_url = Url_interface.url+"notification_leave_permission_wfh_count.php";
    String json_string="";
    Button check_in_out_button;
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = Url_interface.url+"Check_WFH_Apply_Or_Not_And_In_or_Out_status.php";
    String json_string2="";
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = Url_interface.url+"Check_in.php";
    String json_string3="";
    String sremarks="",spost_data="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainmenu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!haspermission(this,permissions)){
            ActivityCompat.requestPermissions(this,permissions,permission_All);

        }

        findViewById(R.id.imageView18).setVisibility(View.GONE);

        findViewById(R.id.horoscope).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, Daily_attendance.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, Profile.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.e_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, leave_Request.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.video_section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, leave_approval.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.consultation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, Permission_Request.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, permission_approval.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.chat_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, Allawance_Claim.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.allowance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, Allowance_approval.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profilev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, WFH_Request.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, wfh_approval.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageView13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_menu.this, Logout.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageView18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_in_out_button.getText().equals("CHECK IN")) {
                    try {
                        spost_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                                +URLEncoder.encode("loc","UTF-8")+"="+ URLEncoder.encode(Saddress,"UTF-8")+"&"
                                +URLEncoder.encode("lat_lon","UTF-8")+"="+ URLEncoder.encode(slat_lon,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    progressDialog.show();
                    getLoc();
                }else if(check_in_out_button.getText().equals("CHECK OUT")) {
                    showInputDialog();
                }
            }
        });

        findViewById(R.id.textView8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        intialise();
        getpermission();
        progressDialog.show();
        new backgroundworker().execute();


    }

    private void showInputDialog() {
        final EditText input = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your Remarks");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sremarks = input.getText().toString();
                json_url3 = Url_interface.url+"Check_out.php";
                try {
                    spost_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8")+"&"
                            +URLEncoder.encode("loc","UTF-8")+"="+ URLEncoder.encode(Saddress,"UTF-8")+"&"
                            +URLEncoder.encode("lat_lon","UTF-8")+"="+ URLEncoder.encode(slat_lon,"UTF-8")+"&"
                            +URLEncoder.encode("remarks","UTF-8")+"="+ URLEncoder.encode(sremarks,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                progressDialog.show();
                getLoc();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void intialise(){

        progressDialog = new ProgressDialog(main_menu.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.setCanceledOnTouchOutside(false);

        Txt_leave_notf = findViewById(R.id.textView34);
        Txt_per_notf = findViewById(R.id.textView32224);
        Txt_all_notf = findViewById(R.id.textView3224);
        Txt_wfh_notf = findViewById(R.id.textView324);

        sessionMaintance = new SessionMaintance(main_menu.this);

        check_in_out_button = findViewById(R.id.button2);
    }

    public void getpermission(){
        if (ActivityCompat.checkSelfPermission(main_menu.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(main_menu.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }else {
        }
    }

    public static boolean haspermission(Context context, String... permissions) {
        if((Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)&&(context!=null)&&(permissions!=null))
        {
            for(String temp : permissions)
                if(ActivityCompat.checkSelfPermission(context,temp)!= PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
        }
        return true;
    }

    public void getLoc(){
        easyLocationProvider = new EasyLocationProvider.Builder(main_menu.this)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setListener(new EasyLocationProvider.EasyLocationCallback() {
                    @Override
                    public void onGoogleAPIClient(GoogleApiClient googleApiClient, String message) {
                        Log.e("EasyLocationProvider","onGoogleAPIClient: "+message);
                    }
                    @Override
                    public void onLocationUpdated(double latitude, double longitude) {
                        Log.e("EasyLocationProvider","onLocationUpdated:: "+ "Latitude: "+latitude+" Longitude: "+longitude);
                        Saddress = getCompleteAddressString(latitude,longitude);
                        slat_lon = String.valueOf(latitude)+","+String.valueOf(longitude);
                        new backgroundworker3().execute();
                        easyLocationProvider.removeUpdates();
                        getLifecycle().removeObserver(easyLocationProvider);

                    }

                    @Override
                    public void onLocationUpdateRemoved() {
                        Log.e("EasyLocationProvider","onLocationUpdateRemoved");
                    }
                }).build();

        getLifecycle().addObserver(easyLocationProvider);
    }

    @Override
    protected void onDestroy() {
        easyLocationProvider.removeUpdates();
        getLifecycle().removeObserver(easyLocationProvider);
        super.onDestroy();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
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
                Txt_leave_notf.setText(jsonObject1.getString("leave_count"));
                Txt_per_notf.setText(jsonObject1.getString("permission_count"));
                Txt_all_notf.setText(jsonObject1.getString("allowance_count"));
                Txt_wfh_notf.setText(jsonObject1.getString("wfh_count"));
                progressDialog.show();
                new backgroundworker2().execute();
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
                String post_data = URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(sessionMaintance.get_user_mail(),"UTF-8");
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
                if(json_string2.split("-")[0].equals("NO")){
                    findViewById(R.id.imageView18).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.imageView18).setVisibility(View.VISIBLE);
                }

                if(json_string2.split("-")[1].equals("NO")){
                    findViewById(R.id.button2).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.button2).setVisibility(View.VISIBLE);
                    check_in_out_button.setText(json_string2.split("-")[2]);
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
                String post_data = spost_data;
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
                if(json_string3.equals("NO")){
                    Toast.makeText(main_menu.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(main_menu.this, "Check in Happended", Toast.LENGTH_SHORT).show();
                    check_in_out_button.setText("CHECK OUT");
                }
            }catch (Exception e){

            }
        }
    }
}