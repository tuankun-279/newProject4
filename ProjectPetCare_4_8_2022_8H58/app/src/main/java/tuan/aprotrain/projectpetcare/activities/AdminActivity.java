package tuan.aprotrain.projectpetcare.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tuan.aprotrain.projectpetcare.Adapter.BookingManagerAdapter;
import tuan.aprotrain.projectpetcare.Adapter.PetManagerAdapter;
import tuan.aprotrain.projectpetcare.Adapter.UserManagerAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Pet;
import tuan.aprotrain.projectpetcare.entity.Service;
import tuan.aprotrain.projectpetcare.entity.User;

public class AdminActivity extends AppCompatActivity {
    List<User> usersList;
    List<Booking> bookingsList;
    List<Pet> petsList;
    private ListView listView;
    ImageView user, pet, booking;
    DatabaseReference databaseReferenceUser, databaseReferencePet, databaseReferenceBooking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listView = findViewById(R.id.listView);
        usersList = new ArrayList<>();
        bookingsList = new ArrayList<>();
        petsList = new ArrayList<>();
        user = findViewById(R.id.userImg);
        pet = findViewById(R.id.petImg);
        booking = findViewById(R.id.bookingImg);

        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot model:dataSnapshot.getChildren()){
                    User user = model.getValue(User.class);
                    usersList.add(user);
                }
                ListAdapter adapter = new UserManagerAdapter(AdminActivity.this,usersList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Users");
                databaseReferenceUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usersList.clear();
                        for(DataSnapshot model:dataSnapshot.getChildren()){
                            User user = model.getValue(User.class);
                            usersList.add(user);
                        }
                        ListAdapter adapter = new UserManagerAdapter(AdminActivity.this,usersList);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReferencePet = FirebaseDatabase.getInstance().getReference("Pets");
                databaseReferencePet.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        petsList.clear();
                        for(DataSnapshot model:dataSnapshot.getChildren()){
                            Pet pet = model.getValue(Pet.class);
                            petsList.add(pet);
                        }
                        ListAdapter adapter = new PetManagerAdapter(AdminActivity.this,petsList);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReferenceBooking = FirebaseDatabase.getInstance().getReference("Bookings");
                databaseReferenceBooking.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookingsList.clear();
                        for(DataSnapshot model:dataSnapshot.getChildren()){
                            Booking booking = model.getValue(Booking.class);
                            bookingsList.add(booking);
                        }
                        ListAdapter adapter = new BookingManagerAdapter(AdminActivity.this,bookingsList);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

//        one=findViewById(R.id.clientsClick);
//        two=findViewById(R.id.LayoutFollowing);
//        three=findViewById(R.id.LayoutImpacted);
//
//        one.onTouchEvent(new )

    }
}