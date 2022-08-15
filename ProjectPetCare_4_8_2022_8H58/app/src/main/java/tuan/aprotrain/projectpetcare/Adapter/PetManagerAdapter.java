package tuan.aprotrain.projectpetcare.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Pet;
import tuan.aprotrain.projectpetcare.entity.Service;

public class PetManagerAdapter extends ArrayAdapter {
    private Activity Context;
    List<Pet> usersList;
    public PetManagerAdapter(Activity Context, List<Pet> usersList){
        super(Context, R.layout.list_pet, usersList);
        this.Context=Context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater=Context.getLayoutInflater();
        View listItemView=inflater.inflate(R.layout.list_pet,null,true);

        TextView name = listItemView.findViewById(R.id.petName);
        TextView gender = listItemView.findViewById(R.id.gender);
        TextView species = listItemView.findViewById(R.id.species);
        TextView kind = listItemView.findViewById(R.id.kind);
        TextView birthDate = listItemView.findViewById(R.id.birthDate);
        TextView color = listItemView.findViewById(R.id.color);
        //ImageView img=listItemView.findViewById(R.id.img1);

        Pet pet = usersList.get(position);
        name.setText(pet.getPetName());
        gender.setText(pet.getGender());
        species.setText(pet.getSpecies());
        kind.setText(pet.getKind());
        birthDate.setText(pet.getBirthDate());
        color.setText(pet.getColor());

        return listItemView;
    }
}