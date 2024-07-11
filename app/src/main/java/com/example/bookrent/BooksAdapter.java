package com.example.bookrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Book> booksList;
    private OnBookDeleteClickListener deleteClickListener;

    public BooksAdapter(Context context, ArrayList<Book> booksList, OnBookDeleteClickListener deleteClickListener) {
        this.context = context;
        this.booksList = booksList;
        this.deleteClickListener = deleteClickListener;
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

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(book);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewDescription, textViewReviews;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewReviews = itemView.findViewById(R.id.textViewReviews);
            buttonDelete = itemView.findViewById(R.id.buttonEraseBook);
        }
    }

    public interface OnBookDeleteClickListener {
        void onDeleteClick(Book book);
    }

    // Metodă pentru actualizarea listei de cărți în adaptor
    public void updateBooks(ArrayList<Book> updatedBooksList) {
        booksList.clear();
        booksList.addAll(updatedBooksList);
        notifyDataSetChanged();
    }
}
