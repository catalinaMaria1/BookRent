package com.example.bookrent;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewBooks;
    private BooksAdapter booksAdapter;
    private BooksDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_home, container, false);
        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        dbHelper = new BooksDBHelper(getActivity());
        ArrayList<Book> booksList = dbHelper.getAllBooks();

        booksAdapter = new BooksAdapter(getActivity(), booksList, dbHelper);
        recyclerViewBooks.setAdapter(booksAdapter);
        return view;
    }

}
