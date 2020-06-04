package com.anastasi.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anastasi.fit.ui.home.HomeFragment;
import com.anastasi.fit.ui.ingredients.IngredientsFragment;
import com.anastasi.fit.ui.recipes.RecipesFragment;
import com.anastasi.fit.ui.workouts.WorkoutsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainActivity {
    DrawerLayout drawerLayout;
     BottomNavigationView navView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
         navView = findViewById(R.id.nav_view);
        // set home to initial fragment

        Fragment fragment = new HomeFragment();
        doFragmentTransaction(fragment, getString(R.string.fragment_home_tag), true, null);

        //set navigation on selection listener

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try{
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            Fragment fragment_home = new HomeFragment();
                            doFragmentTransaction(fragment_home, getString(R.string.fragment_home_tag), false, null);

                            break;
                        case R.id.navigation_ingredients:
                            Fragment fragment_ingredients = new IngredientsFragment();
                            doFragmentTransaction(fragment_ingredients, getString(R.string.fragment_ingredients_tag), false, null);
                            break;
                        case R.id.navigation_recipes:
                            Fragment fragment_recipes = new RecipesFragment();
                            doFragmentTransaction(fragment_recipes, getString(R.string.fragment_recipes_tag), false, null);
                            break;
                        case R.id.navigation_workouts:
                            Fragment fragment_workouts = new WorkoutsFragment();
                            doFragmentTransaction(fragment_workouts, getString(R.string.fragment_workouts_tag), false, null);
                            break;

                    }
                }catch (Exception e){
                    Log.e("Exception", e.toString());
                }

                return true;
            }
        });
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_recipes, R.id.navigation_ingredients)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

       // NavigationUI.setupWithNavController(navView, navController);

        //shared prefs
        final SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);

        //drawer setup
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, topAppBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.create_recipe:
                        goToPage(CreateRecipeActivity.class);
                        break;
                    case R.id.create_ingredient:
                        goToPage(CreateIngredientActivity.class);
                        break;
                    case R.id.my_recipes:
                        goToPage(MyRecipesActivity.class);
                        break;
                    case R.id.my_ingredients:
                        goToPage(MyIngredientsActivity.class);
                        break;
                    case R.id.login:
                        goToPage(LoginActivity.class);
                        break;
                    case R.id.signup:
                        goToPage(SignUpActivity.class);
                        break;
                    case R.id.logout:
                        sharedPreferences.edit().putString("username","").apply();
                        sharedPreferences.edit().putString("password","").apply();
                        goToPage(InitialActivity.class);
                        break;
                    default:

                        break;
                }
                return true;
            }
            });

        //logic for hiding and showing drawer elements
        String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        TextView welcomeText = (TextView) header.findViewById(R.id.welcome_text);
        if(username =="" && password ==""){
            //not logged in

            welcomeText.setText("");
            menu.findItem(R.id.logout).setVisible(false);
        }else{
            //logged in

            welcomeText.setText("Hello "+username+"!");
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.signup).setVisible(false);
        }

        checkRedirect();

    }

    //This function is used to check whether the intent came from the My recipes or My ingredients path. therefore requiring to go to the respective details function
    private void checkRedirect() {

        if(getIntent().hasExtra("fragment")){
            String fragment = getIntent().getStringExtra("fragment");
//            String title = getIntent().getStringExtra("title");
//            String details = getIntent().getStringExtra("details");
            int id = getIntent().getIntExtra("id", -1);
//            Bitmap img = getIntent().getParcelableExtra("img");
            if(fragment.equals("recipe")){
                RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();
                SQLiteDatabase db = this.openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
                Cursor c = db.rawQuery("SELECT * FROM recipes WHERE id="+id,null);
                int titleIndex = c.getColumnIndex("title");
                int cookingMethod = c.getColumnIndex("cooking_method");
                int imgIndex = c.getColumnIndex("image");
                c.moveToFirst();

                //get image bytes
                byte[] imageByte = c.getBlob(imgIndex);
                //converts to bitmap
                Bitmap this_bmp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

                String this_titleText = c.getString(titleIndex);
                String this_cookingMethod = c.getString(cookingMethod);
                detailsFragment.setTitle(this_titleText);
                detailsFragment.setImg(this_bmp);
                detailsFragment.setCookingMethod(this_cookingMethod);
                detailsFragment.setId(id);
                navView.setSelectedItemId(R.id.navigation_recipes);
                this.inflateFragment(detailsFragment, getString(R.string.fragment_recipe_details_tag), true, null);

            }else if(fragment.equals("ingredient")){
                SQLiteDatabase db = this.openOrCreateDatabase("FIT", Context.MODE_PRIVATE, null);
                Cursor c = db.rawQuery("SELECT * FROM ingredients WHERE id="+id,null);
                int titleIndex = c.getColumnIndex("title");
                int descIndex = c.getColumnIndex("description");
                int imgIndex = c.getColumnIndex("image");
                c.moveToFirst();

                //get image bytes
                byte[] imageByte = c.getBlob(imgIndex);
                //converts to bitmap
                Bitmap this_bmp = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

                String this_titleText = c.getString(titleIndex);
                String this_description = c.getString(descIndex);

                IngredientDetailsFragment detailsFragment = new IngredientDetailsFragment();
                detailsFragment.setTitle(this_titleText);
                detailsFragment.setImg(this_bmp);
                detailsFragment.setDescription(this_description);
                detailsFragment.setId(id);
                navView.setSelectedItemId(R.id.navigation_ingredients);
                this.inflateFragment(detailsFragment, getString(R.string.fragment_recipe_details_tag), true, null);
            }else{
                return;
            }


        }



    }

    // checks whether the drawer was open when the back button was pressed therefore requiring the drawer to be closed
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }
    // This function is being used to display a fragment to the designated area
    private void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, ArrayList data){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_container, fragment, tag);

        if(addToBackStack){
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }
    //Intent action
    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(this, whereToGo);
        startActivity(intent);
    }


    @Override
    public void inflateFragment(Fragment fragment, String tag, Boolean addToBackStack, ArrayList data) {
        doFragmentTransaction(fragment, tag, addToBackStack, data);
    }
}
