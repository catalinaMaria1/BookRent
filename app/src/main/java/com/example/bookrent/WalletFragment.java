package com.example.bookrent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookrent.databinding.ActivityWalletBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import static com.example.bookrent.MainActivity.user;

public class WalletFragment extends Fragment {
    private PaymentUtil paymentUtil;


    private PaymentSheet paymentSheet;

    private String amount;
    private MyDataBase dataBase;

    ActivityWalletBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Stripe SDK configuration here
        paymentUtil=new PaymentUtil(getActivity());
        PaymentConfiguration.init(getContext(), paymentUtil.Publish);
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
        paymentUtil.setPaymentSheet(paymentSheet);

        Button button = binding.addMoney;
        EditText text = binding.moneyField;
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        dataBase=new MyDataBase(getActivity());

        button.setOnClickListener(view1 -> {
            amount = String.valueOf(text.getText());
            paymentUtil.setAmount(amount);
            paymentUtil.fetchData();
        });

    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(getContext(),"payment succeful",Toast.LENGTH_SHORT).show();
            float money=Float.valueOf(amount)+user.getAmount();
            user.setAmount(money);
            dataBase.updateMoney(String.valueOf(user.getId()),money);
        }
    }
}