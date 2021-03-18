package ftp;

import java.io.Writer;

public interface Command {
	public void getResult(String data, Writer writer, ControllerThread t);
}
