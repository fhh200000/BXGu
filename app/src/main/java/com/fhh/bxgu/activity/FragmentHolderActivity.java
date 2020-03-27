package com.fhh.bxgu.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fhh.bxgu.R;
import com.fhh.bxgu.fragment.ExerciseDetailFragment;
import com.fhh.bxgu.shared.StaticVariablePlacer;

public class FragmentHolderActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(StaticVariablePlacer.theme);
        String fragmentName = getIntent().getStringExtra("fragment");
//        Fragment fragment=null;
////        assert fragmentName != null;
////        switch (fragmentName) {
////            case "ExerciseDetailFragment":{
////                fragment = new ExerciseDetailFragment();
////                break;
////            }
////        }
//        assert fragment != null;
        Log.e("FFFFFFF",getIntent().getIntExtra("chapter",0)+"");
        Fragment fragment = new ExerciseDetailFragment(getIntent().getIntExtra("chapter",0),getIntent().getStringExtra("title"));
        setContentView(R.layout.activity_single_fragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.generic_fragment_container,fragment)
                .commit();
    }
}
