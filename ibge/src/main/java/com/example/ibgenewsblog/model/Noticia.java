package com.example.ibgenewsblog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Noticia {

    private String titulo;
    private String introducao;
    private String dataPublicacao;
    private String link;
    private String tipo;
    private String fonte = "IBGE";

    public Noticia() {}

    @JsonProperty("titulo")
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @JsonProperty("introducao")
    public String getIntroducao() {
        return introducao;
    }

    public void setIntroducao(String introducao) {
        this.introducao = introducao;
    }

    @JsonProperty("data_publicacao")
    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("tipo")
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    @Override
    public String toString() {
        return "\n--- " + titulo + " ---" +
                "\n  Introducao: " + introducao +
                "\n  Data de Publicacao: " + dataPublicacao +
                "\n  Tipo: " + tipo +
                "\n  Fonte: " + fonte +
                "\n  Link: " + link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noticia noticia = (Noticia) o;
        return link.equals(noticia.link);
    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }
}