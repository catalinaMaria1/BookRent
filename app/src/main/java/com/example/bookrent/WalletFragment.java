package com.example.bookrent;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookrent.databinding.ActivityWalletBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletFragment extends Fragment {

    String Secret="sk_test_51PlTHpBjbVPIcwK8pRgVZgSfJjPLkjh4P0Cg3iRGoPPsXEgjT7bjfGx3ejKYBW8t0Aw6qypxYTOSOArSFqSycMT600uto3qHBc";
    String Publish="pk_test_51PlTHpBjbVPIcwK8Uxs5BLVdMcuYxirJXSOU3HHXXlXjQCQ449lHkKiRUPidxMSzVXUBGEVQihzvUsqB55svvrCw006Bt4sfil";

    PaymentSheet paymentSheet;

    String customerID;
    String EphericalKey;
    String ClientSecret;
    String amount;

    ActivityWalletBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Stripe SDK configuration here
        PaymentConfiguration.init(getContext(), Publish);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ActivityWalletBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize PaymentSheet after the view is created
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> onPaymentResult(paymentSheetResult));

        Button button = binding.addMoney;
        EditText text = binding.moneyField;
        text.setInputType(InputType.TYPE_CLASS_NUMBER);

        button.setOnClickListener(view1 -> {
            amount = String.valueOf(text.getText());
            fetchData(amount);
        });

        // Fetch data after everything is set up
    }

    private void fetchData(String amount){



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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(getContext(),"payment succeful",Toast.LENGTH_SHORT).show();

        }
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

                            getClientSecret(customerID, EphericalKey);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),"ERROR",Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String ephericalKey) {
        StringRequest stringRequest= new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object=new JSONObject(response);
                            ClientSecret=object.getString("client_secret");

                            Toast.makeText(getActivity(),ClientSecret,Toast.LENGTH_SHORT).show();

                            PaymentFlow();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),String.valueOf(error),Toast.LENGTH_SHORT).show();
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
                params.put("amount", amount+"00");
                params.put("currency", "ron");

                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
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