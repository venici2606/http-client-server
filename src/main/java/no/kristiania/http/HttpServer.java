package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public HttpServer(int port) throws IOException {
        //forteller vi java at hver gang en klient connecte seg, så skal de komme til vårt program
        //inngangspunkt til programmet vårt
        ServerSocket serverSocket = new ServerSocket(port);

        //thread executes the code in a separate "thread", that is: in parallel
        new Thread(() -> {
            while (true) { //server venter på klienten og testen selv er klienten
                try {
                    //accept waits for a client to try to connect - blocks
                    Socket clientSocket = serverSocket.accept();
                    handleRequest(clientSocket); //løkke og håndrterer en og en request basert på at vi venter på neste klient
                } catch (IOException e) {
                    //if something went wrong - print out exeception and try again
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleRequest(Socket clientSocket) throws IOException {
        String requestLine = HttpClient.readLine(clientSocket);
        System.out.println(requestLine);

        String requestTarget = requestLine.split(" ")[1]; //splitter request line
        String statusCode = "200";
        String body = "Hello <strong>World</strong>!";

        int questionPos = requestTarget.indexOf('?');
        if (questionPos != -1) {

            QueryString queryString = new QueryString(requestTarget.substring(questionPos+1));
            if (queryString.getParameter("status") != null) {
                /*    int equalPos = queryString.indexOf('=');
                String parameterName = queryString.substring(0, equalPos);
                String parameterValue = queryString.substring(equalPos+1);  */
                statusCode = queryString.getParameter("status");
            }
            if(queryString.getParameter("body") != null) {
                body = queryString.getParameter("body");
            }
        }

        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes());
    }

    public static void main(String[] args) throws IOException {

        new HttpServer(8080);

    }
}
