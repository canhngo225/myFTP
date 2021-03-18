package ftp;

import java.io.File;
import java.io.Writer;

public class DeleCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, ControllerThread t) {
		System.out.println("Thực thi lệnh deleCommand.......");
		try {
			String pathname = t.getNowDir()+"/"+data;
			System.out.println(pathname); 
			File file = new File(pathname);
		        if(file.exists())
		        {
		        	
		        		if(file.delete())
		        		{
		        			
		        			writer.write("File was successfully removed");
		        		}
		        		else
		        		{
		        			writer.write("550 Requested action not taken");
		        		}
		        	
		        }
		        else 
		        {
							writer.write("550 Requested action not taken. File unavailable.");		
		        }
		        writer.write("\r\n");
	            writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
		
	}

