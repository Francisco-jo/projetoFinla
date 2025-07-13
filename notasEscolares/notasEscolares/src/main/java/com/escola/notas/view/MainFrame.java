package com.escola.notas.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("Sistema de Notas Escolares");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        JMenuBar menuBar = new JMenuBar();
        JMenu cadastrosMenu = new JMenu("Cadastros");
        JMenuItem alunoMenuItem = new JMenuItem("Aluno");
        JMenuItem disciplinaMenuItem = new JMenuItem("Disciplina");
        JMenuItem notaMenuItem = new JMenuItem("Nota");

        cadastrosMenu.add(alunoMenuItem);
        cadastrosMenu.add(disciplinaMenuItem);
        cadastrosMenu.add(notaMenuItem);
        menuBar.add(cadastrosMenu);

        JMenu consultasMenu = new JMenu("Consultas");
        JMenuItem relatorioMenuItem = new JMenuItem("Relatório de Notas");
        consultasMenu.add(relatorioMenuItem);
        menuBar.add(consultasMenu);

        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new AlunoFormPanel(), "Aluno");
        mainPanel.add(new DisciplinaFormPanel(), "Disciplina");
        mainPanel.add(new NotaFormPanel(), "Nota");
        mainPanel.add(new RelatorioNotasPanel(), "Relatorio");

        add(mainPanel);

        // Adiciona listeners para os itens de menu
        alunoMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Aluno"));
        disciplinaMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Disciplina"));
        notaMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Nota"));
        relatorioMenuItem.addActionListener(e -> cardLayout.show(mainPanel, "Relatorio"));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}


