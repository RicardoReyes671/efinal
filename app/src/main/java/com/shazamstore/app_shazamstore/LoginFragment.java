package com.shazamstore.app_shazamstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginFragment extends Fragment {

    private TextInputLayout emailTextInput;
    private TextInputEditText emailTextEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordTextEditText;

    private String email;
    private String password;

    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (
        @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.login_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();

        emailTextInput = view.findViewById(R.id.email_login_text_input);
        emailTextEditText = view.findViewById(R.id.email_login_edit_text);
        passwordTextInput = view.findViewById(R.id.password_login_text_input);
        passwordTextEditText = view.findViewById(R.id.password_login_edit_text);

        MaterialButton loginButton = view.findViewById(R.id.button_login);
        MaterialButton registerButton = view.findViewById(R.id.button_new_account);

        emailTextEditText.setText("");
        passwordTextEditText.setText("");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailTextEditText.getText().toString();
                password = passwordTextEditText.getText().toString();
                if(validateDataLogin()){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Sesión iniciada correctamente. Bienvenido.", Toast.LENGTH_LONG).show();
                                goToProfile();
                            }
                            else{
                                ErrorList errorList = new ErrorList();
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                errorList.errorRegisterUser(errorCode,getContext(),emailTextInput,passwordTextInput);
                            }
                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ((com.shazamstore.app_shazamstore.NavigationHost) getActivity()).navigateTo(new com.shazamstore.app_shazamstore.RegisterFragment(),false);
            }
        });

        return view;
    }

    private boolean validateDataLogin (){
        emailTextInput.setError(null);
        passwordTextInput.setError(null);

        if(email.isEmpty()){
            emailTextInput.setError(getString(R.string.error_empty_field));
        }
        if(password.isEmpty()){
            passwordTextInput.setError(getString(R.string.error_empty_field));
        }
        else{
            if(password.length()> 0 && password.length()<6 || password.length()>20){
                passwordTextInput.setError(getString(R.string.error_password));
            }
        }

        if(!email.isEmpty() && password.length()>=6 && password.length()<=20){
            return true;
        }
        return false;
    }

    private void goToProfile(){
        ((com.shazamstore.app_shazamstore.NavigationHost) getActivity()).navigateTo(new com.shazamstore.app_shazamstore.AccountFragment(),false);
    }
}
