package tuan.aprotrain.projectpetcare.Fragment.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.databinding.FragmentShareBinding;

public class ShareFragment extends Fragment {

    //    private ShareViewModel shareViewModel;
    private FragmentShareBinding binding;

    public static ShareFragment newInstance() {
        return new ShareFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentShareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button btnSth = root.findViewById(R.id.btn_sth);
        btnSth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Make this", Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }

}
