package com.example.space.chatapp;

/*
  * Tutorials:
  * https://antonioleiva.com/recyclerview-listener/
  * http://www.devexchanges.info/2015/03/android-listview-dynamically-load-more.html
  * */


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Users extends AppCompatActivity {

    private List<Contact> contacts;
    private ContactAdapter contactAdapter;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contacts = new ArrayList<>();
        random = new Random();

        //set dummy data
        for (int i = 0; i < 10; i++) {
            Contact contact = new Contact();
            contact.setName("user " + i);
            contact.setLastMessage("Hi guys");
            contacts.add(contact);
        }


        // load data from firebase

        //find view by id and attaching adapter for the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(recyclerView, contacts, this);
        recyclerView.setAdapter(contactAdapter);

        //set load more listener for the RecyclerView adapter
        contactAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (contacts.size() <= 20) {
                    contacts.add(null);
                    contactAdapter.notifyItemInserted(contacts.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacts.remove(contacts.size() - 1);
                            contactAdapter.notifyItemRemoved(contacts.size());

                            //Generating more data
                            int index = contacts.size();
                            int end = index + 10;
                            for (int i = index; i < end; i++) {
                                Contact contact = new Contact();
                                contact.setName("user " + i);
                                contact.setLastMessage("Hi guys");
                                contacts.add(contact);
                            }

                            // load more data from firebase

                            contactAdapter.notifyDataSetChanged();
                            contactAdapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(Users.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}