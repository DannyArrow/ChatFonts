package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Danilo on 12/5/17.
 */

public class Login extends Fragment {
EditText userlog;
EditText password;
Button submit;
TextView registerpage;
private FirebaseAuth mAuth;
Fragment fragment;
FragmentManager fragmentManager;
FragmentTransaction fragmentTransaction;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        mAuth = FirebaseAuth.getInstance();
        userlog = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.username);
        submit = (Button) view.findViewById(R.id.submit_btn);
        registerpage = (TextView) view.findViewById(R.id.textView);
        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();

                String email = userlog.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){

                        }
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "succesfully login", Toast.LENGTH_SHORT).show();

                            fragment = new Messaging();
                            fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main,fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                        if(!task.isSuccessful()){
                            Toast.makeText(getActivity(), "something wetn wrong", Toast.LENGTH_SHORT).show();

                        }

                    }
                });



            }
        });




        return view;
    }
}
