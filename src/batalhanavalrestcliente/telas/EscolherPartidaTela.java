package batalhanavalrestcliente.telas;

import batalhanavalrestcliente.BatalhaNavalRESTCliente;
import batalhanavalrestcliente.rede.Comunicador;
import batalhanavalrestcliente.util.PartidaLista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class EscolherPartidaTela {
    
    private TableView<PartidaLista> table;
    
    public void iniciarTela(String partidas) {
        ObservableList<PartidaLista> partidasLista = FXCollections.observableArrayList();
        
        PartidaLista.decodificarPartidas(partidas).stream().forEach((partida) -> {
            partidasLista.add(partida);
        });
        
        BorderPane root;
        
        final Label label = new Label("Partidas disponíveis");
        label.setFont(new Font("Arial", 20));
        label.setAlignment(Pos.CENTER);
        
        table = new TableView<>();
        table.setEditable(false);
        
        TableColumn nomeColuna = new TableColumn("Nome");
        nomeColuna.setCellValueFactory(
                new PropertyValueFactory<>("nome"));
        nomeColuna.setMinWidth(50);
        
        TableColumn idColuna = new TableColumn("Id");
        idColuna.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        idColuna.setMinWidth(50);
 
        table.setItems(partidasLista);
        table.getColumns().addAll(idColuna, nomeColuna);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        final VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        vbox.setAlignment(Pos.CENTER);
        
        final HBox hbox = new HBox(vbox);
        hbox.setAlignment(Pos.CENTER);
        
        root = new BorderPane(hbox);
        
        Button voltarBotao = new Button("Voltar");
        voltarBotao.setOnAction(event -> {
            new EscolhaTela().iniciarTela();
        });
        
        Button entrarBotao = new Button("Entrar");
        entrarBotao.setOnAction(event -> {
            PartidaLista partidaSelecionada = table.getSelectionModel().getSelectedItem();
            
            if (partidaSelecionada != null) {
                String[] token = Comunicador.enviarRequestPost("/partidas/" + partidaSelecionada.getId() + "/conectar").split(";");
                if (token.equals("")) {
                    BatalhaNavalRESTCliente.enviarMensagemErro("Partida cheia, escolha outra");
                } else {
                    new PreparacaoTela().iniciarTela(token[1], Integer.parseInt(token[0]));
                }
            } else {
                BatalhaNavalRESTCliente.enviarMensagemErro("Escolha uma partida");
            }
        });
        
        final HBox hbox2 = new HBox(voltarBotao, entrarBotao);
        hbox2.setSpacing(10);
        hbox2.setPadding(new Insets(10));
        
        root.setBottom(hbox2);
        
        BatalhaNavalRESTCliente.fxContainer.setScene(new Scene(root));
    }
}
