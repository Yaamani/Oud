package com.example.oud.artist.fragments.albums;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;


public class ReleaseDateFragment extends Fragment {
    ConnectionStatusListener connectionStatusListener;
    Bundle albumData;
    Button nextButton;
    DatePicker datePicker;

    public ReleaseDateFragment(Bundle albumData, ConnectionStatusListener connectionStatusListener) {
        this.albumData = albumData;
        this.connectionStatusListener = connectionStatusListener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_release_date, container, false);
        nextButton = v.findViewById(R.id.btn_release_date_next);
        datePicker = v.findViewById(R.id.date_picker_release_date);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();

                String monthString = month+"";
                String dayString = day+"";
                if(month < 10){
                    monthString = "0" + month;
                }
                if(day < 10){
                    dayString  = "0" + day ;
                }
                String date = year+"-"+monthString+"-"+dayString;
                albumData.putString(Constants.BUNDLE_CREATE_ALBUM_RELEASE_DATE_ID,date);
                ChooseGenresFragment fragment = new ChooseGenresFragment(getActivity(),albumData,connectionStatusListener);
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right)
                        .add(R.id.nav_host_fragment_artist,fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        return v;
    }
}
