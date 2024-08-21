package com.example.bookrent;

import static com.example.bookrent.MainActivity.user;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewBooks;
    private BooksAdapter booksAdapter;
    private BooksDBHelper dbHelper;
    private MyDataBase myDataBase;
    private SearchView searchView;
    ArrayList<Book> ownedBooks;
    ArrayList<Book> booksList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home, container, false);
        recyclerViewBooks = view.findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView=view.findViewById(R.id.search_bar);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        dbHelper = new BooksDBHelper(getActivity());
        myDataBase = new MyDataBase(getActivity());
        booksList = dbHelper.getAllBooks();

        ownedBooks=user.getOwnedBooks();

        booksAdapter = new BooksAdapter(getActivity(), booksList, myDataBase);
        booksAdapter.updateBooks(ownedBooks);
        recyclerViewBooks.setAdapter(booksAdapter);
        return view;
    }

    private void filterList(String text) {
        List<Book> filteredList=new ArrayList<>();
        for(Book item:booksList){
            if(item.getTitle().toLowerCase().contains(text.toLowerCase()) || item.getAuthor().toLowerCase().contains(text.toLowerCase()) || item.getCategory().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(),"No Book Found!",Toast.LENGTH_SHORT).show();
        }
        else{
            booksAdapter.setFilteredList(filteredList);
        }
    }

}