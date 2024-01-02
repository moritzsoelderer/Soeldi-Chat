package soeldichat.soeldichat;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScServerConnection extends Thread{
    private SoeldiChatApplication application;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public boolean connect(){
        String hostname = "DESKTOP-KTQHO9R";
        int port = 8080;

        try {
            this.socket = new Socket(hostname, port);

            InputStream input = socket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(input));

            OutputStream outputStream = socket.getOutputStream();
            this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);


        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    @Override
    public void run(){

        try {
                while (true){
                    String inputMessage = this.bufferedReader.readLine();
                    if(!inputMessage.isEmpty()){
                        System.out.println("Message: " + inputMessage);
                        Gson gson = new Gson();
                        Message newMessage = gson.fromJson(inputMessage, Message.class);
                        application.addMessageToChat(newMessage);
                    }

                }

        } catch (UnknownHostException ex) {

            application.addErrorMessage("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            application.addErrorMessage("I/O error: " + ex.getMessage());

        }

    }

    public void sendMessage(Message message){
        Gson gson = new Gson();
        String messageString = gson.toJson(message, Message.class);

        try{
            this.printWriter.println(messageString);
        }
        catch (Exception exception){
            application.addErrorMessage("could not send message to server");
        }
    }

    public void setApplication(SoeldiChatApplication soeldiChatApplication) {
        this.application = soeldiChatApplication;
    }

}
