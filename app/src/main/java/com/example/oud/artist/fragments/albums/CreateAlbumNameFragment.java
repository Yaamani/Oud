package com.example.oud.artist.fragments.albums;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.oud.Constants;
import com.example.oud.R;


public class CreateAlbumNameFragment extends Fragment {
    Button nextButton;
    EditText albumNameEditText;
    Bundle albumData;

    public CreateAlbumNameFragment(Bundle data) {
        this.albumData= data;
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
                //getParentFragmentManager().beginTransaction().replace().commit();
            }
        });

        return v;
    }
}
