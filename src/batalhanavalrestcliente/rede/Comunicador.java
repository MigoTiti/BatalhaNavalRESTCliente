package batalhanavalrestcliente.rede;

import batalhanavalrestcliente.BatalhaNavalRESTCliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Comunicador {

    public static String ip;

    public static String enviarRequestGet(String mensagemString) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://" + ip + ":12345" + mensagemString);

            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            String resposta = result.toString();

            return resposta;
        } catch (IOException | UnsupportedOperationException ex) {
            Platform.runLater(() -> {
                BatalhaNavalRESTCliente.exibirException(ex);
            });
        }

        return null;
    }

    public static String enviarRequestPost(String mensagemString, String... parametros) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost("http://" + ip + ":12345" + mensagemString);

            if (parametros != null && parametros.length >= 2 && parametros.length % 2 == 0) {
                List<NameValuePair> pares = new ArrayList<>();
                
                if (parametros.length > 2)
                    for (int i = 0; i < parametros.length - 1; i += 2)
                        pares.add(new BasicNameValuePair(parametros[i], parametros[i + 1]));
                else 
                    pares.add(new BasicNameValuePair(parametros[0], parametros[1]));

                request.setEntity(new UrlEncodedFormEntity(pares));
            }

            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            String resposta = result.toString();

            return resposta;
        } catch (IOException | UnsupportedOperationException ex) {
            Platform.runLater(() -> {
                BatalhaNavalRESTCliente.exibirException(ex);
            });
        }

        return null;
    }
}
