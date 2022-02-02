package com.shazamstore.app_shazamstore;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shazamstore.app_shazamstore.models.Producto;
import com.shazamstore.app_shazamstore.network.ImageRequest;

import java.util.List;

public class ProductCardRecyclerViewAdapter extends RecyclerView.Adapter<ProductCardViewHolder> {
    private final List<Producto> productList;
    private final ImageRequest imageRequest;

    ProductCardRecyclerViewAdapter(List<Producto> productList){
        this.productList = productList;
        imageRequest = ImageRequest.getInstance();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
        return new ProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position){
        if(productList != null && position < productList.size()){
            Producto product = productList.get(position);
            holder.productId.setId(product.getIdProducto());
            holder.productTitle.setText(product.getDescripcion());
            String price = "S/."+product.getPrecio().toString();
            holder.productPrice.setText(price);
            imageRequest.setImageFromUrl(holder.productImage, product.getUrl());
            if(product.getStock() > 0){
                holder.productAvailability.setText("");
            } else {
                String availability = "Agotado";
                holder.productAvailability.setText(availability);
                holder.productAvailability.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount(){
        return productList.size();
    }
}
