package com.example.bookrent;

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

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private BooksDBHelper dbHelper;
    private Button buy;
    private Double price=0.0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_cart, container, false);

        buy=view.findViewById(R.id.purchaseButton);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new BooksDBHelper(getActivity());
        ArrayList<Book> booksInCart = dbHelper.getBooksInCart();


        cartAdapter = new CartAdapter(getActivity(), booksInCart, dbHelper);
        recyclerViewCart.setAdapter(cartAdapter);
        booksInCart.forEach((book)->{
            price+=book.getPrice();
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if(cont.bani <price)
                bani bani bani baga bani bani wowowowo
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).addToBackStack(null).commit();

                else{
                    cont.bani=cont.bani-price sau idk cont.scadeBani(price)
                }
                 */

            }
        });



        Toast.makeText(getContext(),"Price is"+String.valueOf(price),Toast.LENGTH_SHORT).show();

        return view;
    }
}