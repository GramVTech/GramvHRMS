package clinicleshrms.purplefish;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionMaintance {
    Context ctx;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SessionMaintance(Context ctx)
    {
        this.ctx=ctx;
        prefs=ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor=prefs.edit();
    }

    public void set_user_mail(String user_mail){
        editor.putString("user_mail",user_mail);
        editor.commit();
    }

    public String get_user_mail(){ return prefs.getString("user_mail","");}



}
