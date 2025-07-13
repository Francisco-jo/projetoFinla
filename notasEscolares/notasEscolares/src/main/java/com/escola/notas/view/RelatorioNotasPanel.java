
package com.escola.notas.view;

import com.escola.notas.model.Aluno;
import com.escola.notas.model.Disciplina;
import com.escola.notas.model.Nota;
import com.escola.notas.util.JsonManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioNotasPanel extends JPanel {
    private JTable relatorioTable;
    private DefaultTableModel tableModel;

    public RelatorioNotasPanel() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Aluno");
        tableModel.addColumn("Disciplina");
        tableModel.addColumn("Nota");
        tableModel.addColumn("Média do Aluno");

        relatorioTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(relatorioTable);
        add(scrollPane, BorderLayout.CENTER);

        carregarRelatorio();
    }

    private void carregarRelatorio() {
        tableModel.setRowCount(0); // Limpa a tabela

        List<Aluno> alunos = JsonManager.carregarAlunos();
        List<Disciplina> disciplinas = JsonManager.carregarDisciplinas();
        List<Nota> notas = JsonManager.carregarNotas();

        // Calcula a média de cada aluno
        Map<String, Double> mediasAlunos = notas.stream()
                .collect(Collectors.groupingBy(Nota::getMatriculaAluno,
                        Collectors.averagingDouble(Nota::getValor)));

        for (Nota nota : notas) {
            String nomeAluno = alunos.stream()
                    .filter(a -> a.getMatricula().equals(nota.getMatriculaAluno()))
                    .map(Aluno::getNome)
                    .findFirst()
                    .orElse("Aluno Desconhecido");

            String nomeDisciplina = disciplinas.stream()
                    .filter(d -> d.getCodigo().equals(nota.getCodigoDisciplina()))
                    .map(Disciplina::getNome)
                    .findFirst()
                    .orElse("Disciplina Desconhecida");

            Double mediaAluno = mediasAlunos.get(nota.getMatriculaAluno());

            tableModel.addRow(new Object[]{nomeAluno, nomeDisciplina, nota.getValor(), String.format("%.2f", mediaAluno)});
        }
    }
}


