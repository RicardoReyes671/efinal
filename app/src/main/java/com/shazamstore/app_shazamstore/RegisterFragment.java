package com.shazamstore.app_shazamstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private TextInputLayout nameTextInput;
    private TextInputEditText nameTextEditText;
    private TextInputLayout lastName1TextInput;
    private TextInputEditText lastName1TextEditText;
    private TextInputLayout lastName2TextInput;
    private TextInputEditText lastName2TextEditText;
    private TextInputLayout dniTextInput;
    private TextInputEditText dniTextEditText;
    private TextInputLayout emailTextInput;
    private TextInputEditText emailTextEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordTextEditText;
    private TextInputLayout confirmPasswordTextInput;
    private TextInputEditText confirmPasswordTextEditText;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private CircularProgressIndicator circularProgressIndicator;

    private String name;
    private String lastName1;
    private String lastName2;
    private String dni;
    private String email;
    private String password;
    private String confirmPassword;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    View view;

    @Override
    public View onCreateView (
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.register_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameTextInput = view.findViewById(R.id.name_text_input);
        nameTextEditText = view.findViewById(R.id.name_edit_text);
        lastName1TextInput = view.findViewById(R.id.last_name_1_text_input);
        lastName1TextEditText = view.findViewById(R.id.last_name_1_edit_text);
        lastName2TextInput = view.findViewById(R.id.last_name_2_text_input);
        lastName2TextEditText = view.findViewById(R.id.last_name_2_edit_text);
        dniTextInput = view.findViewById(R.id.dni_text_input);
        dniTextEditText = view.findViewById(R.id.dni_edit_text);
        emailTextInput = view.findViewById(R.id.email_text_input);
        emailTextEditText = view.findViewById(R.id.email_edit_text);
        passwordTextInput = view.findViewById(R.id.password_register_text_input);
        passwordTextEditText = view.findViewById(R.id.password_register_edit_text);
        confirmPasswordTextInput = view.findViewById(R.id.confirm_password_register_text_input);
        confirmPasswordTextEditText = view.findViewById(R.id.confirm_password_register_edit_text);

        nameTextEditText.setText("");
        lastName1TextEditText.setText("");
        lastName2TextEditText.setText("");
        dniTextEditText.setText("");
        emailTextEditText.setText("");
        passwordTextEditText.setText("");
        confirmPasswordTextEditText.setText("");

        loginButton = view.findViewById(R.id.button_return_login);
        registerButton = view.findViewById(R.id.button_register);
        circularProgressIndicator = view.findViewById(R.id.progress_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((com.shazamstore.app_shazamstore.NavigationHost) getActivity()).navigateTo(new LoginFragment(),false);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameTextEditText.getText().toString();
                lastName1 = lastName1TextEditText.getText().toString();
                lastName2 = lastName2TextEditText.getText().toString();
                dni = dniTextEditText.getText().toString();
                email = emailTextEditText.getText().toString();
                password = passwordTextEditText.getText().toString();
                confirmPassword = confirmPasswordTextEditText.getText().toString();
                if(validateDataRegister()){
                    registerUser();
                }
            }
        });

        return view;
    }

    private boolean validateDataRegister(){
        nameTextInput.setError(null);
        lastName1TextInput.setError(null);
        lastName2TextInput.setError(null);
        dniTextInput.setError(null);
        emailTextInput.setError(null);
        passwordTextInput.setError(null);
        confirmPasswordTextInput.setError(null);

        if(name.isEmpty()){
            nameTextInput.setError(getString(R.string.error_empty_field));
        }
        if(lastName1.isEmpty()){
            lastName1TextInput.setError(getString(R.string.error_empty_field));
        }
        if(lastName2.isEmpty()){
            lastName2TextInput.setError(getString(R.string.error_empty_field));
        }
        if(dni.isEmpty()){
            dniTextInput.setError(getString(R.string.error_empty_field));
        }
        else{
            if(dni.length()>0 && dni.length()<8){
                dniTextInput.setError(getString(R.string.error_dni));
            }
        }
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
        if(confirmPassword.isEmpty()){
            confirmPasswordTextInput.setError(getString(R.string.error_empty_field));
        }
        else{
            if(confirmPassword.length()<6 || confirmPassword.length()>20){
                confirmPasswordTextInput.setError(getString(R.string.error_password));
            }
            else {
                if(!confirmPassword.equals(password)){
                    confirmPasswordTextInput.setError(getString(R.string.error_confirm_password));
                }
            }
        }

        if(!name.isEmpty() && !lastName1.isEmpty() && !lastName2.isEmpty() &&
                dni.length()==8 && !email.isEmpty() && password.length()>=6 &&
                password.length()<=20 && confirmPassword.equals(password)){
                return true;
        }
        return false;
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre",name);
                    map.put("apPaterno",lastName1);
                    map.put("apMaterno",lastName2);
                    map.put("DNI",dni);
                    map.put("email",email);
                    map.put("password",password);

                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Usuarios").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                ((com.shazamstore.app_shazamstore.NavigationHost) getActivity()).navigateTo(new AccountFragment(),false);
                            }
                            else{
                                Snackbar.make(view,"No se pudo registrar el usuario",Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
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