package com.example.avinash.erailseva;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class tab2 extends Fragment {

    String uname;
    View myview;
    JSONArray jarray1,jarray2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_tab2, container, false);
        new ShowTransactions().execute();

        Button b1 = (Button) myview.findViewById(R.id.refresh);
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new ShowTransactions().execute();
            }
        });
        return myview;
    }

    private class ShowTransactions extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            String output,url_input = LoginActivity.url + "/customer/allorders/",method="GET";

            urlconnection myconnection = new urlconnection();
            System.out.println("aaaaaaaaaaa");
            output = myconnection.urlreturn(url_input,null,method,"b");
            System.out.println("bbbbbbb     "+output);
            return output;
        }

        @Override
        protected void onPostExecute(String strings) {
            if (strings != null) {
                try {
                    JSONObject jobj = new JSONObject(strings);
                    boolean status = jobj.getBoolean("status");
                    System.out.println("status all orders "+status);
                    if (status) {

                        jarray1 = jobj.getJSONArray("ongoing_orders");
                        jarray2 = jobj.getJSONArray("completed_orders");

                        TableLayout tl = (TableLayout) myview.findViewById(R.id.transactions);
                        TableLayout tl1 = (TableLayout) myview.findViewById(R.id.completed_transactions);

                        tl1.removeAllViews();
                        tl.removeAllViews();

                        View v1 = new View(getActivity());
                        v1.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v1.setBackgroundColor(Color.parseColor("#B3B3B3"));
                        tl.addView(v1);

                        View v2 = new View(getActivity());
                        v2.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                5
                        ));
                        v2.setBackgroundColor(Color.parseColor("#B3B3B3"));
                        tl1.addView(v2);

                        for(int i=0; i<jarray1.length(); i++){
                            boolean rb=false , cb=false;
                            final JSONObject json_data = jarray1.getJSONObject(i);
                            rb = json_data.getBoolean("showrb");
                            cb = json_data.getBoolean("showcb");
                            TableRow row1 = new TableRow(getActivity());
                            TableRow row2 = new TableRow(getActivity());
                            TableRow row3 = new TableRow(getActivity());
                            TableRow row4 = new TableRow(getActivity());
                            TableRow row5 = new TableRow(getActivity());
                            TableRow row6 = new TableRow(getActivity());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setLayoutParams(lp);
                            row2.setLayoutParams(lp);
                            row3.setLayoutParams(lp);
                            row4.setLayoutParams(lp);
                            row5.setLayoutParams(lp);
                            row6.setLayoutParams(lp);
                            //row.setBackgroundColor(Color.BLACK);
                            row1.setPadding(40, 0, 0, 4);
                            row2.setPadding(40, 40, 0, 4);
                            row3.setPadding(40, 0, 0, 4);
                            row4.setPadding(40, 0, 0, 4);
                            row5.setPadding(40, 0, 0, 4);
                            row6.setPadding(110, 0, 0, 40);

                            final TextView shop_id = new TextView(getActivity());
                            TextView shop_name = new TextView(getActivity());
                            TextView station_name = new TextView(getActivity());
                            TextView item_name = new TextView(getActivity());
                            TextView status1 = new TextView(getActivity());
                            TextView quantity = new TextView(getActivity());
                            Button review = new Button(getActivity());
                            Button cancel_order = new Button(getActivity());

                            shop_id.setText(""+json_data.getString("shop_id"));
                            station_name.setText("Station Name    : "+json_data.getString("station_name"));
                            shop_name.setText("Shop Name        : "+json_data.getString("shop_name"));
                            item_name.setText("Item Name         : "+json_data.getString("item_name"));
                            status1.setText("Status Of Order : "+json_data.getString("status"));
                            quantity.setText("Quantity              : "+Integer.toString(json_data.getInt("quantity")));
                            review.setText("ADD REVIEW");
                            cancel_order.setText("Cancel Order");

                            shop_name.setTextSize(18);
                            station_name.setTextSize(18);
                            item_name.setTextSize(18);
                            status1.setTextSize(18);
                            quantity.setTextSize(18);

                            Log.i("log",""+json_data.getString("shop_id")+
                            ","+json_data.getString("station_name")+
                            ","+json_data.getString("shop_name")+
                            ","+json_data.getString("item_name")+
                            ","+json_data.getString("status")+
                            ","+Integer.toString(json_data.getInt("quantity"))+
                            ","+json_data.getBoolean("showrb")+
                            ","+json_data.getString("showcb")
                            );
                            final String a = json_data.getString("shop_id");
                            final String b = json_data.getString("station_name");
                            final String c = json_data.getString("shop_name");
                            final int d = json_data.getInt("order_id");

                            review.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(),Review.class);
                                    intent.putExtra("shop_id",a);
                                    intent.putExtra("station_name",b);
                                    intent.putExtra("shop_name",c);
                                    getActivity().startActivity(intent);
                                }
                            });
                            cancel_order.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Alert")
                                            .setMessage("Are you sure , you want to cancel this order ?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    new cancelorder(d).execute();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            });
                            shop_id.setVisibility(View.GONE);
                            shop_name.setTextColor(Color.parseColor("#f50057"));
                            station_name.setTextColor(Color.parseColor("#f50057"));
                            item_name.setTextColor(Color.parseColor("#f50057"));
                            status1.setTextColor(Color.parseColor("#f50057"));
                            quantity.setTextColor(Color.parseColor("#f50057"));

                            View v = new View(getActivity());
                            v.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    5
                            ));
                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            row1.addView(shop_name);
                            row1.addView(shop_id);
                            row2.addView(station_name);
                            row3.addView(item_name);
                            row4.addView(quantity);
                            row5.addView(status1);

                            if(rb){
                                row6.addView(review);
                            }
                            if(cb){
                                row6.addView(cancel_order);
                            }
                            if(!(rb || cb)){
                                row5.setPadding(40, 0, 0, 40);
                            }

                            tl.addView(row2);
                            tl.addView(row1);
                            tl.addView(row3);
                            tl.addView(row4);
                            tl.addView(row5);
                            if(rb || cb){
                                tl.addView(row6);
                            }
                            tl.addView(v);

                        }

                        for(int i=0; i<jarray2.length(); i++){
                            boolean rb=false , cb=false;
                            final JSONObject json_data1 = jarray2.getJSONObject(i);
                            rb = json_data1.getBoolean("showrb");
                            cb = json_data1.getBoolean("showcb");
                            TableRow row1 = new TableRow(getActivity());
                            TableRow row2 = new TableRow(getActivity());
                            TableRow row3 = new TableRow(getActivity());
                            TableRow row4 = new TableRow(getActivity());
                            TableRow row5 = new TableRow(getActivity());
                            TableRow row6 = new TableRow(getActivity());
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row1.setLayoutParams(lp);
                            row2.setLayoutParams(lp);
                            row3.setLayoutParams(lp);
                            row4.setLayoutParams(lp);
                            row5.setLayoutParams(lp);
                            row6.setLayoutParams(lp);
                            //row.setBackgroundColor(Color.BLACK);
                            row1.setPadding(40, 0, 0, 4);
                            row2.setPadding(40, 40, 0, 4);
                            row3.setPadding(40, 0, 0, 4);
                            row4.setPadding(40, 0, 0, 4);
                            row5.setPadding(40, 0, 0, 4);
                            row6.setPadding(110, 0, 0, 40);

                            final TextView shop_id = new TextView(getActivity());
                            TextView shop_name = new TextView(getActivity());
                            TextView station_name = new TextView(getActivity());
                            TextView item_name = new TextView(getActivity());
                            TextView status1 = new TextView(getActivity());
                            TextView quantity = new TextView(getActivity());
                            Button review = new Button(getActivity());
                            Button cancel_order = new Button(getActivity());


                            shop_id.setText(""+json_data1.getString("shop_id"));
                            station_name.setText("Station Name    : "+json_data1.getString("station_name"));
                            shop_name.setText("Shop Name        : "+json_data1.getString("shop_name"));
                            item_name.setText("Item Name         : "+json_data1.getString("item_name"));
                            status1.setText("Status Of Order : "+json_data1.getString("status"));
                            quantity.setText("Quantity              : "+Integer.toString(json_data1.getInt("quantity")));
                            review.setText("ADD REVIEW");
                            cancel_order.setText("Cancel Order");

                            shop_name.setTextSize(18);
                            station_name.setTextSize(18);
                            item_name.setTextSize(18);
                            status1.setTextSize(18);
                            quantity.setTextSize(18);


//                            Log.i("log",""+json_data.getString("shop_id")+
//                            ","+json_data.getString("station_name")+
//                            ","+json_data.getString("shop_name")+
//                            ","+json_data.getString("item_name")+
//                            ","+json_data.getString("status")+
//                            ","+Integer.toString(json_data.getInt("quantity")));
                            final String a1 = json_data1.getString("shop_id");
                            final String b1 = json_data1.getString("station_name");
                            final String c1 = json_data1.getString("shop_name");
                            final int d1 = json_data1.getInt("order_id");

                            review.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(),Review.class);
                                    intent.putExtra("shop_id",a1);
                                    intent.putExtra("station_name",b1);
                                    intent.putExtra("shop_name",c1);
                                    getActivity().startActivity(intent);
                                }
                            });
                            cancel_order.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Alert")
                                            .setMessage("Are you sure , you want to cancel this order ?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    new cancelorder(d1).execute();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            });
                            String color = "#2e7d32";
                            shop_id.setVisibility(View.GONE);
                            shop_name.setTextColor(Color.parseColor(color));
                            station_name.setTextColor(Color.parseColor(color));
                            item_name.setTextColor(Color.parseColor(color));
                            status1.setTextColor(Color.parseColor(color));
                            quantity.setTextColor(Color.parseColor(color));

                            View v = new View(getActivity());
                            v.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    5
                            ));
                            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
                            row1.addView(shop_name);
                            row1.addView(shop_id);
                            row2.addView(station_name);
                            row3.addView(item_name);
                            row4.addView(quantity);
                            row5.addView(status1);

                            if(rb){
                                row6.addView(review);
                            }
                            if(cb){
                                row6.addView(cancel_order);
                            }
                            if(!(rb || cb)){
                                row5.setPadding(40, 0, 0, 40);
                            }
                            tl1.addView(row2);
                            tl1.addView(row1);
                            tl1.addView(row3);
                            tl1.addView(row4);
                            tl1.addView(row5);
                            if(rb || cb){
                                tl1.addView(row6);
                            }
                            tl1.addView(v);

                        }

                    } else {
                        String msg = jobj.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    //null error
                }
            }

        }
    }

    private class cancelorder extends AsyncTask<String, String, String> {

        int order_id;
        public cancelorder(int a){
            order_id = a;
        }

        @Override
        protected String doInBackground(String... args) {
            JSONObject jsoninput = new JSONObject();
            String output,url_input = LoginActivity.url + "/customer/cancelorder/",method="POST";

            try {
                jsoninput.put("order_id" , order_id);
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
                    System.out.println("status reviews "+status);
                    if (status) {
                        Toast.makeText(getActivity(),"Your order has been cancelled successfully", Toast.LENGTH_SHORT).show();
                        new ShowTransactions().execute();

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
