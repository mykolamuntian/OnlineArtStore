package com.example.onlineartstore.service;

import com.google.gson.JsonParser;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@Primary
public class DatTranLKImageUploadServiceImpl implements ImageUploadService {
// Використовую API Hub by DatTranLK на сайті https://rapidapi.com/
    private static final String URL = "https://upload-image-and-return-url-by-thichthicodeteam.p.rapidapi.com/api/upload-image";
    private static final String ApiKey = "d9230bf2fdmshfb83e030f1a1747p1cb40ajsn924babb803ac";
    private static final String host = "upload-image-and-return-url-by-thichthicodeteam.p.rapidapi.com";


    @Override
    public String uploadImage(MultipartFile file) {
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=---011000010111000001101001");
            connection.setRequestProperty("X-RapidAPI-Key", ApiKey);
            connection.setRequestProperty("X-RapidAPI-Host", host);
            connection.setDoOutput(true);

            // Створила тіло request
            String boundary = "---011000010111000001101001";
            String lineEnding = "\r\n";
            String twoHyphens = "--";

            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);

            // Початок multipart/form-data request
            writer.append(twoHyphens).append(boundary).append(lineEnding);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(URLEncoder.encode(Objects.requireNonNull(file.getOriginalFilename()), StandardCharsets.UTF_8.name())).append("\"")
                    .append(lineEnding);
            writer.append("Content-Type: ").append(file.getContentType()).append(lineEnding);
            writer.append(lineEnding);
            writer.flush();

            // Пишу вміст файлу
            try (InputStream fileInputStream = file.getInputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            // Закінчення multipart/form-data request
            writer.append(lineEnding).append(twoHyphens).append(boundary).append(twoHyphens).append(lineEnding);
            writer.flush();

            // Читаю response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();
                String link;
                link = JsonParser.parseString(response.toString()).getAsJsonObject().get("link").getAsString();
                return link;
            } else {
                // Обробка response на помилку
                System.out.println("Error: " + responseCode);
                connection.disconnect();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
