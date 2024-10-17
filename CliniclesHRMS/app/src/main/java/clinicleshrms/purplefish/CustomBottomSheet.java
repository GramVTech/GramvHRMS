package clinicleshrms.purplefish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomBottomSheet extends BottomSheetDialogFragment {

    List<String> Lre = new ArrayList<>();
    List<String> Lche_in_t = new ArrayList<>();
    List<String> Lche_out_time = new ArrayList<>();
    List<String> Lcheck_in_time_att = new ArrayList<>();
    List<String> Lcheck_out_time_att = new ArrayList<>();
    List<String> Lcheck_in_loc = new ArrayList<>();
    List<String> Lcheck_out_loc = new ArrayList<>();
    List<String> Lcheck_in_lat_lo = new ArrayList<>();
    List<String> Lcheck_out_lat_lo = new ArrayList<>();
    String name="",mobile="",dater="";


    public CustomBottomSheet(Context context, List<String> Lremarks,
                             List<String> Lcheck_in_time,
                             List<String> Lcheck_out_time,
                             List<String> Lcheck_in_time_attendance,
                             List<String> Lcheck_out_time_attendance,
                             List<String> Lcheck_in_location,
                             List<String> Lcheck_out_location,
                             List<String> Lcheck_in_lat_lon,
                             List<String> Lcheck_out_lat_lon,
                             String sname,
                             String smobile,
                             String stdate){
        Lre = Lremarks;
        Lche_in_t = Lcheck_in_time;
        Lche_out_time = Lcheck_out_time;
        Lcheck_in_time_att = Lcheck_in_time_attendance;
        Lcheck_out_time_att = Lcheck_out_time_attendance;
        Lcheck_in_loc = Lcheck_in_location;
        Lcheck_out_loc = Lcheck_out_location;
        Lcheck_in_lat_lo = Lcheck_in_lat_lon;
        Lcheck_out_lat_lo = Lcheck_out_lat_lon;
        name = sname;
        mobile = smobile;
        dater = stdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_daily_attendance_view, container, false);

        TextView tname = view.findViewById(R.id.textView17);
        TextView tmobile = view.findViewById(R.id.textView18);
        TextView t_intime_loc = view.findViewById(R.id.textView1a8);
        TextView ttime_out_loc = view.findViewById(R.id.textView1b8);
        TextView tremarks = view.findViewById(R.id.textView1c8);

        ImageView imageView = view.findViewById(R.id.imageView15);
        ImageView imageView2 = view.findViewById(R.id.imageView16);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = Lcheck_in_lat_lo.get(0).split(",")[0];
                String longitude = Lcheck_in_lat_lo.get(0).split(",")[1];
                Uri locationUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "CHECK IN");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = Lcheck_out_lat_lo.get(0).split(",")[0];
                String longitude = Lcheck_out_lat_lo.get(0).split(",")[1];
                Uri locationUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "CHECK OUT");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        tname.setText(name);
        tmobile.setText(mobile);
        t_intime_loc.setText(dater+" : "+Lche_in_t.get(0));
        ttime_out_loc.setText(dater+" : "+Lche_out_time.get(0));
        tremarks.setText(Lre.get(0));

        view.findViewById(R.id.imageView19).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}

