package com.example.bookrent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private BooksAdapter booksAdapter;
    private BooksDBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_cart, container, false);

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new BooksDBHelper(getActivity());
        ArrayList<Book> booksInCart = dbHelper.getBooksInCart();

        booksAdapter = new BooksAdapter(getActivity(), booksInCart, dbHelper);
        recyclerViewCart.setAdapter(booksAdapter);

        return view;
    }
}
