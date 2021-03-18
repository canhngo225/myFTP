package ftp;

import java.io.IOException;
import java.io.Writer;

public class PwdCommand implements Command{
	@Override
	public void getResult(String data, Writer writer, ControllerThread t) {
		System.out.println("excute Pwd command");
		try {
			writer.write(" 257 \"" + t.getNowDir() +  " \" "  );
			writer.flush();
		} catch (IOException e) {
			// TODO: handle exception
		}
		
	}
}
