package ftpClient;

import javax.swing.*;

import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class Ftp_client {

    private BufferedReader controlReader;
    private PrintWriter controlOut;

    private String ftpusername;
    private String ftppassword;

    private String url;
    private static final int PORT = 21;
    private static final int DATAPORT = 20;

    public static boolean isLogined=false;
    public static boolean isPassMode=false;
	private Socket socket,dataSocket;
	private ServerSocket dataSocketServer;
	private String passHost;
	private int passPort;
    

    public Ftp_client(String url, String username, String password) {
        try {
            socket = new Socket(url, PORT);
           
            setUsername(username);
            setPassword(password);
            setUrl(url);
            controlReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            controlOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            initftp();  
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void initftp() throws Exception {
        String msg;
        do {
            msg = controlReader.readLine();
            System.out.println(msg);
        } while (!msg.startsWith("220 "));

        controlOut.println("USER " + ftpusername);
        
        String response = controlReader.readLine();
        System.out.println(response);

        if (!response.startsWith("331 ")) {
            JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
            throw new IOException("SimpleFTP received an unknown response after sending the user: " + response);

        }

        controlOut.println("PASS " + ftppassword);

        response = controlReader.readLine();
        System.out.println(response);
        if (!response.startsWith("230 ")) {
            JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
           throw new IOException("SimpleFTP was unable to log in with the supplied password: "+ response);
        }

        isLogined=true;
    }

    private void setUsername(String username) {
        this.ftpusername = username;
    }

    private void setPassword(String password) {
        this.ftppassword = password;
    }
    private void setUrl(String url) {
    	this.url=url;
    }
    
    public FTPFile[] getAllFile() throws Exception {
        String response;
        // Send LIST command
        controlOut.println("LIST");

        // Read command response
        response = controlReader.readLine();
        System.out.println(response);


        // Read data from server
        Vector<FTPFile> tempfiles = new Vector<>();

        String line = null;
        while ((line = controlReader.readLine()) != null) {
            if(line.equals("end of files"))
                break;
            if(line.equals("150 Opening data connection for directory list...")) {
            	System.out.println(line);
            	line = controlReader.readLine();
            }
            System.out.println(line);
            FTPFile temp = new FTPFile();
            setFtpFileInfo(temp, line);
            tempfiles.add(temp);
        }

        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        FTPFile[] files = new FTPFile[tempfiles.size()];
        tempfiles.copyInto(files);

        return files;

    }

    
    private void setFtpFileInfo(FTPFile in, String info) {
        String infos[] = info.split(" ");
        Vector<String> vinfos = new Vector<>();
        for (int i = 0; i < infos.length; i++) {
            if (!infos[i].equals(""))
            {
                vinfos.add(infos[i]);
            }
        }
        
        String name="";
        if(vinfos.size()>5) {
        	for(int i=5;i<vinfos.size();i++)
        	{
        	name+=vinfos.get(i);
        	if(!(i==vinfos.size()-1))name+=" ";
        	}
        }
        //in.setName(vinfos.get(5));
        in.setName(name);
        in.setSize(Integer.parseInt(vinfos.get(4)));
        String type=info.substring(0,1);
        if(type.equals("d"))
        {
            in.setType(1);
        }else
        {
            in.setType(0);
        }

    }


    
    public void upload(String File_path) throws Exception {
       
        System.out.print("File Path :" + File_path+"\n");
        File f = new File(File_path);
        if (!f.exists()) {
            System.out.println("File not Exists...");
            return;
        }
        FileInputStream is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        //-----------------------------------------------

        // Send PORT command
        
        String url =InetAddress.getLocalHost().getHostAddress();
       // InetAddress myHost= InetAddress.getLocalHost();
       // String myIp = String.valueOf(myHost.getHostAddress());
        
        //Math.random() is between 0 and 1
        //int dataport=(int)(Math.random()*100000%9999)+1024;
        int dataport=DATAPORT;
        
        String portCommand="MYPORT "+ url+","+dataport;
        controlOut.println(portCommand);
        dataSocketServer = new ServerSocket(dataport);
        dataSocket=dataSocketServer.accept();
        String response;
        response=controlReader.readLine();
        System.out.println(response);


        // Send command STOR
        controlOut.println("STOR " + f.getName());

        
        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        input.close();
        output.close();
        dataSocket.close();


        response = controlReader.readLine();
        System.out.println(response);
        dataSocketServer.close();
    }
    
    public void uploadPass(String File_path) throws Exception {
    	
    		 
        System.out.print("File Path :" + File_path+"\n");
        File f = new File(File_path);
        if (!f.exists()) {
            System.out.println("File not Exists...");
            return;
        }
        FileInputStream is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        //-----------------------------------------------
        if(checkIsPassiveMode()) {
        String response;
        // Send command STOR
        controlOut.println("STOR " + f.getName());

        
        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        input.close();
        output.close();
        dataSocket.close();


        response = controlReader.readLine();
        System.out.println(response);
        //dataSocketServer.close();
    	 }
    	 }



    
    public void download(String from_file_name, String to_path) throws Exception {
        // Send PORT command
        
    	String url = InetAddress.getLocalHost().getHostAddress();
        
    	//int dataport=(int)(Math.random()*100000%9999)+1024;
    	int dataport=DATAPORT;
        //active
        String portCommand="MYPORT "+ url+","+dataport;
        controlOut.println(portCommand);
        dataSocketServer = new ServerSocket(dataport);
        dataSocket=dataSocketServer.accept();
        String response;
        response=controlReader.readLine();
        System.out.println(response);
        //
        //passive
        
        //send RETR command
        controlOut.println("RETR " + from_file_name);

        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream(new File(to_path, from_file_name)));
        BufferedInputStream input = new BufferedInputStream(
                dataSocket.getInputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        output.close();
        input.close();
        dataSocket.close();
        
        response = controlReader.readLine();
        System.out.println(response);

        response = controlReader.readLine();
        System.out.println(response);
        dataSocketServer.close();
    }
    
    public void downloadPass(String from_file_name, String to_path) throws Exception {
        if(checkIsPassiveMode()) {
        	// Send RETR command
            controlOut.println("RETR " + from_file_name);
            // Open data connection
           // Socket dataSocket = new Socket(passHost, passPort);
            // Read data from server
            BufferedOutputStream output = new BufferedOutputStream(
                    new FileOutputStream(new File(to_path, from_file_name)));
            BufferedInputStream input = new BufferedInputStream(
                    dataSocket.getInputStream());
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            output.flush();
            output.close();
            input.close();
            dataSocket.close();
            String response;
            response = controlReader.readLine();
            System.out.println(response);

            response = controlReader.readLine();
            System.out.println(response);
            
        }
    	
    }
    
    
    public void gotoo(String dir_name) throws IOException{
    	controlOut.println("CWD "+ dir_name);
    	String response;
        response=controlReader.readLine();
        System.out.println(response);
    }
    
    public void deldir(String dir_name) throws IOException {
    	controlOut.println("RMD "+ dir_name);
    	String response;
        response=controlReader.readLine();
        System.out.println(response);
        if (response.startsWith("550 ")) {
            JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void add_dir(String dir_name) throws IOException{
    	controlOut.println("MKD "+ dir_name);
    	System.out.println(dir_name);
    	String response;
        response=controlReader.readLine();
        System.out.println(response);
    }
    public void thoat() throws IOException {
    	String response;
    	controlOut.println("QUIT");
    	response=controlReader.readLine();
    	System.out.println(response);
    }
    public void xoafile(String pathname) throws IOException {
    	controlOut.println("DELE "+ pathname);
    	String response;
    	response=controlReader.readLine();
        System.out.println(response);
    }
    private boolean checkIsPassiveMode() throws Exception {
        String response;
            controlOut.println("PASV mode");
            response = controlReader.readLine();
            System.out.println(response);
            if (!response.startsWith("2271 ")) {
            	System.out.println("False");
                return false;
                
            	//throw new IOException("FTPClient could not request passive mode: " + response);
            }
           // else {
            int opening = response.indexOf('(');
            int closing = response.indexOf(')', opening + 1);
            if (closing > 0) {
                String dataLink = response.substring(opening + 1, closing);
                StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
             //fix chỗ này
                try {
                	passHost = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                            + tokenizer.nextToken() + "." + tokenizer.nextToken();
                    passPort = Integer.parseInt(tokenizer.nextToken()) * 256
                            + Integer.parseInt(tokenizer.nextToken());
                	
                	System.out.println(passHost);
                    System.out.println(passPort);
                } catch (Exception e) {
                    throw new IOException(
                            "FTPClient received bad data link information: "
                                    + response);
                }
                dataSocket = new Socket(passHost, passPort);
            }
            System.out.println("True");
             return true;
    }
}