package com.orbitalnll.nll;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CardioMiniFragment extends Fragment {

    public CardioMiniFragment() {
        super(R.layout.fragment_cardio_mini);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView text = view.findViewById(R.id.cardio_mini_textView);
        text.setMovementMethod(new ScrollingMovementMethod());
    }


}
