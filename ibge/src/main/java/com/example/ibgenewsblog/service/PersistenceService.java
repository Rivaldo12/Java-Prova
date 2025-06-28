package com.example.ibgenewsblog.service;

import com.example.ibgenewsblog.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PersistenceService {

    private static final String FILE_PATH = "user_data.json";
    private final ObjectMapper objectMapper;

    public PersistenceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void saveUser(Usuario user) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), user);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados do usuario: " + e.getMessage());
        }
    }

    public Usuario loadUser() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                return objectMapper.readValue(file, Usuario.class);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados do usuario: " + e.getMessage());
        }
        return null;
    }
}
