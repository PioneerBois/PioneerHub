package com.pandinu.PioneerHub.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pandinu.PioneerHub.Post;
import com.pandinu.PioneerHub.Profile;
import com.pandinu.PioneerHub.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment  {
    private static final String TAG = "PostFragment";

    /*private static final String[] CHANNELS = new String[]{
      "Student Feed", "Buy & Sell", "Lost & Found", "Housing", "News", "Ride Sharing"
    };*/

    private PostFragmentListener listener;

    public interface PostFragmentListener{
        void cancelPost(String channel);
        void successfulPost(String channel);
    }


    private Button btnComposePost;
    private EditText etPostDescription;

    private Button btnTakePhoto;
    private Button btnChoosePhoto;
    private static final String ARG_CHANNEL = "";
    private String currentChannel = "";

    private ImageView ivProfileImage;
    private TextView tvFirstName;
    private TextView tvLastName;
    public static final int MAX_TWEET_LENGTH = 280;
    private TextView tvPostCounter;
    private Toolbar toolbar;

    private RelativeLayout rvImageLayout;
    private Button btnCancelImage;
    private TextView tvImageName;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public final static int PICK_PHOTO_CODE = 1046;
    public static final String letters = "ABCDEFGEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxysz1234567890";


    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static PostFragment newInstance(String channel) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putString(ARG_CHANNEL, channel);
        Log.i(TAG, "make new instance" + channel);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        if(getArguments() != null){
            currentChannel = getArguments().getString(ARG_CHANNEL);
            //Log.i(TAG, "getargments is not null " + currentChannel);
        }

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setTitle("Posting to " + currentChannel);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPost();
                listener.cancelPost(currentChannel);
            }
        });


        //getParentFragment().getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
        //getParentFragment().getActivity().getWindow().setBackgroundDrawableResource();


        Log.i(TAG, currentChannel);

        ivProfileImage = v.findViewById(R.id.ivProfileImage);
        tvFirstName = v.findViewById(R.id.tvFirstName);
        tvLastName = v.findViewById(R.id.tvLastName);

        queryProfile();


        etPostDescription = v.findViewById(R.id.etPostDescription);
        tvPostCounter = v.findViewById(R.id.tvPostCounter);
        etPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right as the text is being changed (even supplies the range of text)

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right before text is changing

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Fires right after the text has changed

                int tweetCharLeft = MAX_TWEET_LENGTH - editable.toString().length();

                tvPostCounter.setText("" + tweetCharLeft);
                if(tweetCharLeft <= 20){
                    //btnComposePost.setEnabled(false);
                    tvPostCounter.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));

                    //tvCharLimit.setTextColor(Color.parseColor("#FF0000"));
                }else{
                    tvPostCounter.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryVariant));
                    //btnComposePost.setEnabled(true);
                    //tvCharLimit.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        rvImageLayout = v.findViewById(R.id.rvImageLayout);
        btnCancelImage = v.findViewById(R.id.btnCancelImage);
        tvImageName = v.findViewById(R.id.tvImageName);

        btnChoosePhoto = v.findViewById(R.id.btnChoosePhoto);
        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rlImage.setVisibility(View.VISIBLE);
                //rvImageLayout.setVisibility(View.VISIBLE);
                onPickPhoto();
            }
        });


        btnTakePhoto = v.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rlImage.setVisibility(View.VISIBLE);
                //rvImageLayout.setVisibility(View.VISIBLE);
                launchCamera();
            }
        });


        btnComposePost = v.findViewById(R.id.btnComposePost);
        btnComposePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = etPostDescription.getText().toString().trim();
                ParseUser currentUser = ParseUser.getCurrentUser();
                //File photoFile = null;


                savePost(description, currentUser, photoFile);

            }
        });

        if(savedInstanceState != null){
            Bundle bundle = savedInstanceState.getBundle("bundle");
            if(bundle != null) {
                photoFileName = bundle.getString("photoFileName");
                photoFile = (File) bundle.getSerializable("photoFile");
                capturedImage();
            }
        }else{
            clearPost();
        }

        //clearPost();


        return v;
    }


    //Clear the post when the user has cancel the post

    private void clearPost() {
        etPostDescription.setText("");
        photoFile = null;
        photoFileName = "photo.jpg";
        btnCancelImage.setEnabled(false);
        rvImageLayout.setVisibility(View.GONE);
    }


    //Save the post if the user
        //Checks if the desciption is empty return
        //Set the description, current user id, image if any, and currentChannel they are posting too and save in background


    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();

        if(description.isEmpty()){
            Log.i(TAG, "description is empty");
            return;
        }

        post.setDescription(description);
        post.setUserId(currentUser);
        if(!(photoFile == null)){
            post.setImg(new ParseFile(photoFile));
        }
        post.setChannel(currentChannel);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error while saving", e);
                    return;
                }

                Log.i(TAG, "Post save was successful!");

                //queryForNewPost();

                listener.successfulPost(currentChannel);

            }
        });

    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFileName = generateNewFileName();
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.pandinu.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    //Generate new file name for image that was capture
    private String generateNewFileName() {

        String fileName = "";
        Random rand = new Random();
        int length = letters.length();
        int index;
        char ch;
        for(int i = 0; i<10; i++){
            index = rand.nextInt(length);
            ch = letters.charAt(index);
            fileName += ch;
        }

        fileName = fileName.concat(".jpg");

        return fileName;
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                //We got the picture and want to update the post
                capturedImage();

            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }else{
            if ((data != null) && requestCode == PICK_PHOTO_CODE) {
                Uri photoUri = data.getData();

                rvImageLayout.setVisibility(View.VISIBLE);
                tvImageName.setText(photoUri.toString());

                btnCancelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoFile = null;
                        btnCancelImage.setEnabled(false);
                        rvImageLayout.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


    //Show the cancel button and photoFileName
    //clicking on the file name pops up a dialog fragment to preview the picture
    private void capturedImage() {
        rvImageLayout.setVisibility(View.VISIBLE);
        tvImageName.setText(photoFileName);


        btnCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFile = null;
                btnCancelImage.setEnabled(false);
                rvImageLayout.setVisibility(View.GONE);
            }
        });

        tvImageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putSerializable("photoFile", photoFile);
                bundle.putString("photoFileName", photoFileName);
                PreviewImageFragment previewDialogFragment = PreviewImageFragment.newInstance(bundle);
                previewDialogFragment.show(fm, "preview_image_dialog");
            }
        });
    }


    //Have to take this out later!!! Nerwer devices do not have a dedicated gallery app might hceck google photso instead
    // Trigger gallery selection for a photo
    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }else{
            Log.i(TAG, "no gallery");
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }








    private void queryProfile() {
        ParseQuery<Profile> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Profile>() {
            @Override
            public void done(List<Profile> objects, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with querying Profile");
                    return;
                }else{
                    //Log.i(TAG, objects.get(0).getFirstName());
                    tvFirstName.setText(objects.get(0).getFirstName());
                    tvLastName.setText(objects.get(0).getLastName());
                    Glide.with(getActivity()).load(objects.get(0).getProfileImg().getUrl()).into(ivProfileImage);
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(getParentFragment() instanceof PostFragmentListener){
            listener = (PostFragmentListener) getParentFragment();
            Log.i(TAG, listener.toString());
        }else{
            throw new RuntimeException("getParentFregment()" + " " +
                    "must implement PostFragmentListener");
        }

        //getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        //getActivity().findViewById(R.id.bottom_navigation).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


        if(photoFile != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("photoFile", photoFile);
            bundle.putString("photoFileName", photoFileName);
            outState.putBundle("bundle", bundle);
        }
    }
}