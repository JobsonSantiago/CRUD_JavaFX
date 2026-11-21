import java.time.LocalDate;

public class Aluno {
    private long id;
    private String ra;
    private String nome;
    private int idade;
    private LocalDate nascimento;

    public int getIdade(){
        return idade;
    }

    public void setIdade(int idade){
        this.idade = idade;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return String.format("[%d, %s, %s, %s, %s]",
                this.id, this.ra, this.nome, this.idade, this.nascimento);
    }
}
