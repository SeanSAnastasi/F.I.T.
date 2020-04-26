package com.example.fit.ui.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.fit.CreateIngredientActivity;
import com.example.fit.CreateRecipeActivity;
import com.example.fit.InitialActivity;
import com.example.fit.LoginActivity;
import com.example.fit.MyIngredientsActivity;
import com.example.fit.MyRecipesActivity;
import com.example.fit.R;
import com.example.fit.SignUpActivity;

import java.util.ArrayList;

public class OtherFragment extends Fragment {

    private OtherViewModel otherViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        otherViewModel = ViewModelProviders.of(this).get(OtherViewModel.class);
        View root = inflater.inflate(R.layout.fragment_other, container, false);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.fit", Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username","");
        String password = sharedPreferences.getString("password","");
        ArrayList<Button> buttons = new ArrayList<Button>();
        LinearLayout otherMenuLayout = (LinearLayout) root.findViewById(R.id.otherMenuLayout);
        Log.i("username",username);
        Log.i("pass",password);
        //Dynamically adds login / logout buttons depending on whether the user is logged in or not
        if(username =="" && password ==""){
            //not logged in
            Button loginButton = new Button(getActivity());
            Button signupButton = new Button(getActivity());

            loginButton.setText("Login");
            loginButton.setTag("login");
            loginButton.setBackgroundTintList(getResources().getColorStateList(R.color.bgColor));

            signupButton.setText("Sign Up");
            signupButton.setTag("signUp");
            signupButton.setBackgroundTintList(getResources().getColorStateList(R.color.bgColor));

            otherMenuLayout.addView(loginButton);
            otherMenuLayout.addView(signupButton);
            buttons.add(loginButton);
            buttons.add(signupButton);
        }else{
            //logged in
            Button logoutButton = new Button(getActivity());

            logoutButton.setText("Logout");
            logoutButton.setTag("logout");
            logoutButton.setBackgroundTintList(getResources().getColorStateList(R.color.bgColor));

            otherMenuLayout.addView(logoutButton);
            buttons.add(logoutButton);
        }
        buttons.add((Button) root.findViewById(R.id.createRecipe));
        buttons.add((Button) root.findViewById(R.id.createIngredient));
        buttons.add((Button) root.findViewById(R.id.myRecipes));
        buttons.add((Button) root.findViewById(R.id.myIngredients));

        for(int i = 0; i<buttons.size(); i++){
            //change button background tint for if developer forgets to add it or if they are different
            buttons.get(i).setBackgroundTintList(getResources().getColorStateList(R.color.bgColor));
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    switch(v.getTag().toString()){
                    case ("createRecipe"):
                        goToPage(CreateRecipeActivity.class);
                        break;
                    case ("createIngredient"):
                        goToPage(CreateIngredientActivity.class);
                        break;
                    case ("myRecipes"):
                        goToPage(MyRecipesActivity.class);
                        break;
                    case ("myIngredients"):
                        goToPage(MyIngredientsActivity.class);
                        break;
                    case ("login"):
                        goToPage(LoginActivity.class);
                        break;
                    case ("signUp"):
                        goToPage(SignUpActivity.class);
                        break;
                    case ("logout"):
                        sharedPreferences.edit().putString("username","").apply();
                        sharedPreferences.edit().putString("password","").apply();
                        goToPage(InitialActivity.class);
                        break;

                    default:

                        break;
                }

                }
            });
        }


        return root;
    }



    public void goToPage(Class whereToGo){
        //intent handler
        Intent intent = new Intent(getActivity(), whereToGo);
        startActivity(intent);
    }
}
