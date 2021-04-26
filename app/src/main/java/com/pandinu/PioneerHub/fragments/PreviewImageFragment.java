package com.pandinu.PioneerHub.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.pandinu.PioneerHub.R;

import java.io.File;

public class PreviewImageFragment extends DialogFragment {

    private static final String TAG = "PreviewImageFragment";
    private ImageView ivPreviewImage;
    private File photoFile;
    private String photoFileName;
    private Toolbar toolbar;
    private static final String ARG_FILENAME = "";
    private static final String ARG_FILE = "";
    private static final String ARG_BUNDLE = "";
    private Button cancelButton;

    private TextView tvPhotoFileName;


    public PreviewImageFragment(){
    }

    public static PreviewImageFragment newInstance(Bundle bundle) {

        Bundle args = new Bundle();

        PreviewImageFragment fragment = new PreviewImageFragment();
        args.putBundle(ARG_BUNDLE, bundle);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preview_image_dialog, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        if(getArguments() != null){


            Bundle bundle = getArguments().getBundle(ARG_BUNDLE);
            photoFileName = bundle.getString("photoFileName");
           // toolbar.setTitle(photoFileName);
            photoFile = (File) bundle.getSerializable("photoFile");

        }

        tvPhotoFileName = v.findViewById(R.id.tvPhotoFileName);

        Log.i(TAG,photoFileName);



      //  ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

      //  ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


  //      Menu menu = toolbar.getMenu();
//        menu.getItem(R.id.action_send).setVisible(false);



       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });*/


        //setHasOptionsMenu(false);

        cancelButton = v.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        tvPhotoFileName.setText(photoFileName);

        Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        ivPreviewImage = v.findViewById(R.id.ivPreviewImage);
        ivPreviewImage.setImageBitmap(takenImage);




        getDialog().setCanceledOnTouchOutside(true);

       // getActivity().invalidateOptionsMenu();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
