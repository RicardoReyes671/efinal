package com.shazamstore.app_shazamstore;

import androidx.fragment.app.Fragment;

//Host tipico para una actividad que se mostrará como un fragmento
//Eventos de navegación
public interface NavigationHost {
    //Disparador de navegación de un fragmento específico
    //Adicionalmente ir hacia atrás
    //Es posible hacer este stack reversible
    void navigateTo(Fragment fragment, boolean addToBackstack);
}
