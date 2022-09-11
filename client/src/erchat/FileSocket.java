package erchat;

import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class FileSocket extends Thread {
    private ChatFrame chatFrame;
    private Socket socket;
    private BufferedWriter bw = null;
    private BufferedReader br = null;
    private String IP = "127.0.0.1";
    private int PORT = 8888;
    private boolean flag = false;
    String request, filepath, filename;
    int type;
    
    public FileSocket(ChatFrame chatFrame, String request, String filepath, String filename, int type) {
        this.chatFrame = chatFrame;
        this.request = request;
        this.filepath = filepath;
        this.filename = filename;
        this.type = type;
        
        try {
            socket = new Socket(IP, PORT);
            System.out.println("[Client] " + socket.getInetAddress() + " has connected.");
            bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            flag = true;            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void run() {
        try {
            while (flag) {
                if (request.equals("SENDFILE")) {
                    bw.write(request + "\n");
                    bw.flush();
                    bw.write(filename + "\n");
                    bw.flush();
                    bw.write(type + "\n");
                    bw.flush();
                    
                    String response = br.readLine();
                    if (response.equals("go")) {
                        sendFile(filepath);
                    }
                    break;
                } else if (request.equals("RECEIVEFILE")) {
                    bw.write(request + "\n");
                    bw.flush();
                    bw.write(filename + "\n");
                    bw.flush();

                    receiveFile(filepath);
                    if (type == 2 && chatFrame.getReload()) {
                        StringTokenizer st = new StringTokenizer(chatFrame.getImageInfo(), "%");
                        boolean me = Boolean.parseBoolean(st.nextToken());
                        boolean group = Boolean.parseBoolean(st.nextToken());
                        String sender = st.nextToken();
                        String fullname = st.nextToken();
                        String file = st.nextToken();
                        long time = Long.parseLong(st.nextToken());
                        
                        ImagePanel imagePanel = new ImagePanel(chatFrame, me, group, sender, fullname, file, time);
                        chatFrame.getMessageModel().addElement(imagePanel);
                        MessageList msgList = new MessageList(type, time, fullname);
                        chatFrame.messageList.add(msgList);
                        chatFrame.setReload(false);
                        chatFrame.getJlsContent().validate();
                        chatFrame.getJlsContent().repaint();
                        chatFrame.getJlsContent().scrollRectToVisible(new Rectangle(0, chatFrame.getJlsContent().getHeight() * 2, 0, 0));
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendFile(String filepath) throws IOException, ClassNotFoundException {
        File file = new File(filepath);
        byte[] buf = new byte[1024];
        OutputStream os = socket.getOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(os, 1024);
        FileInputStream in = new FileInputStream(file);
        System.out.println("[Client] Start to send file... ");
        int i = 0;
        int bytcount = 0;
        while ((i = in.read(buf, 0, 1024)) != -1) {
            //System.out.println(i);
            bytcount = bytcount + i;
            out.write(buf, 0, i);
            out.flush();
        }
        System.out.println("[Client] Sending file finished");
        System.out.println("Bytes Sent : " + bytcount);
        flag = false;
        //socket.shutdownOutput();
        //socket.close();
        out.close();
        in.close();
    }

    public void receiveFile(String filepath) throws IOException {
        File file = new File(filepath);
        byte[] b = new byte[1024];
        int len = 0;
        int bytcount = 0;
        FileOutputStream inFile = new FileOutputStream(file);
        InputStream is = socket.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is, 1024);
        System.out.println("[Client] Start to receive file... ");
        while ((len = bis.read(b, 0, 1024)) != -1) {
            //System.out.println(len);
            bytcount = bytcount + len;
            inFile.write(b, 0, len);
        }
        System.out.println("[Client] Receiving file finished");
        System.out.println("Bytes Writen : " + bytcount);
        flag = false;
        bis.close();
        inFile.close();
    }
}
