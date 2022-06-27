package com.orbitalnll.nll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class vaultMainFragment extends Fragment implements View.OnClickListener {
    MaterialCardView pushUpCard;
    NavController controller;
    MaterialButton trybtn;
    // private long mLastClickTime = 0;

    public vaultMainFragment() {
        super(R.layout.vault_main_fragment_layout);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.push_up_cardView).setOnClickListener(this);
        view.findViewById(R.id.sit_up_cardView).setOnClickListener(this);
        view.findViewById(R.id.squat_cardView).setOnClickListener(this);
        view.findViewById(R.id.cardio_mini_cardView).setOnClickListener(this);
        view.findViewById(R.id.strength_mini_cardView).setOnClickListener(this);
        view.findViewById(R.id.accessories_mini_cardView).setOnClickListener(this);
        view.findViewById(R.id.warmup_mini_cardView).setOnClickListener(this);

        view.findViewById(R.id.push_up_trybtn).setOnClickListener(this);
        view.findViewById(R.id.sit_up_tryBtn).setOnClickListener(this);
        view.findViewById(R.id.squat_tryBtn).setOnClickListener(this);

        controller = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.push_up_trybtn:
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LivePreviewActivityMain.class));

            case R.id.sit_up_tryBtn:
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LivePreviewActivityMain.class));

            case R.id.squat_tryBtn:
                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), LivePreviewActivityMain.class));

            case R.id.push_up_cardView:
                //TODO: add transitions

                Toast.makeText(getActivity(), "push up card pressed", Toast.LENGTH_SHORT).show();
                //TODO: if controller null?
                controller.navigate(R.id.action_vaultMainFragment_to_pushUpFragment);
                return;

            case R.id.squat_cardView:
                Toast.makeText(getActivity(), "squat card pressed", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_vaultMainFragment_to_squatFragment);
                return;

            case R.id.sit_up_cardView:
                Toast.makeText(getActivity(), "sit up card pressed", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_vaultMainFragment_to_sitUpFragment);
                return;
            case R.id.cardio_mini_cardView:
                Toast.makeText(getActivity(), "cardio mini pressed", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_vaultMainFragment_to_cardioMiniFragment);
                return;

            case R.id.strength_mini_cardView:
                Toast.makeText(getActivity(), "strength mini pressed", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_vaultMainFragment_to_strengthMiniFragment);
                return;

            case R.id.accessories_mini_cardView:
                Toast.makeText(getActivity(), "accessories mini pressed", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_vaultMainFragment_to_accessoriesMiniFragment);
                return;

            case R.id.warmup_mini_cardView:
                Toast.makeText(getActivity(), "warmup mini pressed", Toast.LENGTH_SHORT).show();
                controller.navigate(R.id.action_vaultMainFragment_to_warmUpMiniFragment);
                return;
            default:
                return;
        }
    }


}
