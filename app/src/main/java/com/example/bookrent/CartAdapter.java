package com.example.bookrent;

import static com.example.bookrent.MainActivity.user;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Book> booksInCart;
    private MyDataBase dbHelper;

    public CartAdapter(Context context, ArrayList<Book> booksInCart, MyDataBase dbHelper) {
        this.context = context;
        this.booksInCart = booksInCart;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_book, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = booksInCart.get(position);
        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookPrice.setText("$" + book.getPrice());
        holder.bookDescription.setText(book.getDescription());
        holder.bookReviews.setText(book.getReviews());
        Glide.with(context)
                .load(book.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.bookCover);

    }


    @Override
    public int getItemCount() {
        return booksInCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookPrice, bookDescription,bookReviews;
        ImageView bookCover;
        Button removeBook;
        private CartAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.cartTextViewTitle);
            bookAuthor = itemView.findViewById(R.id.cartTextViewAuthor);
            bookPrice = itemView.findViewById(R.id.cartTextViewPrice);
            bookCover=itemView.findViewById(R.id.imageViewCover);
            bookDescription=itemView.findViewById(R.id.cartTextViewDescription);
            bookReviews=itemView.findViewById(R.id.cartTextViewReviews);

            removeBook = itemView.findViewById(R.id.cartButtonRemove);
            removeBook.setOnClickListener(view ->{
                int index=getAdapterPosition();
                user.removeFromCart(booksInCart.get(index));
                dbHelper.removeFromCart(String.valueOf(user.getId()),index);
                adapter.notifyItemRemoved(index);
            });
        }
        public ViewHolder linkAdapter(CartAdapter adapter){
            this.adapter=adapter;
            return this;
        }
    }
}