import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //Server socket
    ServerSocket server;
    //Client socket
    Socket socket;

    BufferedReader br;
    PrintWriter writer;
    public Server(){
        try {
            //Server will listen to this port number for any upcoming client requests
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            socket=server.accept();
            System.out.println("Server is waiting");
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
            try {
            while(!socket.isClosed()) {
                String message = br.readLine();
                //To print the message by client
                System.out.println("Client send the message: " + message);

                //for closing reader by client with exit keyword
                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client terminated the chat");
                    socket.close();
                    break;
                }
            }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        };
        new Thread(r1).start();
    }
    public void startWriting(){
        System.out.println("writer started");
        //thread for writing data
        Runnable r2=()->{
            try {
                while (!socket.isClosed()) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    writer.println(content);
                    writer.flush();
                    if (content.equalsIgnoreCase("exit")) {
                        System.out.println("Server class writer closed");
                        writer.close();
                        break;
                    }
                }
            }catch (Exception e){
                    e.printStackTrace();
                }

        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is server.Starting server");
        new Server();
    }
}
