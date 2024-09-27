package br.com.arthub.ah_rest_email.service.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class EmailTemplateUtils {
    public String generateEmailContent(String templatePath, Map<String, String> values) throws IOException {
        StringBuilder emailContent = new StringBuilder();
        
        try (InputStream inputStream = getClass().getResourceAsStream(templatePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                // Substitui as chaves {{key}} pelos valores correspondentes
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    String replacement = entry.getValue() != null ? entry.getValue() : "";
                    line = line.replace("{{ " + entry.getKey() + " }}", replacement);
                }
                emailContent.append(line).append("\n");
            }
        }
        
        return emailContent.toString();
    }
}
