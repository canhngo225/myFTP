package ftp;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.Vector;

public class DirCommand implements Command{
	@Override
	public void getResult(String data, Writer writer, ControllerThread t) {
	System.out.println("excute LIST command ..............");
	String desDir = t.getNowDir()+data;
	System.out.println(desDir);
	File dir = new File(desDir);
	if(!dir.exists()) {
		try {
			writer.write("210 thư mục tập tin không tồn tại\r\n");
			writer.flush();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	else {
		StringBuilder dirs = new StringBuilder();
		System.out.println("Thư mục tập tin như sau:");
		dirs.append("Thư mục tệp tin như sau: \n");
		 Vector<String> allfiles=new Vector<>();
         String[] lists= dir.list();
         String flag = null;
         for(String name : lists) {
             File temp = new File(desDir+File.separator+name);
             if(temp.isDirectory()) {
                 flag = "d";
             }
             else {
                 flag = "f";
             }
             String oneinfo=flag+"rw-rw-rw-   1 ftp      ftp            "+temp.length()+" "+name;
             System.out.println(oneinfo);
             allfiles.add(oneinfo);
	}
         try {
             writer.write("150 Opening data connection for directory list...\r\n");
             writer.flush();

             for(String oneinfo : allfiles)
             {
                 writer.write(oneinfo);
                 writer.write("\r\n");
                 writer.flush();
             }

             writer.write("end of files\r\n");
             writer.flush();

             writer.write("226 transfer complete...\r\n");
             writer.flush();
         } catch (NumberFormatException e) {

             e.printStackTrace();
         } catch (UnknownHostException e) {

             e.printStackTrace();
         } catch (IOException e) {

             e.printStackTrace();
         }
	}
	}
}
