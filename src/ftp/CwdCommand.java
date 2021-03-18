package ftp;
import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class CwdCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        String dir = t.getNowDir(); 
        
        try {
        	if(data.contentEquals("..")) {
        		int ind = t.getNowDir().lastIndexOf(File.separator);
        		if(ind > 0)
        		{
        			dir = t.getNowDir().substring(0, ind);
        			//t.setNowDir(nowDir1);
        			
        		}
        	}
        	else if ((data!=null) && (!data.equals("."))) {
        		 dir= t.getNowDir() + File.separator+data;
        	}
        	File file = new File(dir);
        	
            if((file.exists())&&(file.isDirectory())) {
                //String nowDir =t.getNowDir() +File.separator+data;
                t.setNowDir(dir);
                writer.write("250 CWD command succesful");
            }
            else
            {
                writer.write("550 Requested action not taken. File unavailable");
            }
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}