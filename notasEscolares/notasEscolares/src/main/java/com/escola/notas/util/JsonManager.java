package com.escola.notas.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.escola.notas.model.Aluno;
import com.escola.notas.model.Disciplina;
import com.escola.notas.model.Nota;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> void salvarDados(List<T> dados, String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            GSON.toJson(dados, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> carregarDados(String nomeArquivo, Type tipoLista) {
        File file = new File(nomeArquivo);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(nomeArquivo)) {
            return GSON.fromJson(reader, tipoLista);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Aluno> carregarAlunos() {
        Type tipoListaAluno = new TypeToken<List<Aluno>>() {}.getType();
        return carregarDados("alunos.json", tipoListaAluno);
    }

    public static void salvarAlunos(List<Aluno> alunos) {
        salvarDados(alunos, "alunos.json");
    }

    public static List<Disciplina> carregarDisciplinas() {
        Type tipoListaDisciplina = new TypeToken<List<Disciplina>>() {}.getType();
        return carregarDados("disciplinas.json", tipoListaDisciplina);
    }

    public static void salvarDisciplinas(List<Disciplina> disciplinas) {
        salvarDados(disciplinas, "disciplinas.json");
    }

    public static List<Nota> carregarNotas() {
        Type tipoListaNota = new TypeToken<List<Nota>>() {}.getType();
        return carregarDados("notas.json", tipoListaNota);
    }

    public static void salvarNotas(List<Nota> notas) {
        salvarDados(notas, "notas.json");
    }
}


