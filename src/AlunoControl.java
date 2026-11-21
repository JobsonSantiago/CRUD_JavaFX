import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AlunoControl {
    private ObservableList<Aluno> lista = FXCollections.observableArrayList();
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty ra = new SimpleStringProperty("");
    private StringProperty nome = new SimpleStringProperty("");
    private IntegerProperty idade = new SimpleIntegerProperty(0);
    private ObjectProperty<LocalDate> nascimento = new SimpleObjectProperty();
    private long contadorId = 1;
    private Connection con;

    public AlunoControl() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Classe carregada com sucesso");
            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/alunos",
                    "root",
                    "");
            System.out.println("Conectado no banco de dados");
            pesquisarTodos();
        } catch (ClassNotFoundException e) {
            System.out.println("Classe Maria DB não encontrada");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados");
        }
    }

    public void popularAluno(Aluno a) {
        if (a != null) {
            ra.set(a.getRa());
            nome.set(a.getNome());
            idade.set(a.getIdade());
            nascimento.set(a.getNascimento());
        }
    }

    public void pesquisarTodos() {
        try {
            String sql = "SELECT * FROM alunos";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            lista.clear();
            while (rs.next()) {
                Aluno a = new Aluno();
                a.setRa(rs.getString("ra"));
                a.setId(rs.getLong("id"));
                a.setNome(rs.getString("nome"));
                a.setIdade(rs.getInt("idade"));
                a.setNascimento(rs.getDate("nascimento").toLocalDate());
                lista.add(a);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar no banco de dados");
        }
    }

    public boolean salvar() {
        if (id.get() == 0) {
            try {
                String sql = "INSERT INTO alunos (ra, nome, idade, nascimento) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, ra.get());
                stmt.setString(2, nome.get());
                stmt.setInt(3, idade.get());
                stmt.setDate(4, java.sql.Date.valueOf(nascimento.get()));
                stmt.executeUpdate();
                pesquisarTodos();
            } catch (Exception e) {
                System.out.println("Erro ao inserir no banco de dados");
            }
        } else {
            try {
                String sql = "UPDATE alunos SET ra=?, nome=?, idade=?, nascimento=? WHERE id=?";
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, ra.get());
                stmt.setString(2, nome.get());
                stmt.setInt(3, idade.get());
                stmt.setDate(4, java.sql.Date.valueOf(nascimento.get()));
                stmt.setLong(5, id.get());
                stmt.executeUpdate();
                pesquisarTodos();
            } catch (SQLException e) {
                System.out.println("Erro ao inserir no banco de dados");
            }
        }
        limparCampos();
        return true;
    }

    public void limparCampos() {
        id.set(0);
        ra.set("");
        nome.set("");
        idade.set(0);
        nascimento.set(null);
    }

    public void excluir(Aluno a) {
        try {
            String sql = "DELETE FROM alunos WHERE id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, a.getId());
            stmt.executeUpdate();
            pesquisarTodos();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir no banco de dados");
        }
    }

    public void atualizar(Aluno a) {
        id.set(a.getId());
        ra.set(a.getRa());
        nome.set(a.getNome());
        idade.set(a.getIdade());
        nascimento.set(a.getNascimento());
    }

    public void pesquisar(Aluno a) {
        try {
            String sql = "SELECT * FROM alunos WHERE ra = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, a.getRa());

            ResultSet rs = stmt.executeQuery();

            lista.clear();

            while (rs.next()) {
                Aluno aluno = new Aluno();

                aluno.setId(rs.getLong("id"));
                aluno.setRa(rs.getString("ra"));
                aluno.setNome(rs.getString("nome"));
                aluno.setIdade(rs.getInt("idade"));
                aluno.setNascimento(
                        rs.getDate("nascimento").toLocalDate());

                lista.add(aluno);
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar no banco de dados: " + e.getMessage());
        }
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty raProperty() {
        return ra;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public IntegerProperty idadeProperty() {
        return idade;
    }

    public ObjectProperty<LocalDate> nascimentoProperty() {
        return nascimento;
    }

    public ObservableList<Aluno> getLista() {
        return lista;
    }

}
