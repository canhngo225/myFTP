package ftp;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
//Tạo một new directory trên server
public class MkdCommand implements Command{
	@Override
	public void getResult(String data, Writer writer, ControllerThread t) {
		try {
		if((data!=null) && (data.matches("^[a-zA-Z0-9]+$")))
		{
			String desDir = t.getNowDir() + File.separator +data;
			File file = new File(desDir);
	        System.out.println(desDir);
	        if(!file.mkdir()) {
					writer.write("550 Failed to create new directory");
					
	        }
	        else {
	        	writer.write("250 Directory successfully created");
	        }
		}
		else 
		{
			
				writer.write("550 Invalid name");
				
			
		}
		writer.write("\r\n");
		writer.flush();
		
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
}
