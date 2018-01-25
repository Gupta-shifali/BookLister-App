package com.example.hpnotebook.booklister;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Hp Notebook on 21-01-2018.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_book, parent, false);
        }

        final Book currentBook = getItem(position);

        TextView nameView = listItemView.findViewById(R.id.name);
        nameView.setText(currentBook.getName());
        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentBook.getAuthor());
        TextView ratingsView = listItemView.findViewById(R.id.ratings);
        ratingsView.setText(currentBook.getRatings() + "");
        TextView priceView = listItemView.findViewById(R.id.price);
        priceView.setText("INR " + currentBook.getPrice());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(currentBook.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                getContext().startActivity(webIntent);
            }
        });
        return listItemView;
    }
}