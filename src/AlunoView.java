import java.time.LocalDate;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class AlunoView extends Application {
    private AlunoControl control = new AlunoControl();
    private TableView<Aluno> table = new TableView<>();

    @Override
    public void start(Stage stage) {
        BorderPane panPrincipal = new BorderPane();
        GridPane p = new GridPane();
        Scene scn = new Scene(panPrincipal, 600, 400);
        stage.setScene(scn);

        p.add(new Label("Id: "), 0, 0);
        p.add(new Label("RA: "), 0, 1);
        p.add(new Label("Nome e Sobrenome: "), 0, 2);
        p.add(new Label("Idade"), 0, 3);
        p.add(new Label("Nascimento: "), 0, 4);

        TextField txtId = new TextField();
        txtId.setEditable(false);

        TextField txtRa = new TextField();
        TextField txtNome = new TextField();
        TextField txtIdade = new TextField();
        TextField txtNascimento = new TextField();

        p.add(txtId, 1, 0);
        p.add(txtRa, 1, 1);
        p.add(txtNome, 1, 2);
        p.add(txtIdade, 1, 3);
        p.add(txtNascimento, 1, 4);

        TableColumn<Aluno, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(
                item -> new ReadOnlyLongWrapper(item.getValue().getId()).asObject());

        TableColumn<Aluno, String> colRa = new TableColumn<>("RA");
        colRa.setCellValueFactory(
                item -> new ReadOnlyStringWrapper(item.getValue().getRa()));

        TableColumn<Aluno, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                item -> new ReadOnlyStringWrapper(item.getValue().getNome()));

        TableColumn<Aluno, Integer> colIdade = new TableColumn<>("Idade");
        colIdade.setCellValueFactory(
                item -> new ReadOnlyIntegerWrapper(item.getValue().getIdade()).asObject());

        TableColumn<Aluno, LocalDate> colNascimento = new TableColumn<>("Nascimento");
        colNascimento.setCellValueFactory(
                item -> new ReadOnlyObjectWrapper<>(item.getValue().getNascimento()));

        TableColumn<Aluno, Void> colAcoes = new TableColumn<>("Ações");
        Callback<TableColumn<Aluno, Void>, TableCell<Aluno, Void>> callback = new Callback<>() {
            public TableCell<Aluno, Void> call(TableColumn<Aluno, Void> tc) {
                TableCell<Aluno, Void> tableCell = new TableCell<>() {

                    private final Button btnExcluir = new Button("Excluir");
                    private final Button btnAtualizar = new Button("Atualizar");
                    private final HBox painelBotoes = new HBox();
                    {
                        painelBotoes.getChildren().addAll(btnAtualizar, btnExcluir);
                        btnExcluir.setOnAction(e -> {
                            System.out.printf("Aluno %s excluido\n", getTableRow().getItem());
                            control.excluir(getTableRow().getItem());
                        });
                        btnAtualizar.setOnAction(e -> {
                            System.out.printf("Aluno %s atualizado\n", getTableRow().getItem());
                            control.atualizar(getTableRow().getItem());
                        });
                    }

                    public void updateItem(Void valor, boolean empty) {
                        if (!empty) {
                            setGraphic(painelBotoes);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
                return tableCell;
            }
        };
        colAcoes.setCellFactory(callback);
        table.getColumns().addAll(colId, colRa, colNome, colIdade, colNascimento, colAcoes);
        table.setItems(control.getLista());

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, antigo, novo) -> {
                            control.popularAluno(novo);
                        });

        Bindings.bindBidirectional(txtId.textProperty(), control.idProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtRa.textProperty(), control.raProperty());
        Bindings.bindBidirectional(txtNome.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtIdade.textProperty(), control.idadeProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(
                txtNascimento.textProperty(),
                control.nascimentoProperty(),
                new javafx.util.converter.LocalDateStringConverter());

        Button btnSalvar = new Button("Salvar");
        Button btnPesquisar = new Button("Pesquisar");
        p.add(btnSalvar, 0, 5);
        p.add(btnPesquisar, 1, 5);

        btnSalvar.setOnAction(e -> {
            if (control.salvar()) {
                new Alert(AlertType.INFORMATION,
                        "Aluno cadastrado com sucesso", ButtonType.OK)
                        .showAndWait();

                System.out.printf("Aluno cadastrado com sucesso!\n");
            }
        });

        panPrincipal.setTop(p);
        panPrincipal.setCenter(table);

        btnPesquisar.setOnAction(e -> {
            Aluno a = new Aluno();
            a.setRa(control.raProperty().get());
            control.pesquisar(a);
        });
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(AlunoView.class, args);
    }
}