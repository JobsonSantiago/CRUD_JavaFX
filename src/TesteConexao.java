
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TesteConexao {
    public static void main(String[] args) {
        System.out.println("Teste de conexao com banco de dados");

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Classe carregada com sucesso");

            Connection con = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/alunos",
                "root",
                ""
            );

            System.out.println("Conectado no banco de dados");

            con.close();

        } catch (ClassNotFoundException e) {
            System.out.println("Classe MariaDB nao encontrada");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados");
            e.printStackTrace();
        }
    }
}
