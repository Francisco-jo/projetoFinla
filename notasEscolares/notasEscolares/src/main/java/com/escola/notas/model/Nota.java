
package com.escola.notas.model;

public class Nota {
    private String matriculaAluno;
    private String codigoDisciplina;
    private double valor;

    public Nota(String matriculaAluno, String codigoDisciplina, double valor) {
        this.matriculaAluno = matriculaAluno;
        this.codigoDisciplina = codigoDisciplina;
        this.valor = valor;
    }

    public String getMatriculaAluno() {
        return matriculaAluno;
    }

    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Aluno: " + matriculaAluno + ", Disciplina: " + codigoDisciplina + ", Nota: " + valor;
    }
}


