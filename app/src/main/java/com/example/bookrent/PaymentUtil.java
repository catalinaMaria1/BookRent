package com.example.bookrent;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.paymentsheet.PaymentSheet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentUtil{
    private String amount;
    private PaymentSheet paymentSheet;
    private Activity activity;


    public PaymentUtil(Activity activity){
        this.activity=activity;
    }
    public void setAmount(String amount){
        if(amount.contains(".0")){
            amount=amount.replace(".","0");
        }
        else if(amount.contains(",")){
            amount=amount.replace(",","");
        }
        else if(amount.contains(".")){
            amount=amount.replace(".","");
        }
        else{
            amount=amount+"00";
        }
        Log.w("Amount",amount);
        this.amount=amount;

    }
    public void setPaymentSheet(PaymentSheet paymentSheet){
        this.paymentSheet=paymentSheet;
    }

    public String Secret="sk_test_51PlXVSCd8efrn7mxsszHUhYeHRavvy7GOs2Sz45lMOEZEjT6FSaoVodoCuCdUfH9M2HHgtZ5dEAlSBKpgeZPjbDR00pvrWuBn6";
    public String Publish="pk_test_51PlXVSCd8efrn7mxQXcLVDgbs5x2VTk0Q8I9yhe5Is2DExc9XKWZuDbYEf23VEGiIppz0SSFyWE6vFcCDqlXi0wX007nTa0LHF";

    private String customerID;
    private String EphericalKey;
    private String ClientSecret;



    public void fetchData(){



        StringRequest stringRequest= new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object=new JSONObject(response);
                            customerID=object.getString("id");

                            getEphericalKey(customerID);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header=new HashMap<>();
                header.put("Authorization","Bearer "+Secret);
                return header;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    private void getEphericalKey(String customerID){
        StringRequest stringRequest= new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object=new JSONObject(response);
                            EphericalKey=object.getString("id");

                            getClientSecret(customerID);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(activity,"ERROR",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header=new HashMap<>();
                header.put("Authorization","Bearer "+Secret);
                header.put("Stripe-Version","2024-06-20");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("customer",customerID);

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object=new JSONObject(response);
                            ClientSecret=object.getString("client_secret");

                            Toast.makeText(activity,ClientSecret,Toast.LENGTH_SHORT).show();

                            PaymentFlow();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,String.valueOf(error),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header=new HashMap<>();
                header.put("Authorization","Bearer "+Secret);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("customer",customerID);
                params.put("amount", amount);
                params.put("currency", "ron");

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() {
        paymentSheet.presentWithPaymentIntent(ClientSecret,new PaymentSheet.Configuration("BookRent",
                new PaymentSheet.CustomerConfiguration(
                        customerID,
                        EphericalKey
                ))
        );
    }

}