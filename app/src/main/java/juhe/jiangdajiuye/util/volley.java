package juhe.jiangdajiuye.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by wangqiang on 2016/9/27.
 */
public class volley  {
    public Context context;
    private  RequestQueue requestQueue;
    public StringBuffer sb ;
    public getStringLister stringLister;
    public getJsonLister jsonLister;
    volley(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }
    public interface getStringLister{
         void getString(String response);
    }
    public interface getJsonLister{
        void getJson(JSONObject jsonObject);
    }
    public void setLister(getStringLister stringLister){
        this.stringLister = stringLister;
    }
    public void setLister(getJsonLister jsonLister){
        this.jsonLister = jsonLister;
    }
    public String get(String url){
        sb = new StringBuffer();
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sb.append(response);
                        stringLister.getString(sb.toString());
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.getMessage(), error);
            }
        });
        requestQueue.add(stringRequest);
        return sb.toString();
    }


}
