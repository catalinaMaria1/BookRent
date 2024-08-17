package com.example.bookrent;

import static com.example.bookrent.MainActivity.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewBooks;
    private BooksAdapter booksAdapter;
    private BooksDBHelper dbHelper;
    private MyDataBase myDataBase;
    ArrayList<Book> ownedBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);
        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new BooksDBHelper(getActivity());
        myDataBase = new MyDataBase(getActivity());
        ArrayList<Book> booksList = dbHelper.getAllBooks();

        ownedBooks=user.getOwnedBooks();

        booksAdapter = new BooksAdapter(getActivity(), booksList, myDataBase);
        booksAdapter.updateBooks(ownedBooks);
        recyclerViewBooks.setAdapter(booksAdapter);
        return view;
    }

}