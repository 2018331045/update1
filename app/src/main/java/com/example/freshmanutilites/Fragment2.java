package com.example.freshmanutilites;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment implements View.OnClickListener  {


    private CardView varsitycard, residentialcard ;
private TextView tv;
    @Nullable
    @Override


    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // inflate the xml fragment ...
        View view = inflater.inflate(R.layout.fragment2,container,false);




        return view;




    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        varsitycard = getActivity().findViewById(R.id.cv1);
        residentialcard = getActivity().findViewById(R.id.cv2);
        varsitycard.setOnClickListener(this);



    }


    public  void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.cv1:
                Intent intent = new Intent(v.getContext(), VarsityActivity.class);
                startActivity(intent);
        }


    }


    

}
