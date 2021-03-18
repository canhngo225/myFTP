package ftp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ftp_server  {
	ServerSocket serverSocket;
	private int port;
	public static void main(String[] args) throws IOException {
			ftp_server ftpServer = new ftp_server(21);
			ftpServer.listen();
	}
	public ftp_server(int port) throws IOException {
		serverSocket =new ServerSocket(port);
		//Khởi động thông tin 
		Share.init();
	}
	public void listen() throws IOException {	
			Socket socket=null;
			while(true) {
				socket= serverSocket.accept();
				ControllerThread thread = new ControllerThread(socket);
				thread.start();
			}
	}
	
}