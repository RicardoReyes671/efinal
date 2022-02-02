package com.shazamstore.app_shazamstore.models;

public class Sesion {
    private Integer idSesion;
    private Integer idUsuario;
    private String estado;

    public Sesion() {
    }

    public Sesion(Integer idSesion, Integer idUsuario, String estado) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.estado = estado;
    }

    public Integer getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Integer idSesion) {
        this.idSesion = idSesion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
