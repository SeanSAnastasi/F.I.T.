package com.anastasi.fit;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public interface IMainActivity {


    //This is the interface that the main activity will implement. Currently its only purpose is to inflate all of the necessary fragments used by the main activity.
    public void inflateFragment(Fragment fragment,String tag, Boolean addToBackStack, ArrayList data);

}
