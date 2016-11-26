package com.example.avinash.erailseva;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class order_frag3 extends Fragment {

    String uname,pnr_no,station_name,shop_name="",station_id,shop_id;
    View myview;
    AutoCompleteTextView auto_shop_name;
    List<String> responseList = new ArrayList<String>();
    Button b1,b2;
    JSONArray jarray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            pnr_no = bundle.getString("pnr");
            station_name = bundle.getString("station");
            uname = bundle.getString("uname");
            String from_tab = bundle.getString("tab");
            station_id = bundle.getString("station_id");
            if(from_tab == "4"){
                shop_name = bundle.getString("shop");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_order_frag3, container, false);
        System.out.println("station_id  frag3  : " + station_id);
        TextView t1 = (TextView) myview.findViewById(R.id.pnr);
        t1.setText("PNR NUMBER    : "+pnr_no);
        TextView t2 = (TextView) myview.findViewById(R.id.Station_no);
        t2.setText("STATION NAME : "+station_name);
        auto_shop_name = (AutoCompleteTextView) myview.findViewById(R.id.shop_name);
        if(shop_name != ""){
            auto_shop_name.setText(shop_name,TextView.BufferType.EDITABLE);
        }
        responseList.clear();
        new autocompleteshops().execute();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, responseList);
        auto_shop_name.setAdapter(adapter);
        auto_shop_name.setThreshold(1);

        b1 = (Button) myview.findViewById(R.id.back1);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new order_frag2();
                Bundle bundle = new Bundle();
                bundle.putString("pnr",pnr_no);
                bundle.putString("station",station_name);
                bundle.putString("uname",uname);
                bundle.putString("station_id",station_id);
                bundle.putString("tab","3");
                fragment.setArguments(bundle);
                LinearLayout layout = (LinearLayout) myview.findViewById(R.id.frag3);
                layout.removeAllViews();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frag3, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        b2 = (Button) myview.findViewById(R.id.next1);
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkshops()){
                    Fragment fragment = new order_frag4();
                    AutoCompleteTextView t3 = (AutoCompleteTextView) myview.findViewById(R.id.shop_name);
                    shop_name = t3.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("pnr",pnr_no);
                    bundle.putString("station",station_name);
                    bundle.putString("shop",shop_name);
                    bundle.putString("station_id",station_id);
                    bundle.putString("shop_id",shop_id);
                    bundle.putString("uname",uname);
                    fragment.setArguments(bundle);
                    LinearLayout layout = (LinearLayout) myview.findViewById(R.id.frag3);
                    layout.removeAllViews();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.frag3, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }
        });
        return myview;
    }

    private class autocompleteshops extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input =  LoginActivity.url +"/shop/getshops/",method="POST";
            try {

                jsoninput.put("station_id" , station_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlconnection url_connect = new urlconnection();
            output = url_connect.urlreturn(url_input,jsoninput,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if(strings!=null){
                try{
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if(status){
                        jarray = jobj.getJSONArray("shops");
                        System.out.println("*****JARRAY*****" + jarray.length());
                        String suggestions;
                        for(int i=0; i<jarray.length(); i++){
                            JSONObject json_data = jarray.getJSONObject(i);
                            suggestions=json_data.getString("name");
                            //suggestions+=json_data.getString("name");
                            responseList.add(suggestions);
                            //System.out.println(suggestions[i]);
//                            Log.i("log_tag", "" + json_data.getString("id") +
//                                    ", " + json_data.getString("name")
//                            );
                        }

                    }else{
                        //String msg = jobj.getString("message");
                        Toast.makeText(getActivity(), "status false",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    //null error
                }
            }

        }

    }

    public boolean checkshops(){
        boolean flag=true;

        // Reset errors.
        auto_shop_name.setError(null);
        String shname = auto_shop_name.getText().toString();

        boolean cancel = false;
        boolean shfound = false;
        View focusView = null;


        if (TextUtils.isEmpty(shname)) {
            auto_shop_name.setError("This is field cannot be empty");
            focusView = auto_shop_name;
            cancel = true;
        }

        String data = auto_shop_name.getText().toString();
        for(String s : responseList){
            if(s.toLowerCase().equals(data.toLowerCase())){
                shfound = true;
                break;
            }
        }

        if (!shfound) {
            auto_shop_name.setError("Station name should be selected from the drop down");
            focusView = auto_shop_name;
            cancel = true;
        }
        else{
            for(int i=0; i<jarray.length(); i++){
                String s1,s2;
                try{
                    JSONObject json_data = jarray.getJSONObject(i);
                    s1=json_data.getString("name");
                    s2=json_data.getString("id");
                    if(s1.equals(data)){
                        shop_id=s2;
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            flag=false;
            focusView.requestFocus();
        }

        return flag;
    }
}
