package ftp;

import java.io.IOException;
import java.io.Writer;

public class PassCommand implements Command{

	@Override
	public void getResult(String data, Writer writer, ControllerThread t) {
		System.out.println("execute the pass command");
        System.out.println("the data is "+data);
        String key = ControllerThread.USER.get();
        String pass = Share.users.get(key);

        String response = null;
        if(pass.equals(data)) {
            System.out.println("Đăng nhập thành công");
            Share.loginedUser.add(key);
            t.setIsLogin(true);
            response = "230 User "+key+" logged in";
        }
        else {
            System.out.println("Đăng nhập không thành công, sai mật khẩu");
            response = "530 Lỗi mật khẩu";
        }
        try {
            writer.write(response);
            writer.write("\r\n");
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
	}

}
