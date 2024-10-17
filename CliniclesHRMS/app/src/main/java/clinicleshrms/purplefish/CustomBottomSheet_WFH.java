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

public class CustomBottomSheet_WFH extends BottomSheetDialogFragment {

    List<String> Lre = new ArrayList<>();
    List<String> Lwfh_lat_lo = new ArrayList<>();
    List<String> Lwfh_lo = new ArrayList<>();
    List<String> Lcreated_o = new ArrayList<>();
    String name="",mobile="",dater="";


    public CustomBottomSheet_WFH(Context context,
                                 List<String> Lremarks,
                                 List<String> Lwfh_lat_lon,
                                 List<String> Lwfh_location,
                                 List<String> Lcreated_on,
                                 String sname,
                                 String smobile,
                                 String stdate){
        Lre = Lremarks;
        Lwfh_lat_lo = Lwfh_lat_lon;
        Lwfh_lo = Lwfh_location;
        Lcreated_o = Lcreated_on;
        name = sname;
        mobile = smobile;
        dater = stdate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout_wfh_attendance_view, container, false);

        TextView tname = view.findViewById(R.id.textView17);
        TextView tmobile = view.findViewById(R.id.textView18);
        TextView tremarks_a = view.findViewById(R.id.textView1c8);
        TextView tremarks_a2 = view.findViewById(R.id.textView1d8);
        TextView tremarks_a3 = view.findViewById(R.id.textView1e8);
        TextView tremarks_a4 = view.findViewById(R.id.textView1f8);

        if(Lre.size()==1){
            tremarks_a.setText(Lwfh_lo.get(0)+"\n"+Lre.get(0));
        }else if(Lre.size()==2){
            tremarks_a.setText(Lwfh_lo.get(0)+"\n"+Lre.get(0));
            tremarks_a2.setText(Lwfh_lo.get(1)+"\n"+Lre.get(1));
        }else if(Lre.size()==3){
            tremarks_a.setText(Lwfh_lo.get(0)+"\n"+Lre.get(0));
            tremarks_a2.setText(Lwfh_lo.get(1)+"\n"+Lre.get(1));
            tremarks_a3.setText(Lwfh_lo.get(2)+"\n"+Lre.get(2));
        }else if(Lre.size()==4){
            tremarks_a.setText(Lwfh_lo.get(0)+"\n"+Lre.get(0));
            tremarks_a2.setText(Lwfh_lo.get(1)+"\n"+Lre.get(1));
            tremarks_a3.setText(Lwfh_lo.get(2)+"\n"+Lre.get(2));
            tremarks_a4.setText(Lwfh_lo.get(3)+"\n"+Lre.get(3));
        }

        tname.setText(name);
        tmobile.setText(mobile);


        view.findViewById(R.id.imageView20).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}

