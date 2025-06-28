package com.example.ibgenewsblog.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private List<Noticia> favoritos;
    private List<Noticia> lidas;
    private List<Noticia> paraLerDepois;

    public Usuario() {
        this.favoritos = new ArrayList<>();
        this.lidas = new ArrayList<>();
        this.paraLerDepois = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Noticia> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Noticia> favoritos) {
        this.favoritos = favoritos;
    }

    public List<Noticia> getLidas() {
        return lidas;
    }

    public void setLidas(List<Noticia> lidas) {
        this.lidas = lidas;
    }

    public List<Noticia> getParaLerDepois() {
        return paraLerDepois;
    }

    public void setParaLerDepois(List<Noticia> paraLerDepois) {
        this.paraLerDepois = paraLerDepois;
    }
}
