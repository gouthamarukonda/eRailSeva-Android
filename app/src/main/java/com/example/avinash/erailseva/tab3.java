package com.example.avinash.erailseva;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.Inet4Address;


public class tab3 extends Fragment {

    String uname;
    View myview;
    EditText t1;
    Integer walletamount;
    String addamount;int money;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_tab3, container, false);
        new getwalletamount().execute();

        t1 = (EditText)myview.findViewById(R.id.setmoney);
        Button b1 = (Button) myview.findViewById(R.id.addmoney);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(checkwallet()){
                    new addwalletamount().execute();
                }

            }
        });
        return myview;
    }

    private class getwalletamount extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/getwalletamount/",method="GET";

            urlconnection myconnection = new urlconnection();
            //System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,null,method,"b");
            //System.out.println("bbbbbbb     "+output);
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        walletamount = jobj.getInt("wallet_amount");
                        //System.out.println(walletamount+"walletamount");
                        TextView walletmoney = (TextView) myview.findViewById(R.id.getmoney);
                        walletmoney.setText(walletamount.toString());
                    } else {
//                        String msg = jobj.getString("msg");
//                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "registration unsuccessful",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }

    private class addwalletamount extends AsyncTask<String, String, String> {
        EditText txt1 = (EditText)myview.findViewById(R.id.setmoney);
        String addm = txt1.getText().toString();
        int money = Integer.parseInt(addm);

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url + "/customer/addwalletamount/",method="POST";
            try {

                jsoninput.put("amount", money);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            urlconnection myconnection = new urlconnection();
            output = myconnection.urlreturn(url_input,jsoninput,method,"a");
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    if (status) {
                        walletamount = jobj.getInt("wallet_amount");
                        TextView walletmoney = (TextView) myview.findViewById(R.id.getmoney);
                        walletmoney.setText(walletamount.toString());
                        //System.out.println(walletamount+"    walletamount");
                        Toast.makeText(getActivity(), "money added to wallet successful",Toast.LENGTH_SHORT).show();
                    } else {
//                        String msg = jobj.getString("msg");
//                        Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "operation unsuccessful,try again",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }

    }

    public boolean checkwallet(){
        boolean flag=true;

        // Reset errors.
        t1.setError(null);
        String wallet = t1.getText().toString();



        boolean cancel = false;
        View focusView = null;

        if(!wallet.equals("")){
            int a = Integer.parseInt(wallet);
            if(a==0){
                t1.setError("Amount to be added must be greater than 0");
                focusView = t1;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(wallet)) {
            t1.setError("This is field cannot be empty");
            focusView = t1;
            cancel = true;
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

