
package com.escola.notas.view;

import com.escola.notas.model.Disciplina;
import com.escola.notas.util.JsonManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DisciplinaFormPanel extends JPanel {
    private JTextField codigoField;
    private JTextField nomeField;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JList<Disciplina> disciplinaList;
    private DefaultListModel<Disciplina> disciplinaListModel;

    public DisciplinaFormPanel() {
        setLayout(new BorderLayout());

        // Formulário de entrada
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Disciplina"));

        formPanel.add(new JLabel("Código:"));
        codigoField = new JTextField();
        formPanel.add(codigoField);

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(salvarButton);
        buttonPanel.add(novoButton);
        buttonPanel.add(excluirButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Lista de disciplinas
        disciplinaListModel = new DefaultListModel<>();
        disciplinaList = new JList<>(disciplinaListModel);
        JScrollPane scrollPane = new JScrollPane(disciplinaList);
        add(scrollPane, BorderLayout.CENTER);

        carregarDisciplinas();
        adicionarListeners();
    }

    private void adicionarListeners() {
        salvarButton.addActionListener(e -> salvarDisciplina());
        novoButton.addActionListener(e -> limparCampos());
        excluirButton.addActionListener(e -> excluirDisciplina());
        disciplinaList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Disciplina disciplinaSelecionada = disciplinaList.getSelectedValue();
                if (disciplinaSelecionada != null) {
                    codigoField.setText(disciplinaSelecionada.getCodigo());
                    nomeField.setText(disciplinaSelecionada.getNome());
                }
            }
        });
    }

    private void carregarDisciplinas() {
        disciplinaListModel.clear();
        List<Disciplina> disciplinas = JsonManager.carregarDisciplinas();
        for (Disciplina disciplina : disciplinas) {
            disciplinaListModel.addElement(disciplina);
        }
    }

    private void salvarDisciplina() {
        String codigo = codigoField.getText().trim();
        String nome = nomeField.getText().trim();

        if (!validarCampos(codigo, nome)) {
            return;
        }

        List<Disciplina> disciplinas = JsonManager.carregarDisciplinas();
        boolean disciplinaExiste = false;
        for (int i = 0; i < disciplinas.size(); i++) {
            if (disciplinas.get(i).getCodigo().equals(codigo)) {
                disciplinas.set(i, new Disciplina(codigo, nome)); // Atualiza
                disciplinaExiste = true;
                break;
            }
        }

        if (!disciplinaExiste) {
            disciplinas.add(new Disciplina(codigo, nome)); // Cria
        }

        JsonManager.salvarDisciplinas(disciplinas);
        carregarDisciplinas();
        limparCampos();
        JOptionPane.showMessageDialog(this, "Disciplina salva com sucesso!");
    }

    private void excluirDisciplina() {
        Disciplina disciplinaSelecionada = disciplinaList.getSelectedValue();
        if (disciplinaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir a disciplina " + disciplinaSelecionada.getNome() + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            List<Disciplina> disciplinas = JsonManager.carregarDisciplinas();
            disciplinas.removeIf(disciplina -> disciplina.getCodigo().equals(disciplinaSelecionada.getCodigo()));
            JsonManager.salvarDisciplinas(disciplinas);
            carregarDisciplinas();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Disciplina excluída com sucesso!");
        }
    }

    private void limparCampos() {
        codigoField.setText("");
        nomeField.setText("");
        disciplinaList.clearSelection();
    }

    private boolean validarCampos(String codigo, String nome) {
        if (codigo.isEmpty() || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (codigo.length() > 10) {
            JOptionPane.showMessageDialog(this, "Código deve ter no máximo 10 caracteres.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}


