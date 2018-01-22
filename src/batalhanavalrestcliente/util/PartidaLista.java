package batalhanavalrestcliente.util;

import java.util.ArrayList;
import java.util.List;

public class PartidaLista {
    
    public static final int ESTADO_NAO_ENTROU = 0;
    public static final int ESTADO_PREPARANDO = 1;
    public static final int ESTADO_PRONTO = 2;
    public static final int ESTADO_VEZ = 3;
    public static final int ESTADO_VITORIA = 4;
    public static final int ESTADO_DERROTA = 5;
    
    private int id;
    private String nome;

    public PartidaLista(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public PartidaLista() {
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public static List<PartidaLista> decodificarPartidas(String partidas) {
        String[] partidasDivididas = partidas.split("&");
        
        List<PartidaLista> partidasDecodificas = new ArrayList<>();
        
        for (String partidaDividida : partidasDivididas) {
            String[] partida = partidaDividida.split(",");
            partidasDecodificas.add(new PartidaLista(Integer.parseInt(partida[0]), partida[1]));
        }
        
        return partidasDecodificas;
    }
}
