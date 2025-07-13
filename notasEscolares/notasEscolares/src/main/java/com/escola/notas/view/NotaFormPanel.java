
package com.escola.notas.view;

import com.escola.notas.model.Aluno;
import com.escola.notas.model.Disciplina;
import com.escola.notas.model.Nota;
import com.escola.notas.util.JsonManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class NotaFormPanel extends JPanel {
    private JComboBox<Aluno> alunoComboBox;
    private JComboBox<Disciplina> disciplinaComboBox;
    private JTextField valorField;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JList<Nota> notaList;
    private DefaultListModel<Nota> notaListModel;

    public NotaFormPanel() {
        setLayout(new BorderLayout());

        // Formulário de entrada
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Nota"));

        formPanel.add(new JLabel("Aluno:"));
        alunoComboBox = new JComboBox<>();
        formPanel.add(alunoComboBox);

        formPanel.add(new JLabel("Disciplina:"));
        disciplinaComboBox = new JComboBox<>();
        formPanel.add(disciplinaComboBox);

        formPanel.add(new JLabel("Valor:"));
        valorField = new JTextField();
        formPanel.add(valorField);

        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(salvarButton);
        buttonPanel.add(novoButton);
        buttonPanel.add(excluirButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Lista de notas
        notaListModel = new DefaultListModel<>();
        notaList = new JList<>(notaListModel);
        JScrollPane scrollPane = new JScrollPane(notaList);
        add(scrollPane, BorderLayout.CENTER);

        carregarAlunosDisciplinas();
        carregarNotas();
        adicionarListeners();
    }

    private void adicionarListeners() {
        salvarButton.addActionListener(e -> salvarNota());
        novoButton.addActionListener(e -> limparCampos());
        excluirButton.addActionListener(e -> excluirNota());
        notaList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Nota notaSelecionada = notaList.getSelectedValue();
                if (notaSelecionada != null) {
                   // Selecionar aluno e disciplina nos comboboxes
                    for (int i = 0; i < alunoComboBox.getItemCount(); i++) {
                        if (alunoComboBox.getItemAt(i).getMatricula().equals(notaSelecionada.getMatriculaAluno())) {
                            alunoComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < disciplinaComboBox.getItemCount(); i++) {
                        if (disciplinaComboBox.getItemAt(i).getCodigo().equals(notaSelecionada.getCodigoDisciplina())) {
                            disciplinaComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    valorField.setText(String.valueOf(notaSelecionada.getValor()));
                }
            }
        });
    }

    private void carregarAlunosDisciplinas() {
        alunoComboBox.removeAllItems();
        JsonManager.carregarAlunos().forEach(alunoComboBox::addItem);

        disciplinaComboBox.removeAllItems();
        JsonManager.carregarDisciplinas().forEach(disciplinaComboBox::addItem);
    }

    private void carregarNotas() {
        notaListModel.clear();
        List<Nota> notas = JsonManager.carregarNotas();
        for (Nota nota : notas) {
            notaListModel.addElement(nota);
        }
    }

    private void salvarNota() {
        Aluno aluno = (Aluno) alunoComboBox.getSelectedItem();
        Disciplina disciplina = (Disciplina) disciplinaComboBox.getSelectedItem();
        String valorStr = valorField.getText().trim();

        if (!validarCampos(aluno, disciplina, valorStr)) {
            return;
        }

        double valor = Double.parseDouble(valorStr);

        List<Nota> notas = JsonManager.carregarNotas();
        boolean notaExiste = false;
        for (int i = 0; i < notas.size(); i++) {
            if (notas.get(i).getMatriculaAluno().equals(aluno.getMatricula()) &&
                notas.get(i).getCodigoDisciplina().equals(disciplina.getCodigo())) {
                notas.set(i, new Nota(aluno.getMatricula(), disciplina.getCodigo(), valor)); // Atualiza
                notaExiste = true;
                break;
            }
        }

        if (!notaExiste) {
            notas.add(new Nota(aluno.getMatricula(), disciplina.getCodigo(), valor)); // Cria
        }

        JsonManager.salvarNotas(notas);
        carregarNotas();
        limparCampos();
        JOptionPane.showMessageDialog(this, "Nota salva com sucesso!");
    }

    private void excluirNota() {
        Nota notaSelecionada = notaList.getSelectedValue();
        if (notaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma nota para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta nota?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            List<Nota> notas = JsonManager.carregarNotas();
            notas.removeIf(nota -> nota.getMatriculaAluno().equals(notaSelecionada.getMatriculaAluno()) &&
                                   nota.getCodigoDisciplina().equals(notaSelecionada.getCodigoDisciplina()));
            JsonManager.salvarNotas(notas);
            carregarNotas();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Nota excluída com sucesso!");
        }
    }

    private void limparCampos() {
        alunoComboBox.setSelectedIndex(-1);
        disciplinaComboBox.setSelectedIndex(-1);
        valorField.setText("");
        notaList.clearSelection();
    }

    private boolean validarCampos(Aluno aluno, Disciplina disciplina, String valorStr) {
        if (aluno == null || disciplina == null || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            if (valor < 0 || valor > 10) {
                JOptionPane.showMessageDialog(this, "O valor da nota deve ser entre 0 e 10.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor da nota inválido. Use um número.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}


