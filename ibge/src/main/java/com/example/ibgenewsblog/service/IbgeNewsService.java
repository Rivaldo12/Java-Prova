package com.example.ibgenewsblog.service;

import com.example.ibgenewsblog.model.Noticia;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class IbgeNewsService {

    private static final String API_URL = "https://servicodados.ibge.gov.br/api/v3/noticias";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public IbgeNewsService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Noticia> buscarNoticias(String query) {
        List<Noticia> noticias = new ArrayList<>();
        String url = API_URL + "?busca=" + query;
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("items");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    Noticia noticia = new Noticia();
                    noticia.setTitulo(item.path("titulo").asText());
                    noticia.setIntroducao(item.path("introducao").asText());
                    noticia.setDataPublicacao(item.path("data_publicacao").asText());
                    noticia.setLink(item.path("link").asText());
                    noticia.setTipo(item.path("tipo").asText());
                    noticias.add(noticia);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar noticias do IBGE: " + e.getMessage());
        }
        return noticias;
    }

    public List<Noticia> buscarNoticiasPorTipo(String tipo) {
        List<Noticia> noticias = new ArrayList<>();
        String url = API_URL + "?tipo=" + tipo;
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("items");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    Noticia noticia = new Noticia();
                    noticia.setTitulo(item.path("titulo").asText());
                    noticia.setIntroducao(item.path("introducao").asText());
                    noticia.setDataPublicacao(item.path("data_publicacao").asText());
                    noticia.setLink(item.path("link").asText());
                    noticia.setTipo(item.path("tipo").asText());
                    noticias.add(noticia);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar noticias do IBGE por tipo: " + e.getMessage());
        }
        return noticias;
    }

    public List<Noticia> buscarNoticiasRecentes() {
        List<Noticia> noticias = new ArrayList<>();
        String url = API_URL + "?qtd=10";
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("items");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    Noticia noticia = new Noticia();
                    noticia.setTitulo(item.path("titulo").asText());
                    noticia.setIntroducao(item.path("introducao").asText());
                    noticia.setDataPublicacao(item.path("data_publicacao").asText());
                    noticia.setLink(item.path("link").asText());
                    noticia.setTipo(item.path("tipo").asText());
                    noticias.add(noticia);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar noticias recentes do IBGE: " + e.getMessage());
        }
        return noticias;
    }
}
