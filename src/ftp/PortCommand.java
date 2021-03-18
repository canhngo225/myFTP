package ftp;
import java.io.IOException;
import java.io.Writer;
import java.net.Socket;

public class PortCommand implements Command{

    @Override
    public void getResult(String data, Writer writer,ControllerThread t) {
        String response = "200 the port an ip have been transfered\t\t";
        try {
        	writer.write(response);
        	writer.write("\t\n");
        	writer.flush();
            String[] iAp =  data.split(",");
            String ip = iAp[0];
            String port = Integer.toString(Integer.parseInt(iAp[1]));
            System.out.println("ip is "+ip);
            System.out.println("port is "+port);
            t.setDataIp(ip);
            t.setDataPort(port);
            //
            try{
                t.setDataSocket( new Socket(t.getDataIp(), Integer.parseInt(t.getDataPort())));
                response ="Data connection - Active Mode - established";
                writer.write(response);
                writer.flush();
                System.out.println(response);
                
                
            }catch (IOException e){
                response ="Could not connect to client data socket";
                writer.write(response);
                writer.flush();
                System.out.println(response);
                
                e.printStackTrace();
            }
            
            
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

} 