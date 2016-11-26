package com.example.avinash.erailseva;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class order_frag4 extends Fragment {

    String uname,pnr_no,station_name,shop_name,station_id,shop_id,pay_mode;
    int bill_cost=0;
    View myview;
    AutoCompleteTextView text;
    List<String> responseList = new ArrayList<String>();
    Button b1,b2;
    JSONArray orders = new JSONArray(),order_ids = new JSONArray();
    final CharSequence[] payment_method = {"Cash On Delivery", "Pay From Wallet"};
    final ArrayList seletedItems=new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            pnr_no = bundle.getString("pnr");
            station_name = bundle.getString("station");
            shop_name = bundle.getString("shop");
            uname = bundle.getString("uname");
            station_id = bundle.getString("station_id");
            shop_id = bundle.getString("shop_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_order_frag4, container, false);

        TextView t1 = (TextView) myview.findViewById(R.id.pnr);
        t1.setText("PNR NUMBER    : "+pnr_no);
        TextView t2 = (TextView) myview.findViewById(R.id.Station_no);
        t2.setText("STATION NAME : "+station_name);
        TextView t3 = (TextView) myview.findViewById(R.id.Shop_name);
        t3.setText("SHOP NAME      : "+shop_name);
        //System.out.println("SHOP ID : "+shop_id);
        new showitems().execute();

        b1 = (Button) myview.findViewById(R.id.back1);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Fragment fragment = new order_frag3();
                Bundle bundle = new Bundle();
                bundle.putString("pnr",pnr_no);
                bundle.putString("station",station_name);
                bundle.putString("shop",shop_name);
                bundle.putString("uname",uname);
                bundle.putString("station_id",station_id);
                bundle.putString("tab","4");
                fragment.setArguments(bundle);
                LinearLayout layout = (LinearLayout) myview.findViewById(R.id.frag4);
                layout.removeAllViews();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.frag4, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        b2 = (Button) myview.findViewById(R.id.submit_order);
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                bill_cost=0;
                TableLayout tl = (TableLayout) myview.findViewById(R.id.itemlist);
                boolean atleast_one_item_order=false;
                orders = new JSONArray();

                for (int i=0;i<tl.getChildCount();i++){
                    TableRow row = (TableRow)tl.getChildAt(i);
                    TextView item_id = (TextView)row.getChildAt(0); // get child index on particular row
                    TextView item_name = (TextView)row.getChildAt(1);
                    TextView item_cost = (TextView)row.getChildAt(2);
                    EditText item_quantity = (EditText) row.getChildAt(3);

                    try{
                        if(!item_quantity.getText().toString().equals("")){
                            if(Integer.parseInt(item_quantity.getText().toString())>0){

                                bill_cost+=Integer.parseInt(item_quantity.getText().toString())*Integer.parseInt(item_cost.getText().toString());

                                atleast_one_item_order = true;
                                JSONObject temp = new JSONObject();
                                temp.put("id",item_id.getText().toString());
                                temp.put("quantity",Integer.parseInt(item_quantity.getText().toString()));
                                orders.put(temp);
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i("log",","+item_id.getText().toString() +
                            ","+item_name.getText().toString() +
                            ","+item_cost.getText().toString() +
                            ","+item_quantity.getText().toString());
                }

                if(atleast_one_item_order){
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Select The Payment Method"+"\n"+"Your bill is Rs "+bill_cost)
                            .setSingleChoiceItems(payment_method, 0,null)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on OK
                                    //  You can write the code  to save the selected item here

                                    dialog.dismiss();
                                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                    if(selectedPosition==0){
                                        pay_mode="0";
                                    }
                                    if(selectedPosition==1){
                                        pay_mode="1";
                                    }
                                    new placeorder().execute();
                                    //System.out.println("bbbbbbb     "+selectedPosition);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Your code when user clicked on Cancel
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
                else{
                    Toast.makeText(getActivity(), "Atleast one item should have a quantity of greater or equal to 1", Toast.LENGTH_SHORT).show();
                };

            }
        });

        return myview;
    }

    private class showitems extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url +"/shop/getitems/",method="POST";

            try {
                jsoninput.put("shop_id" , shop_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlconnection myconnection = new urlconnection();
            //System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,jsoninput,method,"b");
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

                        JSONArray jarray = jobj.getJSONArray("items");
                        System.out.println("*****JARRAY*****" + jarray.length());
                        TableLayout tl = (TableLayout) myview.findViewById(R.id.itemlist);
                        tl.removeAllViews();

                        for(int i=0; i<jarray.length(); i++){
                            final JSONObject json_data = jarray.getJSONObject(i);
                            TableRow row = new TableRow(getActivity());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            //row.setBackgroundColor(Color.BLUE);
                            row.setPadding(0, 0, 0, 2);

                            TextView item_name = new TextView(getActivity());
                            TextView item_id = new TextView(getActivity());
                            TextView item_cost = new TextView(getActivity());
                            EditText quantity = new EditText(getActivity());
                            quantity.setGravity(Gravity.CENTER);

                            item_id.setText(json_data.getString("id"));
                            item_name.setText(json_data.getString("name"));
                            item_cost.setText(Integer.toString(json_data.getInt("cost")));
                            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);

                            item_id.setVisibility(View.GONE);
                            item_name.setTextColor(Color.parseColor("#f50057"));
                            quantity.setHintTextColor(Color.parseColor("#f50057"));
                            item_cost.setTextColor(Color.parseColor("#f50057"));

                            row.addView(item_id);
                            row.addView(item_name);
                            row.addView(item_cost);
                            row.addView(quantity);

                            tl.addView(row);

//                            Log.i("log_tag", "" + json_data.getString("id") +
//                                    ", " + json_data.getString("name") + "," + json_data.getString("cost")
//                            );
                        }

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

    private class placeorder extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url + "/customer/placeorder/",method="POST";

            try {
                jsoninput.put("pnr" , pnr_no);
                jsoninput.put("shop_id" , shop_id);
                jsoninput.put("items",orders);
                jsoninput.put("paymode",pay_mode);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            urlconnection myconnection = new urlconnection();
            //System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,jsoninput,method,"b");
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
                        Toast.makeText(getActivity(),"Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                        JSONArray jarray = jobj.getJSONArray("order_ids");
                        System.out.println("*****JARRAY*****" + jarray.length());

                        for(int i=0; i<jarray.length(); i++){
                            //final JSONObject json_data = jarray.getJSONObject(i);
                            order_ids.put(jarray.getInt(i));
                            Log.i("log_tag", "" + jarray.getInt(i)
                            );
                        }
                        Intent intent = new Intent(getActivity(),OrderForm.class);

                        intent.putExtra("username",uname);
//                        intent.putExtra("reason","order");
                        getActivity().startActivity(intent);

                    } else {
                        String msg = jobj.getString("msg");
                        System.out.println(msg);
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }
}
