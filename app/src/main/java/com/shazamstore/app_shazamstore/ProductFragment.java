package com.shazamstore.app_shazamstore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.shazamstore.app_shazamstore.models.Categoria;
import com.shazamstore.app_shazamstore.models.Producto;
import com.shazamstore.app_shazamstore.network.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ProductFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private Context context;
    Spinner spinner;
    RecyclerView recyclerView;
    ProductCardRecyclerViewAdapter productCardRecyclerViewAdapter;
    List<Producto> productoList = new ArrayList<>();
    List<Categoria> categoriaList = new ArrayList<>();
    CircularProgressIndicator circularProgressIndicator;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.product_fragment,container,false);

        circularProgressIndicator = view.findViewById(R.id.progress_products);
        spinner = view.findViewById(R.id.spinner_products_categories);
        recyclerView = view.findViewById(R.id.recycler_view);

        databaseHelper = new DatabaseHelper(this.getContext());
        loadCategorias();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                circularProgressIndicator.setVisibility(View.VISIBLE);
                if(i!=0){
                    ApiService.getApiService().findAllProducts().enqueue(new Callback<List<Producto>>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                            if(response.isSuccessful() && response.body()!=null) {
                                productoList.removeAll(productoList);
                                for(Producto p : response.body()){
                                    if(p.getIdCategoria() == categoriaList.get(i-1).getIdCategoria()){

                                        productoList.add(p);
                                    }
                                }
                                circularProgressIndicator.setVisibility(View.GONE);
                                productCardRecyclerViewAdapter.notifyDataSetChanged();
                            }
                            else{
                                circularProgressIndicator.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Algo ha salido mal...", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        @EverythingIsNonNull
                        public void onFailure(Call<List<Producto>> call, Throwable t) {
                            circularProgressIndicator.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Algo ha salido mal...", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    productoList.clear();
                    loadProductos();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(productoList.isEmpty()){
            loadProductos();
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));

        productCardRecyclerViewAdapter = new ProductCardRecyclerViewAdapter(productoList);
        recyclerView.setAdapter(productCardRecyclerViewAdapter);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.product_grid_spacing_small);
        recyclerView.addItemDecoration(new com.shazamstore.app_shazamstore.ProductGridItemDecoration(largePadding, smallPadding));

        return view;
    }

    private void loadProductos(){
        circularProgressIndicator.setVisibility(View.VISIBLE);
        spinner.setEnabled(false);
        ApiService.getApiService().findAllProducts().enqueue(new Callback<List<Producto>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    circularProgressIndicator.setVisibility(View.GONE);
                    databaseHelper.eliminarProductos();
                    for(Producto p : response.body()){
                        databaseHelper.registrarProductoDb(p);
                    }
                    productoList.addAll(response.body());
                    productCardRecyclerViewAdapter.notifyDataSetChanged();
                    spinner.setEnabled(true);
                }
                else{
                    circularProgressIndicator.setVisibility(View.GONE);
                    if(databaseHelper.obtenerProductosDb()!=null){
                        productoList.addAll(databaseHelper.obtenerProductosDb());
                    }
                    Toast.makeText(getContext(), "Algo ha salido mal...", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                circularProgressIndicator.setVisibility(View.GONE);
                if(databaseHelper.obtenerProductosDb()!=null){
                    productoList.addAll(databaseHelper.obtenerProductosDb());
                }
                Toast.makeText(getContext(), "Algo ha salido mal...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addItemsToSpinner(List<String> stringList){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, stringList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public void loadCategorias(){
        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Todas");
        ApiService.getApiService().findAllCategories().enqueue(new Callback<List<Categoria>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    categoriaList.addAll(response.body());
                    for(Categoria c : response.body()){
                        listSpinner.add(c.getDescripcion());
                    }
                    addItemsToSpinner(listSpinner);
                }
                else{
                    addItemsToSpinner(listSpinner);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                addItemsToSpinner(listSpinner);
            }
        });
    }
}
