package com.example.oud.artist.fragments.albums;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;


public class CreateAlbumNameFragment extends Fragment {
    Button nextButton;
    EditText albumNameEditText;
    Bundle albumData;
    ConnectionStatusListener connectionStatusListener;

    public CreateAlbumNameFragment(Bundle data,ConnectionStatusListener connectionStatusListener) {
        this.albumData= data;
        this.connectionStatusListener= connectionStatusListener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_album_name, container, false);
        nextButton = v.findViewById(R.id.btn_album_name_next);
        albumNameEditText = v.findViewById(R.id.text_Create_album_name);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(albumNameEditText.getText().toString().length()==0){
                    albumNameEditText.setError("album name can't be empty");
                    return;
                }
                albumData.putString(Constants.BUNDLE_CREATE_ALBUM_ALBUM_NAME,albumNameEditText.getText().toString());
                AlbumTypeFragment fragment = new AlbumTypeFragment(albumData,connectionStatusListener);
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
