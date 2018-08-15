package com.petsadoption.tal.petsadoption.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.petsadoption.tal.petsadoption.Adapter.NewPetsAdapter;
import com.petsadoption.tal.petsadoption.Objects.Pet;
import com.petsadoption.tal.petsadoption.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tal on 10/5/2016.
 */
public class AreaSearchFragment extends Fragment {

    private String title;
    ArrayList<Pet> petsname = new ArrayList<>();
    private int page;
    private List<Pet> petAreaList;
    private ArrayList<String> mKeys = new ArrayList<>();
    NewPetsAdapter adapter;
    RecyclerView recyclerView;
    Query query;
    private FirebaseDatabase database;
    AutoCompleteTextView etSearchPetCity;
    DatabaseReference databaseReference;
    Button btnSearchPetCity;
    TextView tvCity;
    int j = 0;
    String tvCityTv;



    public static AreaSearchFragment fragment(int page, String title){
        AreaSearchFragment areaSearchFragment = new AreaSearchFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        areaSearchFragment.setArguments(args);
        return areaSearchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someint",0);
        title = getArguments().getString("someTitle");
        database = FirebaseDatabase.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.second_fragment,container,false);

        petAreaList = new ArrayList<>();

        final String[] autoPetList = getResources().getStringArray(R.array.city);


        recyclerView = (RecyclerView)view.findViewById(R.id.searchcitytable);
        btnSearchPetCity = (Button)view.findViewById(R.id.citySearch);
        tvCity = (TextView)view.findViewById(R.id.tvCity);
        tvCity.setVisibility(View.GONE);



        btnSearchPetCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("בחר עיר");
                final String[] petCity = getResources().getStringArray(R.array.city);
                builder.setItems(petCity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        tvCity.setText(petCity[i++]);
                        tvCityTv = tvCity.getText().toString();

                        for (j = 0; j<= autoPetList.length ; j++){

                            if (tvCityTv.equals(autoPetList[j])){
                                databaseReference = database.getReference(autoPetList[j]);
                                updateList();
                                petAreaList.clear();
                                break;
                            }else{
                                petAreaList.clear();

                            }
                        }


                    }
                });

                builder.create();
                builder.show();

            }

        });





        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new NewPetsAdapter(getActivity(), petAreaList);
        recyclerView.setAdapter(adapter);



        return view;

    }



    private void updateList(){
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Pet pet = dataSnapshot.getValue(Pet.class);
                petAreaList.add(pet);

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Pet model = dataSnapshot.getValue(Pet.class);
                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);

                petAreaList.set(index,model);

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
