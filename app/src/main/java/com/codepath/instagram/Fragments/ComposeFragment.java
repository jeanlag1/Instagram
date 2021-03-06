package com.codepath.instagram.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.instagram.LoginActivity;
import com.codepath.instagram.Post;
import com.codepath.instagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {


    private static final String TAG = "MainActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 43 ;
    private Button mTakePicture;
    private Button mSubmit;
    private EditText mDescription;
    private ImageView mPicture;
    public String mPhotoFileName = "photo.jpg";
    private File mPhotoFile;


    public ComposeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        mTakePicture = view.findViewById(R.id.btnPicture);
        mSubmit = view.findViewById(R.id.btnSubmit);
        mDescription = view.findViewById(R.id.etDescription);
        mPicture = view.findViewById(R.id.ivPostImg);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access

        mPhotoFile = getPhotoFileUri(mPhotoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", mPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void submitPost() {
        String desc = mDescription.getText().toString();
        if (desc.isEmpty()) {
            Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (mPhotoFile == null || mPicture.getDrawable() == null) {
            Toast.makeText(getContext(), "There is no image", Toast.LENGTH_LONG).show();
            return;
        }
        ParseUser currentUser = ParseUser.getCurrentUser();
        savePost(desc, currentUser, mPhotoFile);
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String mPhotoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + mPhotoFileName);
    }
    private void savePost(String desc, ParseUser currentUser, File mPhotoFile) {
        Post post = new Post();
        post.setDescription(desc);
        post.setImage(new ParseFile(mPhotoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override

            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving!", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "Post saved successfully");
                mDescription.setText("");
                mPicture.setImageResource(0);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                mPicture.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}