package com.example.bookrent;


import static com.example.bookrent.MainActivity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.util.ArrayList;

public class CartFragment extends Fragment {


    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private MyDataBase dbHelper;
    private BooksDBHelper booksDBHelper;
    private String amount;
    private Button buy;
    private float price = 0.0F;

    private PaymentUtil paymentUtil;
    private PaymentSheet paymentSheet;
    ArrayList<Book> booksInCart=new ArrayList<Book>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentUtil=new PaymentUtil(getActivity());
        PaymentConfiguration.init(getContext(),paymentUtil.Publish);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart, container, false);

        buy = view.findViewById(R.id.purchaseButton);




        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new MyDataBase(getActivity());
        booksDBHelper=new BooksDBHelper(getActivity());

        String[] idList=dbHelper.getList(String.valueOf(user.getId()));
        if(idList[0]!="not found!" &&  !idList[0].isEmpty()){
            for(int i=0;i<idList.length;i++){
                Book b=booksDBHelper.getBook(idList[i]);
                booksInCart.add(b);
            }
        }
        user.setCart(booksInCart);

        cartAdapter = new CartAdapter(getActivity(), booksInCart, dbHelper);
        recyclerViewCart.setAdapter(cartAdapter);

        booksInCart.forEach(book -> {
            price += book.getPrice();
        });


        paymentSheet=new PaymentSheet(this, paymentSheetResult -> onPaymentResult(paymentSheetResult,0.0f));

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(price>user.getAmount()){
                    paymentUtil.setPaymentSheet(paymentSheet);
                    amount= String.valueOf(price- user.getAmount());
                    Toast.makeText(getContext(),amount,Toast.LENGTH_SHORT).show();
                    paymentUtil.setAmount(amount);
                    paymentUtil.fetchData();
                }
                else{
                    user.setOwnedBooks(booksInCart);
                    dbHelper.addBooksOwned(String.valueOf(user.getId()),booksInCart);
                    user.setAmount(user.getAmount()-price);
                    dbHelper.updateMoney(String.valueOf(user.getId()),user.getAmount()-price);
                    user.clearCart();
                    dbHelper.clearCart(String.valueOf(user.getId()));
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                }
            }

        });




        return view;
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult,float money) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(getContext(),"payment successful",Toast.LENGTH_SHORT).show();
            user.setAmount(money);
            dbHelper.updateMoney(String.valueOf(user.getId()),money);
            user.clearCart();
            dbHelper.clearCart(String.valueOf(user.getId()));
            user.setOwnedBooks(booksInCart);
            if(!dbHelper.addBooksOwned(String.valueOf(user.getId()),booksInCart)){
                Toast.makeText(getContext(),"ADDED FAILED",Toast.LENGTH_SHORT).show();
            }
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
        }
    }
}