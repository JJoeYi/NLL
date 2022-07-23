package com.orbitalnll.nll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class LungesFragment extends Fragment {
    MaterialButton tryBtn;
    public LungesFragment() {
        super(R.layout.fragment_lunges);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tryBtn = view.findViewById(R.id.lunges_tryBtn);
        tryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id) {
                    case R.id.lunges_tryBtn:
                        Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LivePreviewActivityMain.class));
                }
            }
        });
    }

}