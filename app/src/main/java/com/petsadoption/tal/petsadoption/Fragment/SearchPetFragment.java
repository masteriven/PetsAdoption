package com.petsadoption.tal.petsadoption.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.petsadoption.tal.petsadoption.Adapter.NewPetsAdapter;
import com.petsadoption.tal.petsadoption.Objects.Pet;
import com.petsadoption.tal.petsadoption.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tal on 10/5/2016.
 */
public class SearchPetFragment extends android.support.v4.app.Fragment{
    private String title;
    private int page;
    private List<Pet> petList;
    NewPetsAdapter adapter;
    private ArrayList<String> mKeys = new ArrayList<>();
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private AutoCompleteTextView etSearchPet;
    private Button btnSearchPet;
    private FirebaseDatabase database;
    private TextView tvPet;
    private int j = 0;
    String stringPet;



    public static SearchPetFragment fragment(int page, String title){
        SearchPetFragment fragmentFirst = new SearchPetFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt",0);
        title = getArguments().getString("someTitle");
         database = FirebaseDatabase.getInstance();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final String[] petListAlert = getResources().getStringArray(R.array.petArray);

        View view = inflater.inflate(R.layout.fragment_first,container,false);

        petList = new ArrayList<>();


        recyclerView = (RecyclerView)view.findViewById(R.id.rvSearchPet);
        btnSearchPet = (Button)view.findViewById(R.id.btnSearchPet);
        tvPet = (TextView)view.findViewById(R.id.tvPet);
        tvPet.setVisibility(View.GONE);
        final String[] choosePetlist = getResources().getStringArray(R.array.petArray);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("בחר בעל חיים");

        btnSearchPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.setItems(choosePetlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvPet.setText(choosePetlist[i]);
                        stringPet = tvPet.getText().toString();

                            if (stringPet.equals(choosePetlist[i])) {
                                databaseReference = database.getReference(petListAlert[i]);
                                updateList();
                                petList.clear();

                            }else {
                                petList.clear();
                            }

                    }
                });

                alertDialog.create();
                alertDialog.show();

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapter = new NewPetsAdapter(getActivity(),petList);
        recyclerView.setAdapter(adapter);



        return view;

    }


    private void updateList(){

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Pet pet = dataSnapshot.getValue(Pet.class);
                petList.add(pet);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Pet model = dataSnapshot.getValue(Pet.class);
                String key = dataSnapshot.getKey();

                int index = mKeys.indexOf(key);

                petList.set(index,model);

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
