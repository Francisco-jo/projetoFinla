package com.escola.notas.view;

import com.escola.notas.model.Aluno;
import com.escola.notas.util.JsonManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class AlunoFormPanel extends JPanel {
    private JTextField matriculaField;
    private JTextField nomeField;
    private JTextField dataNascimentoField;
    private JButton salvarButton;
    private JButton novoButton;
    private JButton excluirButton;
    private JList<Aluno> alunoList;
    private DefaultListModel<Aluno> alunoListModel;

    public AlunoFormPanel() {
        setLayout(new BorderLayout());

        // Formulário de entrada
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Aluno"));

        formPanel.add(new JLabel("Matrícula:"));
        matriculaField = new JTextField();
        formPanel.add(matriculaField);

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Data de Nascimento (DD/MM/AAAA):"));
        dataNascimentoField = new JTextField();
        formPanel.add(dataNascimentoField);

        salvarButton = new JButton("Salvar");
        novoButton = new JButton("Novo");
        excluirButton = new JButton("Excluir");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(salvarButton);
        buttonPanel.add(novoButton);
        buttonPanel.add(excluirButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Lista de alunos
        alunoListModel = new DefaultListModel<>();
        alunoList = new JList<>(alunoListModel);
        JScrollPane scrollPane = new JScrollPane(alunoList);
        add(scrollPane, BorderLayout.CENTER);

        carregarAlunos();
        adicionarListeners();
    }

    private void adicionarListeners() {
        salvarButton.addActionListener(e -> salvarAluno());
        novoButton.addActionListener(e -> limparCampos());
        excluirButton.addActionListener(e -> excluirAluno());
        alunoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Aluno alunoSelecionado = alunoList.getSelectedValue();
                if (alunoSelecionado != null) {
                    matriculaField.setText(alunoSelecionado.getMatricula());
                    nomeField.setText(alunoSelecionado.getNome());
                    dataNascimentoField.setText(alunoSelecionado.getDataNascimento());
                }
            }
        });
    }

    private void carregarAlunos() {
        alunoListModel.clear();
        List<Aluno> alunos = JsonManager.carregarAlunos();
        for (Aluno aluno : alunos) {
            alunoListModel.addElement(aluno);
        }
    }

    private void salvarAluno() {
        String matricula = matriculaField.getText().trim();
        String nome = nomeField.getText().trim();
        String dataNascimento = dataNascimentoField.getText().trim();

        if (!validarCampos(matricula, nome, dataNascimento)) {
            return;
        }

        List<Aluno> alunos = JsonManager.carregarAlunos();
        boolean alunoExiste = false;
        for (int i = 0; i < alunos.size(); i++) {
            if (alunos.get(i).getMatricula().equals(matricula)) {
                alunos.set(i, new Aluno(matricula, nome, dataNascimento)); // Atualiza
                alunoExiste = true;
                break;
            }
        }

        if (!alunoExiste) {
            alunos.add(new Aluno(matricula, nome, dataNascimento)); // Cria
        }

        JsonManager.salvarAlunos(alunos);
        carregarAlunos();
        limparCampos();
        JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!");
    }

    private void excluirAluno() {
        Aluno alunoSelecionado = alunoList.getSelectedValue();
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o aluno " + alunoSelecionado.getNome() + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            List<Aluno> alunos = JsonManager.carregarAlunos();
            alunos.removeIf(aluno -> aluno.getMatricula().equals(alunoSelecionado.getMatricula()));
            JsonManager.salvarAlunos(alunos);
            carregarAlunos();
            limparCampos();
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
        }
    }

    private void limparCampos() {
        matriculaField.setText("");
        nomeField.setText("");
        dataNascimentoField.setText("");
        alunoList.clearSelection();
    }

    private boolean validarCampos(String matricula, String nome, String dataNascimento) {
        if (matricula.isEmpty() || nome.isEmpty() || dataNascimento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (matricula.length() > 10) {
            JOptionPane.showMessageDialog(this, "Matrícula deve ter no máximo 10 caracteres.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validação de formato de data DD/MM/AAAA
        String regexData = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
        if (!Pattern.matches(regexData, dataNascimento)) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use DD/MM/AAAA.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}


