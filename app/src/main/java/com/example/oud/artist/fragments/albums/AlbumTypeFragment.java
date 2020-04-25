package com.example.oud.artist.fragments.albums;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.google.android.exoplayer2.C;


public class AlbumTypeFragment extends Fragment {
    Bundle albumData;
    ConnectionStatusListener connectionStatusListener;

    Button nextButton;
    RadioGroup radioGroup;
    public AlbumTypeFragment(Bundle albumData,ConnectionStatusListener connectionStatusListener){
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
        View v=inflater.inflate(R.layout.fragment_album_type, container, false);
        radioGroup = v.findViewById(R.id.radio_group_album_type);
        nextButton = v.findViewById(R.id.btn_album_type_next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int  radioButtonId = radioGroup.getCheckedRadioButtonId();
                if(radioButtonId == -1){
                    Toast.makeText(getContext(),"please choose the album Type",Toast.LENGTH_LONG).show();
                    return;
                }

                if(radioButtonId == 1)
                        albumData.putString(Constants.BUNDLE_CREATE_ALBUM_TYPE_ID,"single");
                else if(radioButtonId == 2)
                        albumData.putString(Constants.BUNDLE_CREATE_ALBUM_TYPE_ID,"album");
                else
                    albumData.putString(Constants.BUNDLE_CREATE_ALBUM_TYPE_ID,"compilation");

                ReleaseDateFragment fragment = new ReleaseDateFragment(albumData,connectionStatusListener);
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
