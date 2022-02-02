package com.shazamstore.app_shazamstore;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements com.shazamstore.app_shazamstore.NavigationHost {

    AccountFragment accountFragment = new AccountFragment();
    LoginFragment loginFragment = new LoginFragment();
    ProductFragment productFragment = new ProductFragment();
    ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container,
                            new ProductFragment()).commit();
        }

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.home:
                    loadFragment(productFragment);
                    return true;
                case R.id.shopping_cart:
                    loadFragment(shoppingCartFragment);
                    return true;
                case R.id.account:
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();

                    if(user!=null){
                        loadFragment(accountFragment);
                    }
                    else {
                        loadFragment(loginFragment);
                    }
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }

    /**
        *Aquí va el fragmento de navegación
        *@param fragment Fragmento a donde vamos a navegar
        *@param addToBackstack Si podemos ir hacia atrás o no
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack){
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment);

        if(addToBackstack){
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}