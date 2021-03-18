package ftp;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Writer;



public class StoreCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {
        System.out.println("Thực thi lệnh StoreCommand");
        
        try{
            writer.write("150 Kết nối dữ liệu nhị phân\r\n");
            //writer.write("\t\n");
            writer.flush();
            RandomAccessFile inFile = new RandomAccessFile(t.getNowDir()+"/"+data,"rw");
            System.out.println(t.getNowDir()+"/"+data);
            //Socket tempSocket = new Socket(t.getDataIp(),Integer.parseInt(t.getDataPort()));
            InputStream inSocket= t.getDataSocket().getInputStream();
            
            
            byte byteBuffer[] = new byte[4096];
            int amount;
            //
            while((amount =inSocket.read(byteBuffer) )!= -1){
                inFile.write(byteBuffer, 0, amount);
            }
            System.out.println("Truyền xong đóng kết nối ...");
            inFile.close();
            inSocket.close();
            t.getDataSocket().close();
            //Ngắt kết nối dữ liệu 

            writer.write("226 Chuyển hoàn tất \r\n");
            writer.flush();
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
        }
       


    }
