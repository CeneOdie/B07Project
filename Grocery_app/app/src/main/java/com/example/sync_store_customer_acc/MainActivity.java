package com.example.sync_store_customer_acc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView storeListText;
    /*
    HashMap item;
    HashMap mHashmap;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    */

    //refer condition in database to TextView storeListCondition
    DatabaseReference storeRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference storeConditionRef = storeRootRef.child("stores");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storeListText = (TextView)findViewById(R.id.storeList);

        // Write a message(HashmapMap) to the database
        /*
        HashMap mHashmap = new HashMap<>();
        mHashmap.put("Name 1", "Tim Hortons");
        mHashmap.put("Name 2", "Starbucks");
        mHashmap.put("Name 3", "McDonald's");
        mHashmap.put("Name 4", "KFC");
        storeConditionRef.updateChildren(mHashmap);
         */
    }

    @Override
    protected void onStart() {
        super.onStart();

        storeConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String text = snapshot.getValue(String.class);
                String default_text = getString(R.string.store_list);
                if (text == null){
                    storeListText.setText(default_text);
                }else{
                    /*
                    for(int i=0;i<mHashmap.size();i++){
                        item = new HashMap<String,String>();
                        item.put( "line1", mHashmap[i][0]);
                        item.put( "line2", mHashmap[i][1]);
                        item.put( "line3", mHashmap[i][2]);
                        list.add( item );
                    */


                    storeListText.setText(text);
                    storeListText.setTextSize(24);
                    storeListText.setTextColor(Color.BLACK);

                    /*
                    item = new HashMap<String,String>();
                    item.put(

                     */


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

}

