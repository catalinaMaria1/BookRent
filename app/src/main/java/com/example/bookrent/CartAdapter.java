package com.example.bookrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> booksList;
    private final BooksDBHelper dbHelper;

    public CartAdapter(Context context, List<Book> booksList, BooksDBHelper dbHelper) {
        this.context = context;
        this.booksList = booksList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText("Author: " + book.getAuthor());
        holder.textViewDescription.setText("Description: " + book.getDescription());
        holder.textViewReviews.setText("Reviews: " + book.getReviews());
        holder.textViewPrice.setText("Price: $" + book.getPrice()); // Show price

        Glide.with(context)
                .load(book.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.imageViewCover);

        holder.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.removeBookFromCart(book.getId());
                updateBooks(dbHelper.getBooksInCart());
                Toast.makeText(context, "Book removed", Toast.LENGTH_SHORT).show();
            }
        } );
    }

    @Override
    public int getItemCount() {
        return booksList != null ? booksList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewDescription, textViewReviews, textViewPrice;
        ImageView imageViewCover;
        Button buttonRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewReviews = itemView.findViewById(R.id.textViewReviews);
            textViewPrice = itemView.findViewById(R.id.textViewPrice); // Added price TextView
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

    public void updateBooks(List<Book> updatedBooksList) {
        booksList.clear();
        booksList.addAll(updatedBooksList);
        notifyDataSetChanged();
    }
}