package com.anastasi.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyIngredientsActivity extends AppCompatActivity {
    Bitmap bmp;
    String description;
    String titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // this is used to show all of the ingredients created by a specific username or by when the username is blank(therefore meaning that the recipe was created without being logged in and is local to the phone)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        LayoutInflater vi = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(MainActivity.class);
            }
        });

        LinearLayout linearLayout = findViewById(R.id.recipesLinearLayout);

        try {
            SQLiteDatabase db = this.openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM ingredients WHERE username = '' OR username = '"+username+"'",null);
            int titleIndex = c.getColumnIndex("title");
            int descriptionIndex = c.getColumnIndex("description");
            int imgIndex = c.getColumnIndex("image");
            int idIndex = c.getColumnIndex("id");
            c.moveToFirst();
            while(c!=null){
                //get image
                byte[] imageByte = c.getBlob(imgIndex);
                bmp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

                //create card
                View card = vi.inflate(R.layout.card, null);
                //set image
                ImageView imageView = card.findViewById(R.id.card_image);
                imageView.setImageBitmap(bmp);
                //set title
                titleText = c.getString(titleIndex);
                TextView title = card.findViewById(R.id.card_title);

                title.setText(titleText);
                //set description
                TextView desc = card.findViewById(R.id.card_description);
                description = c.getString(descriptionIndex);
                desc.setText(description);


                setOnClick(card, c.getInt(idIndex));

                linearLayout.addView(card, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));




                c.moveToNext();
            }
        }catch (SQLiteException e){

            TextView titleTextView = new TextView(this);
            titleTextView.setText("Sorry there are no recipes to show");
            linearLayout.addView(titleTextView);
        }
        catch (Exception e){
            Log.e("EXCEPTION: ", e.toString());
        }


    }
    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }

    //The on click of this card should use an intent to go to the main activity and set extras to notify the main activity that it should inflate the details fragment with the data given
    public void setOnClick(View card, final int id){
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToPageWithFunction(MainActivity.class,  titleText, bmp, description, id);



            }
        });
    }

    public void goToPageWithFunction(Class whereToGo,  String title, Bitmap img, String details, int id){

        Intent intent = new Intent(this, whereToGo);
        intent.putExtra("fragment", "ingredient");
//        intent.putExtra("title", title);
//        intent.putExtra("img", bmp);
//        intent.putExtra("details", details);
        intent.putExtra("id", id);
        startActivity(intent);

    }

}

