package br.com.arthub.ah_rest_art.test.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTokenUtil {

    private static final String LOGIN_URL = "http://localhost:8080/useraccount/v1/public/login";

    public String getAuthToken() throws Exception {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", "systemdemo@gmail.com");
        loginPayload.put("password", "demosys");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(loginPayload);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Lê a resposta
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        // Extrai o token da resposta JSON
        Map<String, Object> responseMap = objectMapper.readValue(response.toString(), Map.class);
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        return (String) data.get("token");
    }
}
