package ftp;

import java.io.Writer;

public class UserCommand implements Command{
	@Override
	public void getResult(String data, Writer writer, ControllerThread t) {
		String response = "";
		//System.out.println("data :   "+data);
		if(Share.users.containsKey(data)) {
			ControllerThread.USER.set(data);
			response = "331 please input your password";
			
		}
		else {
			response= "501 user is not validate";
		}
		try {
			writer.write(response);
			writer.write("\r\n");
			writer.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
