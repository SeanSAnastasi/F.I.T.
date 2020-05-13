package com.anastasi.fit;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public interface IMainActivity {

    public void inflateFragment(Fragment fragment,String tag, Boolean addToBackStack, ArrayList data);

}
