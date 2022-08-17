package tuan.aprotrain.projectpetcare.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import tuan.aprotrain.projectpetcare.Adapter.BookingDialogAdapter;
import tuan.aprotrain.projectpetcare.Adapter.BookingManagerAdapter;
import tuan.aprotrain.projectpetcare.Adapter.PetManagerAdapter;
import tuan.aprotrain.projectpetcare.Adapter.UserManagerAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.BookingDetail;
import tuan.aprotrain.projectpetcare.entity.CaptureAct;
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

        Button button = findViewById(R.id.logoutBtn);

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });

//        one=findViewById(R.id.clientsClick);
//        two=findViewById(R.id.LayoutFollowing);
//        three=findViewById(R.id.LayoutImpacted);
//
//        one.onTouchEvent(new )
        final ImageView scan_QrCode = (ImageView) findViewById(R.id.scan_QrCode);
        scan_QrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
//            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//            builder.setTitle("Result");
//            builder.setMessage(result.getContents());
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            }).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Bookings").orderByChild("bookingId").equalTo(result.getContents())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                    if (bookingSnapshot.exists()){
                        for (DataSnapshot bookings:bookingSnapshot.getChildren()){
                            Booking booking = bookings.getValue(Booking.class);
                            reference.child("Pets").orderByChild("petId")
                                    .equalTo(bookings.getValue(Booking.class).getPetId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot petSnapshot) {
                                    if (petSnapshot.exists()){
                                        for (DataSnapshot pets:petSnapshot.getChildren()){
                                            openDialog(Gravity.CENTER,booking,pets.getValue(Pet.class).getPetName());
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

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    });


    public void openDialog(int gravity, Booking booking, String petName) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.layout_dialog_booking);

        TextView petName_Details, startTimeTxt, endTimeTxt, totalPrice;
        petName_Details = dialog.findViewById(R.id.petName_Details);
        startTimeTxt = dialog.findViewById(R.id.startTimeTxt);
        endTimeTxt = dialog.findViewById(R.id.endTimeTxt);
        totalPrice = dialog.findViewById(R.id.totalPrice);
        ImageView qr_code = dialog.findViewById(R.id.qr_code);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        petName_Details.setText(petName);
        startTimeTxt.setText(booking.getBookingStartDate());
        endTimeTxt.setText(booking.getBookingEndDate());
        totalPrice.setText(booking.getTotalPrice() + "$");

        ListView listView = dialog.findViewById(R.id.listViewService);
        BookingDialogAdapter bookingDialogAdapter = new BookingDialogAdapter(dialog.getContext(),
                booking.getSelectedService());
        listView.setAdapter(bookingDialogAdapter);
        //qr code
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(booking.getBookingId(), BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Button btnCancel = dialog.findViewById(R.id.btnCancelDialog);
        Button btnBooking = dialog.findViewById(R.id.btnBooking);

        //Toast.makeText(this, "Dialog info:" + getCheckedService().get(1), Toast.LENGTH_SHORT).show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Booking Successfully", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            startActivity(new Intent(AdminActivity.this, LoginActivity.class ));
        } else {

        }
    }
}