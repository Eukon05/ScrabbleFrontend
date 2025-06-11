package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.example.model.BestScore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScoresView {

    @FXML
    private TableView<BestScore> table;

    @FXML
    private TableColumn<BestScore, Integer> idColumn;

    @FXML
    private TableColumn<BestScore, String> nickColumn;

    @FXML
    private TableColumn<BestScore, String> dateColumn;

    @FXML
    private TableColumn<BestScore, Integer> scoreColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().gameID()).asObject());
        nickColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nick()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().date()));
        scoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().score()).asObject());

        loadData();
    }


    private void loadData() {
        try {
            URL url = new URL("http://localhost:8000/best");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }

            reader.close();

            // Tu wypisujemy surową odpowiedź JSON w konsoli:
            System.out.println("Response from server: " + response);

            List<BestScore> scores = parseJsonArray(response.toString());

            ObservableList<BestScore> data = FXCollections.observableArrayList(scores);
            table.setItems(data);

        } catch (Exception e) {
           // e.printStackTrace();
        }
    }


    // Bardzo proste i prymitywne parsowanie JSON, tylko dla formatu dokładnie takiego jak w przykładzie
    private List<BestScore> parseJsonArray(String json) {
        List<BestScore> list = new ArrayList<>();

        // Usuń nawiasy kwadratowe na początku i końcu
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        } else {
            return list;  // niepoprawny format
        }

        // Podziel na obiekty JSON oddzielone "},{"
        // Dodaj brakujące nawiasy klamrowe po podziale
        String[] objects = json.split("\\},\\{");

        for (int i = 0; i < objects.length; i++) {
            String obj = objects[i];
            if (i == 0 && !obj.startsWith("{")) obj = "{" + obj;
            if (i == objects.length - 1 && !obj.endsWith("}")) obj = obj + "}";
            if (i > 0 && i < objects.length - 1) obj = "{" + obj + "}";

            // Parsowanie pól w formacie "key":value lub "key":"value"
            int gameID = extractInt(obj, "\"gameID\":");
            String nick = extractString(obj, "\"nick\":\"");
            String date = extractString(obj, "\"date\":\"");
            int score = extractInt(obj, "\"score\":");

            list.add(new BestScore(gameID, nick, date, score));
        }

        return list;
    }

    private int extractInt(String json, String key) {
        try {
            int start = json.indexOf(key);
            if (start == -1) return 0;
            start += key.length();
            int end = json.indexOf(',', start);
            if (end == -1) end = json.indexOf('}', start);
            String number = json.substring(start, end).trim();
            return Integer.parseInt(number);
        } catch (Exception e) {
            return 0;
        }
    }

    private String extractString(String json, String key) {
        try {
            int start = json.indexOf(key);
            if (start == -1) return "";
            start += key.length();
            int end = json.indexOf('"', start);
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }

    @FXML
    public void onClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
