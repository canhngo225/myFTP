package ftp;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

public class RetrCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        
        String desDir = t.getNowDir()+File.separator+data;
        File file = new File(desDir);
        System.out.println("retr"+desDir);
        if(file.exists())
        {
            try {
                writer.write("150 open ascii mode...\r\n");
                writer.flush();
                BufferedOutputStream dataOut = new BufferedOutputStream(t.getDataSocket().getOutputStream());
                
                byte[] buf = new byte[1024];
                InputStream is = new FileInputStream(file);
                while(-1 != is.read(buf)) {
                    dataOut.write(buf);
                }
                dataOut.flush();
                
                writer.write("220 transfer complete...\r\n");
                writer.flush();
                t.getDataSocket().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        	
        	
        }
        else {
            try {
                writer.write("220  Các tập tin không tồn tại \r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
