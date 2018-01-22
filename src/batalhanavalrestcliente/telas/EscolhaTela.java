package batalhanavalrestcliente.telas;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import batalhanavalrestcliente.BatalhaNavalRESTCliente;
import batalhanavalrestcliente.rede.Comunicador;
import javafx.scene.Scene;

public class EscolhaTela {
   
    public void iniciarTela() {
        Button btnEntrar = new Button("Entrar em partida");
        btnEntrar.setOnAction((ActionEvent event) -> {
            new Thread(() -> entrarEmPartida()).start();
        });

        Button btnCriar = new Button("Criar partida");
        btnCriar.setOnAction((ActionEvent event) -> {
            criarPartida();
        });

        VBox vBox = new VBox(btnEntrar, btnCriar);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        StackPane root = new StackPane(vBox);

        BatalhaNavalRESTCliente.fxContainer.setScene(new Scene(root));
    }

    private void entrarEmPartida() {
        String partidas = Comunicador.enviarRequestGet("/partidas/");
        new EscolherPartidaTela().iniciarTela(partidas);
    }

    private void criarPartida() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Criar partida");
        dialog.setResizable(true);

        Label label1 = new Label("Nome: ");
        TextField text1 = new TextField();

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Criar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(buttonTypeOk);
        okButton.setDisable(true);

        text1.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().length() < 1);
        });

        dialog.setResultConverter((ButtonType b) -> {
            if (b == buttonTypeOk) {
                return text1.getText();
            }

            return null;
        });

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String[] token = Comunicador.enviarRequestPost("/partidas/criar", "nome", result.get()).split(";");
            new PreparacaoTela().iniciarTela(token[1], Integer.parseInt(token[0]));
        }
    }
}
