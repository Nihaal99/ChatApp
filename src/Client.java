import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    //Client socket
    Socket socket;

    BufferedReader br;
    PrintWriter writer;
    public Client(){
        try {
            //Client will send request on this ip address on port number specified on server class
            socket=new Socket("127.0.0.1",7777);
            System.out.println("Connection from client to server done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer=new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void startReading(){
        System.out.println("reader started");
        //thread for reading data
        Runnable r1=()->{
            System.out.println("reader started...");
            while(!socket.isClosed()) {
                try {
                    String message = br.readLine();

                    //for closing reader by client with exit keyword
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    //To print the message by client
                    System.out.println("Server send the message: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        System.out.println("writer started");
        //thread for writing data
        Runnable r2=()->{
            while(!socket.isClosed()){
                try{
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    writer.println(content);
                    writer.flush();
                    if (content.equalsIgnoreCase("exit")) {
                        System.out.println("Client class writer closed");
                        writer.close();
                        break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("this is client");
        new Client();
    }
}
