package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Danilo on 12/5/17.
 */

public class Loginregister extends Fragment {
    Button login;
    Button register1;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginregisterxml, container, false);

        login = (Button) view.findViewById(R.id.loginbutton);
        register1 = (Button) view.findViewById(R.id.button3);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main,login);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register register = new Register();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main,register);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;


    }
    }

