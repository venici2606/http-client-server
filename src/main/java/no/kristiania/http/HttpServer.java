package no.kristiania.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private File contentRoot; // property

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
        } else { //hvis den ikke finner ? så skal den lete i disk
            File file = new File(contentRoot, requestTarget);
            statusCode = "200";
            String contentType = "text/plain"; //default

            if(file.getName().endsWith(".html")){
                contentType = "text/html";
            }
            String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                    "Content-Length: " + file.length() + "\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "\r\n";

            //skrive 200, lengden på filen, og skrive ut dette pog overføre til klienten
            clientSocket.getOutputStream().write(response.getBytes());

            new FileInputStream(file).transferTo(clientSocket.getOutputStream());

        }

        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                body;

        clientSocket.getOutputStream().write(response.getBytes());
    }

    public static void main(String[] args) throws IOException {

        HttpServer server = new HttpServer(8080);
        server.setContentRoot(new File("src/main/resources")); //katalogen, der i java prosjekt der settes inn filer som ikke er java fil

    }

    public void setContentRoot(File contentRoot) {
        this.contentRoot = contentRoot;
    }
}
