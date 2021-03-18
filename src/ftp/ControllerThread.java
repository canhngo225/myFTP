package ftp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;


public class ControllerThread extends Thread{
	private int count =0;
	private Socket socket,dataSocket;
	public String mode="control";
	private String nowDir = Share.rootDir;
	public static final ThreadLocal<String> USER = new ThreadLocal<String>();
	private String dataIp;
	private  String dataPort;
	private boolean isLogin = false;
	
	
	public Socket getDataSocket() {
		return dataSocket;
	}

	public void setDataSocket(Socket dataSocket) {
		this.dataSocket = dataSocket;
	}

	public ControllerThread(Socket socket)
	 {
		 this.socket = socket;
	 }
	 
	 public ControllerThread(Socket socket, String mode)
	 {
		 this.socket = socket;
		 this.mode = mode;
	 }
	 public String getNowDir() {
	        return nowDir;
	    }
	    public void setNowDir(String nowDir) {
	        this.nowDir = nowDir;
	    }
	    public void setIsLogin(boolean t) {
	        isLogin = t;
	    }
	    public boolean getIsLogin() {
	        return isLogin;
	    }
	    public Socket getSocket() {
	        return socket;
	    }
	    public String getDataIp() {
	        return dataIp;
	    }
	    public void setDataIp(String dataIp) {
	        this.dataIp = dataIp;
	    }
	    public String getDataPort() {
	        return dataPort;
	    }
	    public void setDataPort(String dataPort) {
	        this.dataPort = dataPort;
	    }
	    public String getMode() {
	    	return mode;
	    }
	 @Override
	public void run() {
		System.out.println("a new client is connected..");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Writer writer = new OutputStreamWriter(socket.getOutputStream());
			while(true) {
				if(count == 0 && this.mode.equals("control"))
				{
					writer.write("220 welcome my ftp server, Server ready .\r\n");
					writer.flush();
					count++;
				}
				
				else {
					if(!socket.isClosed()) {
						
						String command = reader.readLine();
						System.out.println(command);
						
						if(command != null) {
							String[] datas=command.split(" ");
							Command commandSolver = CommandFactory.createCommand(datas[0]);
						if(loginValiate(commandSolver)) {
							if(commandSolver == null)
							{
								writer.write("502 Lệnh không tồn tại, xin vui lòng nhập lại");
							}
							else
							{
								String data ="";
								if(datas.length >2) {
									for(int i = 1;i<datas.length;i++)
									{
										data +=datas[i];
										if(!(i==datas.length-1)) data+=" ";
									}
								}
								if(datas.length==2) data=datas[1];
								
								commandSolver.getResult(data,writer,this);
							}
						}
						else
						{
							writer.write("532 đăng nhập là cần thiết để thực hiện lệnh này. "
									+ "Xin vui lòng đăng nhập và sau đó thực hiện các hoạt động tương ứng \r\n");
							writer.flush();
						}
						}
					
					}
					else {
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("Kết thúc kết nối tcp");
		}
	}
	 
	 public boolean loginValiate(Command command) {
		 if(command instanceof UserCommand || command instanceof PassCommand) {
			 return true;
		 }
		 else {
			 return isLogin;
		 }
	 }
}
