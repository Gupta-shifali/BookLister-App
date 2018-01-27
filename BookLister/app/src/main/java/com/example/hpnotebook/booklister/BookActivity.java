package com.example.hpnotebook.booklister;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    private static final String LOG_TAG = BookActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private String REQUEST_URL = "";
    private BookAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private EditText simpleEditText;
    private String strValue ;
    private View loadingIndicator;
    private boolean isRunning = false;
    private ListView bookListView;

    private List<Book> books_list;

    private Parcelable state = null;
    private static final String LIST_STATE = "listState";
    private static final String BOOKLIST_SCROLL_POSITION = "Position of Scroll";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookListView = findViewById(R.id.listView);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setVisibility(View.GONE);

        loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);

        onSearch();
    }

    private void onSearch() {
        Button mSearchButton = findViewById(R.id.search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMethod();
            }
        });
    }

    private void searchMethod(){
        simpleEditText = findViewById(R.id.text);
        strValue = simpleEditText.getText().toString();
        Log.e(LOG_TAG, strValue);
        if (strValue.length() < 1 || strValue == null) {
            Toast.makeText(getApplicationContext(), "Enter a valid book name..", Toast.LENGTH_SHORT).show();
        } else {
            if(isRunning == true){
                mAdapter.clear();
            }
            try {
                REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + java.net.URLEncoder.encode(strValue, "UTF-8") + "&maxResults=10";
                onLoad();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void onLoad() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (isConnected) {
            loadingIndicator.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            if(isRunning == false){
                isRunning = true;
                loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            } else{
                loaderManager.restartLoader(BOOK_LOADER_ID, null, this);
            }
        } else {
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        books_list = books;
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else if (simpleEditText != null) {
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Saving custom values into the bundle
        outState.putString("strValue", strValue);
        outState.putParcelableArrayList(LIST_STATE, (ArrayList<Book>) books_list);
        outState.putInt(BOOKLIST_SCROLL_POSITION, bookListView.getFirstVisiblePosition());
        // Calling the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        // Restoring state members from saved instance
        strValue = savedInstanceState.getString("strValue");
        books_list = savedInstanceState.getParcelableArrayList(LIST_STATE);
        int position = savedInstanceState.getInt(BOOKLIST_SCROLL_POSITION);
        if (books_list != null && !books_list.isEmpty()) {
            mAdapter.addAll(books_list);
            bookListView.setSelection(position);
        } else if (simpleEditText != null) {
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }
}

