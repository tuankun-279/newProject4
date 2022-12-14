package tuan.aprotrain.projectpetcare.activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import tuan.aprotrain.projectpetcare.activities.Pet.PetActivity;
import tuan.aprotrain.projectpetcare.databinding.ActivityMainBinding;
import tuan.aprotrain.projectpetcare.entity.Image;
import tuan.aprotrain.projectpetcare.entity.Pet;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import tuan.aprotrain.projectpetcare.Adapter.petListNavDerAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.User;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_GALLERY = 1;
    private static final int FRAGMENT_SHARE = 2;

    private int mCurrentFragment = FRAGMENT_HOME;

    private RecyclerView recyclerView;
    private petListNavDerAdapter adapter;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private BottomNavigationView mBottomNavigationView;

    private DatabaseReference mReference;

    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList("Home", "Feedback", "Contact Us", "Share us",
            "Version Code", "Live Watch", "Add Items"));

    private ArrayList<Pet> pets;
    private ArrayList<Image> images;

    private DrawerLayout mDrawerLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        replaceFragment(new HomeFragment());

        mReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recyclerListPet);

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //tuan
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if(getIntent().hasExtra("Yes")){
            Toast.makeText(this, "Hello Accept", Toast.LENGTH_SHORT).show();
        }else if(getIntent().hasExtra("No")){
            Toast.makeText(this, "Hello No", Toast.LENGTH_SHORT).show();
        }

        // bottom drawer
        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_breeding, R.id.nav_slideshow, R.id.nav_booking_history)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // bottom navigation
        binding.appBarMain.contentMain.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
//                    replaceFragment(new HomeFragment());
                    getSupportActionBar().show();
                    NavOptions navOptionsHome = new NavOptions.Builder()
                            .setPopUpTo(R.id.action_home, true)
                            .build();
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_home, null, navOptionsHome);
                    break;
                case R.id.action_breeding:
//                    replaceFragment(new GalleryFragment());
                    getSupportActionBar().hide();
                    NavOptions navOptionsBreeding = new NavOptions.Builder()
                            .setPopUpTo(R.id.action_breeding, true)
                            .build();
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_breeding, null, navOptionsBreeding);
                    break;
                case R.id.action_slideshow:
//                    replaceFragment(new SlideshowFragment());
                    getSupportActionBar().show();
                    NavOptions navOptionsSlideShow = new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_slideshow, true)
                            .build();
                    Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_slideshow, null, navOptionsSlideShow);
                    break;
            }
            return true;
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);

//        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_share)
//                .setOpenableLayout(mDrawerLayout)
//                .build();


        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new petListNavDerAdapter(pets, images, MainActivity.this);
        //Conecting Adapter Class with the recycler view
        recyclerView.setAdapter(adapter);

        GetPetNameDataFromDatabase();
        GetListImagePetFromDatabase();
    }


//    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.content_frame, fragment);
//        fragmentTransaction.commit();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 101:
                Snackbar.make(findViewById(R.id.drawer_layout),
                        "You clicked Edit Pet", Snackbar.LENGTH_LONG).show();
                return true;
            case 102:
                Snackbar.make(findViewById(R.id.drawer_layout),
                        "You clicked Delete Pet", Snackbar.LENGTH_LONG).show();
                petListNavDerAdapter personAdapter = new petListNavDerAdapter();
//                String selectedPetString = personAdapter.getSelectedPet();
//                int i = 0;
                adapter.RemoveSelectedPet();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void GetPetNameDataFromDatabase() {
        Query query = mReference.child("Pets");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearPetNameAll();
                pets = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long idPet = Long.parseLong(snapshot.child("petId").getValue().toString());
                    String petName = snapshot.child("petName").getValue().toString();
                    Pet pet = new Pet(idPet, petName);
                    pets.add(pet);
                }
                adapter = new petListNavDerAdapter(pets, images, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                GetMatchingDataPetNameAndImage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetListImagePetFromDatabase() {
        Query query = mReference.child("Image");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearPetImageAll();
                images = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long idPet = Long.parseLong(snapshot.child("petId").getValue().toString());
                    String url = snapshot.child("url").getValue().toString();
                    images.add(new Image(url, idPet));
                }
                adapter = new petListNavDerAdapter(pets, images, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//                GetMatchingDataPetNameAndImage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearPetNameAll() {
        if (pets != null) {
            pets.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        pets = new ArrayList<>();
    }

    private void clearPetImageAll() {
        if (images != null) {
            images.clear();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
        images = new ArrayList<>();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Pet Options");
        menu.add(0, 101, 0, "Edit it");
        menu.add(1, 102, 1, "Remove it");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_booking_history:
                NavOptions navOptionsShare = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_booking_history, true)
                        .build();
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main).navigate(R.id.nav_booking_history, null, navOptionsShare);
                return true;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            mReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (!snapshot.child(user.getUid()).getValue(User.class).getUserRole().equals("user")) {
                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                        } else {

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}