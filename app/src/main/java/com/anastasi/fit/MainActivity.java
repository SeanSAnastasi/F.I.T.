package com.anastasi.fit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final BottomNavigationView navView = findViewById(R.id.nav_view);
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

    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }

    private void doFragmentTransaction(Fragment fragment, String tag, boolean addToBackStack, ArrayList data){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_container, fragment, tag);

        if(addToBackStack){
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }

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
