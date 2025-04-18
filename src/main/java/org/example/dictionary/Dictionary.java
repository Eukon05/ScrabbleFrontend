package org.example.dictionary;

import java.io.*;
import java.net.Socket;

public class Dictionary {
    private Dictionary(){}

    public static boolean checkWord(String word) throws IOException {
        try(Socket socket = new Socket("localhost", 8082)){
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.println("GET /doesexist/".concat(word));
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String result = in.readLine();
            System.out.println(result);

            return result.contains("200");
        }
    }
}
