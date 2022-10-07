package com.orbitalnll.nll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class SquatFragment extends Fragment {
    MaterialButton tryBtn;
    public SquatFragment() {
        super(R.layout.fragment_squat);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tryBtn = view.findViewById(R.id.squat_tryBtn);
        tryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                switch (id) {
                    case R.id.squat_tryBtn:
//                        Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), LivePreviewActivityMain.class));
                }
            }
        });
    }

}