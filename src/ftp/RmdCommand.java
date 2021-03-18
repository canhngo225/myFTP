package ftp;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
//remove a directory
public class RmdCommand implements Command{
	@Override
	public void getResult(String data, Writer writer, ControllerThread t)  {
		System.out.println("excute Rmd Command........");
		String desDir = t.getNowDir();
		//Chỉ cho phép những thư mục chứa chữ và số
		try {
		if(data.contentEquals("..")) {
    		int ind = t.getNowDir().lastIndexOf(File.separator);
    		if(ind > 0)
    		{
    			desDir = t.getNowDir().substring(0, ind);
    			//t.setNowDir(nowDir1);
    			
    		}
    	}
    	else if((data!=null) && (data.matches("^[a-zA-Z0-9]+$")))
		{
			desDir = desDir + File.separator +data;
		}
    	else
		{
					writer.write("550 Invalid file name");
					
		}
			File file = new File(desDir);
	        System.out.println(desDir);
	        if(file.exists() && file.isDirectory())
	        {
	        	
	        		if(file.delete())
	        		{
	        			
	        			writer.write("250 Directory was successfully removed");
	        		}
	        		else
	        		{
	        			writer.write("550 Requested action not taken. File not empty.");
	        		}
	        	
	        }
	        else 
	        {
						writer.write("550 Requested action not taken. File unavailable.");		
	        }
	        writer.write("\r\n");
            writer.flush();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
