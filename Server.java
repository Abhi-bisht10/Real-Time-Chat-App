import java.io.*;
import java.net.*;
public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Server(){
        try {
            server=new ServerSocket(7777);
            System.out.println("Server is ready to accept");
            System.out.println("Wating......");
            socket =server.accept();
            System.out.println("hello");

            br =new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("Reader is Started.....");
            try{
                while(true){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Terminated(client)");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: "+msg);
                }
            }catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2 =()->{
            System.out.println("Writer Strated");
            try{
                while(!socket.isClosed()){
                    BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
                }
            }catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("SERVER");
        new Server();
    }
}
