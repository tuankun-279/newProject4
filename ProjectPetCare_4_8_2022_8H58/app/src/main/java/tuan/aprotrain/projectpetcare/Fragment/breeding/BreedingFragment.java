package tuan.aprotrain.projectpetcare.Fragment.breeding;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import tuan.aprotrain.projectpetcare.Adapter.PetAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Pet;

public class BreedingFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {



    private Context context;
    private PetAdapter petAdapter;
    private DatabaseReference reference;
    private ArrayList<Pet> petList;
    private View view;
    private Toolbar toolbar;

    private Spinner spinnerGender;
    private Spinner spinnerSpecies;
    private SearchView searchView;
    private RecyclerView recyclerView;

    LinearLayout ckAdditionalSearchCheckBox;
    LinearLayout ckAdditionalSearchSpinner;
    LinearLayout showoHideButtonFilter;

    private ArrayList<Pet> petListSelected = new ArrayList<>();
    private ArrayList<String> spinnersSpeciesList = new ArrayList<>();

    public static BreedingFragment newInstance() {
        return new BreedingFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
        getListPetsFromRealTimeDataBase();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_breeding, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_pet_id);
        TextView emptyTextView = (TextView) view.findViewById(android.R.id.empty);

        recyclerView.setAdapter(petAdapter);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    private void getListPetsFromRealTimeDataBase() {

        //Real time database

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = database.getReference("Pet");

        reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Pets").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(petSnapshot -> {
                    if (!petSnapshot.getValue(Pet.class).getUserId().equals(user.getUid())) {
                        Pet pet = petSnapshot.getValue(Pet.class);
                        petList.add(pet);
                    }
                });
                petAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_breeding_pet, menu);
        MenuItem searchItem = menu.findItem(R.id.search_id);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    public boolean onQueryTextChange(String searchString) {

        CheckBox colorFilter = view.findViewById(R.id.colorFilter);
        CheckBox breedFilter = view.findViewById(R.id.breedFilter);

        //check 2: color and breed
        if ((colorFilter.isChecked() == true && breedFilter.isChecked() == true)
                || (colorFilter.isChecked() == false && breedFilter.isChecked() == false)) {
            colorAbreedFilterList(searchString);
        }
        //check 1: color or breed
        if (colorFilter.isChecked() == true) {
            colorFilter(searchString);
        } else if (breedFilter.isChecked() == true) {
            breedFilter(searchString);
        }
        return true;
    }

    private void colorAbreedFilterList(String searchText) {
        List<Pet> filteredPet = new ArrayList<>();
        for (Pet item : petList) {

            if (item.getColor().toLowerCase().contains(searchText.toLowerCase())) {
                filteredPet.add(item);
            }
        }
        if (filteredPet.isEmpty()) {
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#D41717"));
            // Set BackgroundDrawable
            toolbar.setBackground(colorDrawable);
        } else {
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#0F9D58"));
            // Set BackgroundDrawable
            toolbar.setBackground(colorDrawable);

            petAdapter.setFilteredList(filteredPet);
        }
    }

    public void breedFilter(String searchText) {
        List<Pet> filteredPet = new ArrayList<>();
        for (Pet item : petList) {

            if (item.getKind().toLowerCase().contains(searchText.toLowerCase())) {
                filteredPet.add(item);
            }
        }
        if (filteredPet.isEmpty()) {
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#D41717"));
            // Set BackgroundDrawable
            toolbar.setBackground(colorDrawable);
        } else {
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#0F9D58"));
            // Set BackgroundDrawable
            toolbar.setBackground(colorDrawable);

            petAdapter.setFilteredList(filteredPet);
        }
    }

    // search by color
    public void colorFilter(String searchText) {

        List<Pet> filteredPet = new ArrayList<>();
        for (Pet item : petList) {

            if (item.getColor().toLowerCase().contains(searchText.toLowerCase())) {
                filteredPet.add(item);
            }
        }
        if (filteredPet.isEmpty()) {
//            Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();

            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#D41717"));
            // Set BackgroundDrawable
            toolbar.setBackground(colorDrawable);

        } else {
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor("#0F9D58"));
            // Set BackgroundDrawable
            toolbar.setBackground(colorDrawable);

            petAdapter.setFilteredList(filteredPet);
        }
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        searchView.setQueryHint("Type here to search");

        //button for hide and show after clicking search icon
