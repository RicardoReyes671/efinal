package com.shazamstore.app_shazamstore.network.service;

import com.shazamstore.app_shazamstore.models.Categoria;
import com.shazamstore.app_shazamstore.models.Producto;
import com.shazamstore.app_shazamstore.models.Sesion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("producto")
    Call<List<Producto>> findAllProducts();

    @GET("producto")
    Call<Producto> findProductById(@Query("idProducto") Integer idProducto);

    @GET("categoria")
    Call<List<Categoria>> findAllCategories();

    @GET("categoria")
    Call<Categoria> findCategoryById(@Query("idCategoria") Integer idCategoria);

    /*@GET("marca")
    Call<List<Marca>> findAllBrands();

    @GET("marca")
    Call<Marca> findBrandById(@Query("idMarca") Integer idMarca);

    @GET("carrito")
    Call<List<Carrito>> findAllCarts();

    @GET("carrito")
    Call<Carrito> findCartById(@Query("idCarrito") Integer idCarrito);

    @GET("usuario")
    Call<List<Usuario>> findAllUsers();

    @GET("usuario")
    Call<Usuario> findUserById(@Query("idUsuario") Integer idUsuario);*/

    @GET("sesion")
    Call<Sesion> findAllSessions();

    @GET("sesion")
    Call<Sesion> findSessionById(@Query("idSesion") Integer idSesion);
}
