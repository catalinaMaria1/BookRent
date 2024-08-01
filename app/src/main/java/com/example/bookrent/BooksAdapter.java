package com.example.bookrent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private final Context context;
    private final List<Book> booksList;

    public BooksAdapter(Context context, List<Book> booksList) {
        this.context = context;
        this.booksList = booksList;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewDescription, textViewReviews;
        ImageView imageViewCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewReviews = itemView.findViewById(R.id.textViewReviews);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
        }
    }

    public void updateBooks(List<Book> updatedBooksList) {
        booksList.clear();
        booksList.addAll(updatedBooksList);
        notifyDataSetChanged();
    }
}
