package ftp;

public class CommandFactory {
	public static Command createCommand(String type) {

        type = type.toUpperCase();
        switch(type)
        {
            case "USER":return new UserCommand();//

            case "PASS":return new PassCommand();//

            case "LIST":return new DirCommand();//

            case "MYPORT":return new PortCommand();//

            case "QUIT":return new QuitCommand();//

            case "RETR":return new RetrCommand();//download

            case "CWD":return new CwdCommand();//Đổi thư mục đang làm việc  Go to

            case "STOR":return new StoreCommand();//upload

            case "PASV":return new PasvCommand();//// un success
            
            case "RMD":return new RmdCommand();//Xóa thư mục 
            
            case "MKD":return new MkdCommand();//Tạo thư mục 
            
            case "PWD": return new PwdCommand();//In ra thư mục làm việc. Trả về thư mục hiện tại trên host. un

            case "DELE": return new DeleCommand();//Xóa file 
          //TYPE SYST FEAT EPSV NLST NLST
            default :return null;
        }

    }
}
