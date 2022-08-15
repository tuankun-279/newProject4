package tuan.aprotrain.projectpetcare.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tuan.aprotrain.projectpetcare.Adapter.BookingHistoryAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Pet;

public class BookingHistoryActivity extends AppCompatActivity {
    ListView listViewBooking;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        listViewBooking = findViewById(R.id.listViewBooking);

        BookingHistoryAdapter bookingHistoryAdapter = new BookingHistoryAdapter(BookingHistoryActivity.this, getBooking());
        getBooking().forEach(booking -> {
            System.out.println("Booking: " + getBooking());
        });
        listViewBooking.setAdapter(bookingHistoryAdapter);
        listViewBooking.setClickable(true);
    }

    public List<Booking> getBooking() {
        List<Booking> bookingList = new ArrayList<Booking>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Pets").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot petSnapshot) {
                petSnapshot.getChildren().forEach(pets -> {
                    Pet pet = pets.getValue(Pet.class);
                    if (pet.getUserId().equals(user.getUid())) {
                        long petId = pet.getPetId();
                        reference.child("Bookings").orderByChild("petId").equalTo(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                                if (bookingSnapshot.exists()) {
                                    //bookingList.clear();
                                    bookingSnapshot.getChildren().forEach(bookings -> {
                                        bookingList.add(bookings.getValue(Booking.class));

                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return bookingList;
    }
}