//        Button btnHideAndShowAdditionalSearch = view.findViewById(R.id.btn_hide_show);
//        btnHideAndShowAdditionalSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int xx = 0;
//                int currentTag = btnHideAndShowAdditionalSearch.getTag() == null ? 0 :
//                        (int) btnHideAndShowAdditionalSearch.getTag();
//                currentTag++;
//                btnHideAndShowAdditionalSearch.setTag(currentTag);
//                if (currentTag % 2 == 0) {
//                    btnHideAndShowAdditionalSearch.setText("Show");
//                    ckAdditionalSearchCheckBox.setVisibility(View.VISIBLE);
//                    ckAdditionalSearchSpinner.setVisibility(View.VISIBLE);
//                } else {
//                    btnHideAndShowAdditionalSearch.setText("Hide");
//                    ckAdditionalSearchCheckBox.setVisibility(View.GONE);
//                    ckAdditionalSearchSpinner.setVisibility(View.GONE);
//                }
//            }
//        });

        //display after clicking search icon
//        ckAdditionalSearchCheckBox.setVisibility(View.VISIBLE);
//        ckAdditionalSearchSpinner.setVisibility(View.VISIBLE);
//        showoHideButtonFilter.setVisibility(View.VISIBLE);
//
//        //begin display spinner species
//        for (Pet petItem : petList) {
//            spinnersSpeciesList.add(petItem.getSpecies());
//        }
//        Set<String> speciesWithoutDuplicate = new LinkedHashSet<String>(spinnersSpeciesList);
//
//        speciesWithoutDuplicate.remove("Species");
//        speciesWithoutDuplicate.remove("All");
//
//        spinnersSpeciesList.clear();
//
//        spinnersSpeciesList.add("Species");
//        spinnersSpeciesList.add("All");
//
//        for (String i : speciesWithoutDuplicate) {
//            spinnersSpeciesList.add(i);
//        }
        //end display spinner species

        //begin spinnerSpecies spinner
//        spinnerSpecies = (Spinner) view.findViewById(R.id.speciesFilter);
//        ArrayAdapter<String> myadapterSpecies = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1, spinnersSpeciesList);
//        myadapterSpecies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSpecies.setAdapter(myadapterSpecies);
//        spinnerSpecies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//
//                if (selectedItem.equals("All")) {
//                    petListSelected.clear();
//                    petAdapter = new PetAdapter(petList);
//                    recyclerView.setAdapter(petAdapter);
//                } else {
//                    petListSelected.clear();
//                    for (Pet petItemOnlySpecies : petList) {
//                        if (selectedItem.equals(petItemOnlySpecies.getSpecies())) {
//                            petListSelected.add(petItemOnlySpecies);
//                        }
//                    }
//                    petAdapter = new PetAdapter(petListSelected);
//                    recyclerView.setAdapter(petAdapter);
//                }
//                //end spinnerSpecies spinner
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        //begin gender spinner
//        spinnerGender = (Spinner) view.findViewById(R.id.id_gender_spinner);
//        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
//        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerGender.setAdapter(myadapter);

//        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//
//                if (selectedItem.equals("Male")) {
//                    petListSelected.clear();
//                    for (Pet petItem : petList) {
//                        if (petItem.getGender().equals("male")) {
//                            petListSelected.add(petItem);
//                        }
//                    }
//
//                    petAdapter = new PetAdapter(petListSelected);
//                    recyclerView.setAdapter(petAdapter);
//                } else if (selectedItem.equals("Female")) {
//                    petListSelected.clear();
//
//                    for (Pet petItem : petList) {
//                        if (petItem.getGender().equals("female")) {
//                            petListSelected.add(petItem);
//                        }
//                    }
//
//                    petAdapter = new PetAdapter(petListSelected);
//                    recyclerView.setAdapter(petAdapter);
//                } else {
//                    petListSelected.clear();
//                    petAdapter = new PetAdapter(petList);
//                    recyclerView.setAdapter(petAdapter);
//                }
//
//            }
//
//            //end  gender spinner
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        ckAdditionalSearchCheckBox.setVisibility(View.GONE);
        ckAdditionalSearchSpinner.setVisibility(View.GONE);
        showoHideButtonFilter.setVisibility(View.GONE);
        return true;
    }
}