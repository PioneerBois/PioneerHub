package com.pandinu.PioneerHub.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandinu.PioneerHub.Model.Resource;
import com.pandinu.PioneerHub.R;
import com.pandinu.PioneerHub.ResourcesAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    public static final String TAG = "LibraryFragment";
    private RecyclerView rvResources;
    private ResourcesAdapter adapter;
    private List<Resource> allResources;

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvResources = view.findViewById(R.id.rvResources);

        allResources = new ArrayList<>();
        adapter = new ResourcesAdapter(getContext(), allResources);
        rvResources.setAdapter(adapter);
        rvResources.setLayoutManager(new LinearLayoutManager(getContext()));
        //rvResources.setLayoutManager(new GridLayoutManager(getContext(),2));

        queryResources();
    }

    // Getting data resource
    private void queryResources() {
        ParseQuery<Resource> query = ParseQuery.getQuery(Resource.class);
        query.findInBackground(new FindCallback<Resource>() {
            @Override
            public void done(List<Resource> resources, ParseException e) {
                // If there is an error
                if (e != null) {    // Recall: if exception is null, we gucci
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                // Debugging
                for (Resource resource : resources) {
                    Log.i(TAG, "Resource: " + resource.getName() + ", url: " + resource.getWebsiteUrl() + "image: " + resource.getThumbnail());
                }
                // All resource to array
                allResources.addAll(resources);
                adapter.notifyDataSetChanged();
            }
        });
    }
}