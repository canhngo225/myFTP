package ftp;


import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.util.List;

public class Share {
	public static  String rootDir = new File("").getAbsolutePath();
	public static Map<String,String> users = new HashMap<String,String>();
	public static HashSet<String> loginedUser = new HashSet<String>();
	public static HashSet<String> adminUsers = new HashSet<String>();

	public static void init() {
		String path = System.getProperty("user.dir")+"/config/server.xml";
		File file = new File(path);
		SAXBuilder builder = new SAXBuilder();
		try {
			Document parse = builder.build(file);
			Element root = parse.getRootElement();
			
			//định cấu hình thư mục mặc định của máy chủ
			rootDir = root.getChildText("rootDir");
			System.out.print("rootDir is ");
			System.out.print(rootDir);
			
			Element usersE = root.getChild("users");
			List<Element> usersEC = usersE.getChildren();
			String username = null, password=null;
			System.out.println("\nTất cả thông tin người dùng: ");
			 for(Element user : usersEC) {
	                username = user.getChildText("username");
	                password = user.getChildText("password");
	                System.out.println("Tên người dùng： "+username);
	                System.out.println("Mật khẩu： "+password);
	                users.put(username,password);
	            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
