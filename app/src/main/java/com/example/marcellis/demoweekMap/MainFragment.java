package com.example.marcellis.demoweekMap;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,OnMapReadyCallback {



    // Identifies a particular Loader being used in this component
    private static final int URL_LOADER = 0;

    public static final String REMINDER = "reminder";
    private GoogleMap mMap;
    private EditText editText;

    private RecyclerView reminderView;
    private ReminderAdapter adapter;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_main, container, false);



        editText = (EditText) view.findViewById(R.id.editText);

        reminderView = (RecyclerView) view.findViewById(R.id.idRecyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        reminderView.setLayoutManager(mLayoutManager);


        //adapter = new ReminderAdapter(cursor, MainFragment.this);

        getAllReminders();

        SupportMapFragment mapFragment;
        mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
      //  mapFragment.getMapAsync(MainFragment.this);



        // LinearLayoutManager mLayoutManager
        //               = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();
                values.put(RemindersContentprovider.REMINDER_NAME, editText.getText().toString());
                getActivity().getContentResolver().insert(RemindersContentprovider.CONTENT_URI, values);

                getAllReminders();
            }
        });

        return view;
    }




    public void getAllReminders() {


        //Cursor =  getActivity().getContentResolver().query(RemindersContentprovider.CONTENT_URI, new String [] {RemindersContentprovider._ID, RemindersContentprovider.REMINDER_NAME}, null, null, null )

        getLoaderManager().initLoader(URL_LOADER, null,this );

    }


    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle)
    {

        String[] projection = {RemindersContentprovider._ID, RemindersContentprovider.REMINDER_NAME};
    /*
     * Takes action based on the ID of the Loader that's being created
     */
        switch (loaderID) {
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),   // Parent activity context
                        RemindersContentprovider.CONTENT_URI,        // Table to query
                        projection,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            adapter = new ReminderAdapter(data, this);
            reminderView.setAdapter(adapter);
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
