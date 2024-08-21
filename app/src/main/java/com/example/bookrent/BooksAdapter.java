 package com.example.bookrent;

import static com.example.bookrent.MainActivity.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private final Context context;
    private List<Book> booksList;
    private MyDataBase myDataBase;
    private ArrayList<Book> userBooks;

    public void setFilteredList(List<Book> filteredList){
        this.booksList=filteredList;
        notifyDataSetChanged();
    }



    public BooksAdapter(Context context, List<Book> booksList, MyDataBase myDataBase) {
        this.context = context;
        this.booksList = booksList;
        this.userBooks=new ArrayList<Book>();
        this.myDataBase=myDataBase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText("Author: " + book.getAuthor());
        holder.textViewDescription.setText("Description: " + book.getDescription());
        holder.textViewReviews.setText("Reviews: " + book.getReviews());
        holder.textViewPrice.setText("Price: $" + book.getPrice());
        holder.textViewCategory.setText("Category: " + book.getCategory());

        boolean condition=false;
        for(Book book1 :userBooks)
            if(book1.getId()==book.getId())condition=true;

        holder.buttonAdd.setText(condition ? "Book Owned" : "Buy now!");
        holder.buttonAdd.setEnabled(!condition);
        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(user.getId()==-1){
                    Toast.makeText(context,"Please Log in!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if( !myDataBase.insertLast(String.valueOf(user.getId()),String.valueOf(book.getId()))){
                        Toast.makeText(context, "Book added to cart", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Book already in cart", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        Glide.with(context)
                .load(book.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.imageViewCover);

    }

    @Override
    public int getItemCount() {
        return booksList != null ? booksList.size() : 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewDescription, textViewReviews, textViewPrice, textViewCategory;
        ImageView imageViewCover;
        Button buttonAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewReviews = itemView.findViewById(R.id.textViewReviews);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
        }
    }


    public void updateBooks(List<Book> updatedBooksList) {
        if(!userBooks.isEmpty())
            userBooks.clear();
        userBooks.addAll(updatedBooksList);
        notifyDataSetChanged();
    }
}