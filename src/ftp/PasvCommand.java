package ftp;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PasvCommand implements Command {
    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        System.out.println("excute the Pasv Command......");
        String response = "";
        try {
            int tempport = -1;
            ServerSocket serverSocket = null;
            while (serverSocket == null) {
                tempport = (int) (Math.random() * 100000) % 9999 + 1024;
                serverSocket = getDataServerSocket(tempport);
                
            }
            if (tempport != -1 && serverSocket != null) {
                int p1 = tempport / 256;
                int p2 = tempport % 256;
                		

               InetAddress myHost= InetAddress.getLocalHost();
               String myIp = String.valueOf(myHost.getHostAddress());
   	        String myIpSplit[] = myIp.split("\\.");
                response = "2271 Entering Passive Mode ("+myIpSplit[0]+","+myIpSplit[1]+","+myIpSplit[2]+","+myIpSplit[3]+","+ p1 + "," + p2 + ")";
                //response = "2271 Entering Passive Mode (127,0,0,1," + p1 + "," + p2 + ")";
                System.out.println(response);
                
            }

            writer.write(response);
            writer.write("\r\n");
            writer.flush();
            System.out.println("set PASV successful");
//////////
            //Socket socket=t.getSocket();
            t.setDataSocket(serverSocket.accept());
            response="Data connection - Passive Mode - established";
            System.out.println(response);
            writer.write(response);
            writer.write("\t\n");
            writer.flush();

           // t.setDataSocket(serverSocket.accept());
           // ControllerThread dataThread = new ControllerThread(socket, datasocket,"data");
           
            //dataThread.start();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    
    public static ServerSocket getDataServerSocket(int port) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return socket;
    }


}
