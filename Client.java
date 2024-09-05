import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Arial", Font.PLAIN, 18); // Adjust font size

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connected successfully");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGui();
            handleEvents();

            startReading();
            // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // https://youtu.be/4B8IvdA44h4?t=1701
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGui() {
        this.setTitle("Client Messenger [END]"); // Updated title
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color primaryColor = new Color(63, 166, 149); // Greenish-Blue
        Color secondaryColor = new Color(90, 90, 90); // Dark Gray
        Color backgroundColor = new Color(245, 245, 245);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setBackground(backgroundColor);
        heading.setIcon(new ImageIcon("icon.png")); // Replace with your custom icon
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        // Set custom borders for components
        messageInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, secondaryColor), // Bottom border
                new EmptyBorder(10, 15, 10, 15))); // Padding

        // Set layout and add components
        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
        this.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        // Set WhatsApp-like theme colors for components
        this.getContentPane().setBackground(backgroundColor);
        heading.setForeground(primaryColor);
        messageInput.setBackground(backgroundColor);
        messageInput.setForeground(primaryColor);
        messageArea.setForeground(primaryColor);

        this.setVisible(true);
    }

    public void startReading() {
        Runnable r1 = () -> {
            System.out.println("Reader is Started...");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Terminated (server)");
                        JOptionPane.showMessageDialog(this, "Terminated (Server)");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server: " + msg);
                    messageArea.append("Server: " + msg + "\n");
                }
            } catch (IOException e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2 =()->{
            System.out.println("Writer Strated...");
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
                System.out.println("Connection closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("CLIENT");
        new Client();
    }
}

