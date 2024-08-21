package com.example.bookrent;

import java.util.ArrayList;

public class User {
    private int id =-1;
    private String fullname;
    private String email;
    private float amount;
    private ArrayList<Book> cart;
    private ArrayList<Book> ownedBooks= new ArrayList<>();
    private String profilePic;

    public User(int id, String fullname, String email, ArrayList<Book> cart, ArrayList<Book> ownedBooks) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.cart = cart;
        this.ownedBooks=ownedBooks;
    }

    public User() {

    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setAmount(float amount) {
        this.amount=amount;
    }

    public void setCart(ArrayList<Book> data){this.cart=data;}

    public void setOwnedBooks(ArrayList<Book> data){this.ownedBooks.clear(); this.ownedBooks.addAll(data);}

    public void removeFromCart(Book b){
        this.cart.remove(b);
    }

    public void clearCart(){this.ownedBooks.clear();}

    public String getFullname() { return fullname; }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Book> getCart() {
        return cart;
    }

    public ArrayList<Book> getOwnedBooks(){
        return ownedBooks;
    }

    public Float getAmount(){
        return amount;
    }
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePicUri) {
        this.profilePic = profilePicUri;
    }


}