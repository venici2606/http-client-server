package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private final int statusCode;
    private final Map<String, String> responseHeaders = new HashMap<>();
    private String responseBody;

    public HttpClient(final String hostname, int port, final String requestTarget) throws IOException {
        Socket socket = new Socket(hostname, port); //connecte til en server vha. socket standard port 80

        String request = "GET " + requestTarget + " HTTP/1.1\r\n" + // samme som nettleseren sender; minste man må ha med er request line og en header (host)
                "Host: " + hostname + "\r\n" +
                "\r\n"; // kjører rewuest (bygger)

        socket.getOutputStream().write(request.getBytes()); //skriver til rerver

        //String line = readLine(socket); // lagt til ved refaktor - extract method leser svaret
       // System.out.println(line); // skriver ut første linje

        String responseLine = readLine(socket);
        String[] responseLineParts = responseLine.split(" "); // streng med flere biter splitter

        statusCode = Integer.parseInt(responseLineParts[1]); //gir statuskoden

        String headerLine;
        while (!(headerLine = readLine(socket)).isEmpty()) {
            int colonPos = headerLine.indexOf(':');
            String headerName = headerLine.substring(0, colonPos); // (0, colonPos) starten, frem til
            String headerValue = headerLine.substring(colonPos+1).trim(); // fjerner space foran og bak
            responseHeaders.put(headerName, headerValue); // legge til i map
        }

        int contentLength = Integer.parseInt(getResponseHeader("Content-Length"));
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            body.append((char)socket.getInputStream().read());
        }
        responseBody = body.toString();

    }

    public static String readLine(Socket socket) throws IOException {
        StringBuilder line = new StringBuilder(); // tar vare på karakterer
        int c ; // få plass til alle karakter og -1
        while ((c = socket.getInputStream().read()) != -1) { // read returnerer en int -1 er kun gyldig som karakter
            if (c == '\r') {
                socket.getInputStream().read(); // read and ignore the following \n
                break;
            }
            line.append((char)c); //legger dem inn
        }
        return line.toString();
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80, "/echo?status=200&Content-Type=text%2Fhtml&body=Hei%20Kristiania");
        System.out.println(client.getResponseBody());
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseHeader(String headerName) {
        return responseHeaders.get(headerName);
    }

    public String getResponseBody() {
        return responseBody;
    }
}

