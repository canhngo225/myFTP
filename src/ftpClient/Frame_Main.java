package ftpClient;

import java.awt.EventQueue;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.net.ftp.FTPFile;



public class Frame_Main extends JFrame {

	private static final long serialVersionUID = 1L;
	static FTPFile[] file;
    static String FTP;
    static String username;
    static String password;
    
    DefaultTableModel model;
    private boolean isPassMode=false;
    private JTable table;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JScrollPane scrollPane;
    
    //private JButton upload,refresh;
    static Ftp_client ftp_client;
    public static Ftp_client getFtp_client() {
        return ftp_client;
    }
    
    public static FTPFile[] getFile(){
        return file;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Frame_Main().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public Frame_Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        //frame = new JFrame();
        //frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Frame_Main.class.getResource("/com/sun/java/swing/plaf/windows/icons/UpFolder.gif")));
        this.setTitle("FTP Client");
        this.setBounds(100, 100, 650, 534);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);


      //(FTP username)-----------------------------------------------
        lblNewLabel = new JLabel("Địa chỉ IP FTP");
        lblNewLabel.setBounds(32, 8, 150, 20);
        lblNewLabel.setFont(new Font("Arial",Font.BOLD,16));
        this.add(lblNewLabel);

        lblNewLabel_1 = new JLabel("Tên người dùng ");
        lblNewLabel_1.setBounds(32, 30, 150, 20);
        lblNewLabel_1.setFont(new Font("Arial",Font.BOLD,16));
        this.add(lblNewLabel_1);

        lblNewLabel_2 = new JLabel("Mật khẩu ");
        lblNewLabel_2.setBounds(32, 53, 150, 20);
        lblNewLabel_2.setFont(new Font("Arial",Font.BOLD,16));
        this.add(lblNewLabel_2);

        JTextField url = new JTextField("127.0.0.1");   //địa chỉ dịch vụ FTP 1.55.116.158
        url.setBounds(170,8,120,20);
        url.setFont(new Font("Arial",Font.ITALIC,16));
        this.add(url);

        JTextField usernameField = new JTextField("admin"); //tên người dùng
        usernameField.setBounds(170,30,120,20);
        usernameField.setFont(new Font("Arial",Font.ITALIC,16));
        this.add(usernameField);

        JPasswordField passwordField = new JPasswordField("000000");  //mật khẩu 
        passwordField.setBounds(170,53,120,20);
        this.add(passwordField);

       //Nút đăng nhập ------------------------------------------------
        JButton login=new JButton("Đăng nhập");
        login.setFont(new Font("Arial", Font.PLAIN, 16));
        login.setBackground(UIManager.getColor("Button.highlight"));
        login.setBounds(300, 15, 130, 23);
        this.add(login);
        
        login.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Đăng nhập==============");
                try {
                    FTP=url.getText().trim();
                    System.out.println(FTP);
                    username=usernameField.getText().trim();
                    password=passwordField.getText().trim();

                   ftp_client=new Ftp_client(FTP,username,password);
                   // ftp_passive =new Ftp_passive(FTP,username,password);
                    if(Ftp_client.isLogined)
                    //if(Ftp_passive.isLogined)
                    {
                        file=ftp_client.getAllFile();
                        setTableInfo();//Hiển thị tất cả thông tin tệp 

                        url.setEditable(false);
                        usernameField.setEditable(false);
                        passwordField.setEditable(false);
                       
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showConfirmDialog(null, "Tên người dùng hoặc mật khẩu sai  \n username："+username, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        //Thoát
        JButton exit=new JButton("Thoát");
        exit.setFont(new Font("Arial", Font.PLAIN, 16));
        exit.setBackground(UIManager.getColor("Button.highlight"));
        exit.setBounds(300, 50, 130, 23);
        this.add(exit);
        
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Thoát=========");
                try {
                    Frame_Main.getFtp_client().thoat();
                    exiit();
                } catch (Exception e1) {
                    e1.printStackTrace();                    
                }
            }
        });
        
        //Tải lên--------------------------------------------------
        JButton upload = new JButton("Tải lên");
        upload.setFont(new Font("Arial", Font.PLAIN, 16));
        upload.setBackground(UIManager.getColor("Button.highlight"));
        upload.setBounds(440, 50, 135, 23);
        this.add(upload);
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //Tải lên và nhập vào nút kích hoạt------------------------------------
                System.out.println("Tải lên！！！！！");
                int result = 0;
                //File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                System.out.println(fsv.getHomeDirectory());                 
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("Vui lòng chọn tệp để tải lên ...");
                fileChooser.setApproveButtonText("OK");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                result = fileChooser.showOpenDialog(null);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path=fileChooser.getSelectedFile().getPath();
                    System.out.println("path: "+path);
                    try {
                    	if(isPassMode)
                    		Frame_Main.getFtp_client().uploadPass(path);
                        else
                        	Frame_Main.getFtp_client().upload(path);
                        	//ftp_client.upload(path);
                        System.out.println("Tải tệp lên thành công! ");
                        JOptionPane.showMessageDialog(null, "Tải tệp lên thành công!", "Message",
    							JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    finally{
                    	try {
							file=ftp_client.getAllFile();
							CD();
							
							
						} catch (Exception e) {
							
							e.printStackTrace();
						}
                        
//                        Frame_Main.getFtp().close_connection();
                    }
                }
               
            }
        });

        
        JButton refresh = new JButton("Làm mới ");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try{
                    file=ftp_client.getAllFile();
                    CD();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                
            }
        });
        refresh.setFont(new Font("Arial", Font.PLAIN, 16));
        refresh.setBackground(UIManager.getColor("Button.highlight"));
        refresh.setBounds(440, 15, 135, 23);
        this.add(refresh);
        

        /////////////////////////////////////
        JButton download = new JButton("Tải xuống ");
        download.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//fireEditingStopped();
            	if((table.getSelectedRow()==-1)|| !table.getValueAt(table.getSelectedRow(), 0).toString().contains(".")) 
            	{
            		JOptionPane.showMessageDialog(null, "Vui lòng chọn tệp bạn muốn tải xuống!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
            	
                FTPFile[]  file1= new FTPFile[0];
                try {
                    file1 = Frame_Main.getFile(); 
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                String from_file_name=file1[table.getSelectedRow()].getName();
                int result = 0;
                //File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                fsv.createFileObject(from_file_name);
                //System.out.println(fsv.getHomeDirectory());  
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //fileChooser.setCurrentDirectory(new File(from_file_name));  
                fileChooser.setDialogTitle("Lưu thành:");
                //fileChooser.setApproveButtonText("Lưu");   
                result = fileChooser.showSaveDialog(null);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path=fileChooser.getSelectedFile().getPath()+"\\"; 
                    System.out.println("path: "+path);
                    System.out.println("from_file_name:"+from_file_name);
                    try {
                    	if(isPassMode)
                    		Frame_Main.getFtp_client().downloadPass(from_file_name, path);
                        else
                        	Frame_Main.getFtp_client().download(from_file_name, path);
                        System.out.println("Tải xuống thành công! ");
                        JOptionPane.showMessageDialog(null, "Tải xuống thành công!", "Message",
    							JOptionPane.INFORMATION_MESSAGE);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
            }
        });
        download.setFont(new Font("Arial", Font.PLAIN, 16));
        download.setBackground(UIManager.getColor("Button.highlight"));
        download.setBounds(440, 85, 135, 23);
        this.add(download);
        
        
        JButton gotoo = new JButton("Chuyển đến");
        gotoo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//fireEditingStopped();
            	if((table.getSelectedRow()==-1)|| table.getValueAt(table.getSelectedRow(), 0).toString().contains(".")) 
            	{
            		JOptionPane.showMessageDialog(null, "Vui lòng chọn thư mục mới để chuyển đến!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					 FTPFile[]  file1= new FTPFile[0];
		                try {
		                    file1 = Frame_Main.getFile(); 
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                String file_name=file1[table.getSelectedRow()].getName();
	                    System.out.println("file_name:"+file_name);
	                    try {
	                        Frame_Main.getFtp_client().gotoo(file_name);
	                       
	                    } catch (Exception e1) {
	                        e1.printStackTrace();
	                    }
	                    finally{
	                    	try {
								file=ftp_client.getAllFile();
								CD();
								
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	                    }
        
                
            
            }
            }
        });
        gotoo.setFont(new Font("Arial", Font.PLAIN, 16));
        gotoo.setBackground(UIManager.getColor("Button.highlight"));
        gotoo.setBounds(440, 120, 135, 23);
        this.add(gotoo);
        
        JButton back = new JButton("Trở về");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//fireEditingStopped();
	                    try {
	                        Frame_Main.getFtp_client().gotoo("..");
	                        
	                    } catch (Exception e1) {
	                       
	                        e1.printStackTrace();
	                    }
	                    finally{
	                    	try {
								file=ftp_client.getAllFile();
								CD();
								
							} catch (Exception e) {
								
								e.printStackTrace();
							}
	                        
	                    }
        
                
            
            }
            
        });
        back.setFont(new Font("Arial", Font.PLAIN, 16));
        back.setBackground(UIManager.getColor("Button.highlight"));
        back.setBounds(440, 155, 135, 23);
        this.add(back);
        
        
        JButton deldir = new JButton("Xóa thư mục");
        deldir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//fireEditingStopped();
            	if((table.getSelectedRow()==-1)|| table.getValueAt(table.getSelectedRow(), 0).toString().contains(".")) 
            	{
            		JOptionPane.showMessageDialog(null, "Vui lòng chọn thư mục để xóa!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					 FTPFile[]  file1= new FTPFile[0];
		                try {
		                    file1 = Frame_Main.getFile(); 
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                String file_name=file1[table.getSelectedRow()].getName();
	                    System.out.println("file_name:"+file_name);
	                    try {
	                        Frame_Main.getFtp_client().deldir(file_name);
	                      

	                    } catch (Exception e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
	                    finally{
	                    	try {
								file=ftp_client.getAllFile();
								model.setRowCount(0);
								CD();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                    }
        
                
            
            }
            }
        });
        deldir.setFont(new Font("Arial", Font.PLAIN, 16));
        deldir.setBackground(UIManager.getColor("Button.highlight"));
        deldir.setBounds(440, 225, 135, 23);
        this.add(deldir);
        
        JButton addir = new JButton("Thêm thư mục");
        addir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//fireEditingStopped();
            	
            		String name = JOptionPane.showInputDialog("Vui lòng nhập tên thư mục muốn tạo!");
            		if (!name.matches("^[a-zA-Z0-9]+$")) { 
            			JOptionPane.showMessageDialog(null, "Tên của thư mục chỉ chứa các kí tự chữ và số","Message",JOptionPane.WARNING_MESSAGE);
            		return;
            		}
            		else {
	                    try {
	                        Frame_Main.getFtp_client().add_dir(name);

	                    } catch (Exception e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
	                    finally{
	                    	try {
								file=ftp_client.getAllFile();
								model.setRowCount(0);
								CD();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                    }
            }
            }
        });
        addir.setFont(new Font("Arial", Font.PLAIN, 16));
        addir.setBackground(UIManager.getColor("Button.highlight"));
        addir.setBounds(440, 190, 135, 23);
        this.add(addir);
        JButton delfile = new JButton("Xóa file");
        delfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	//fireEditingStopped();
            	if((table.getSelectedRow()==-1)|| !table.getValueAt(table.getSelectedRow(), 0).toString().contains(".")) 
            	{
            		JOptionPane.showMessageDialog(null, "Vui lòng chọn file để xóa!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					 FTPFile[]  file1= new FTPFile[0];
		                try {
		                    file1 = Frame_Main.getFile(); 
		                } catch (Exception e1) {
		                    e1.printStackTrace();
		                }
		                String pathname=file1[table.getSelectedRow()].getName();
	                    System.out.println("file_name:"+pathname);
	                    try {
	                        Frame_Main.getFtp_client().xoafile(pathname);
	                      

	                    } catch (Exception e1) {
	                        // TODO Auto-generated catch block
	                        e1.printStackTrace();
	                    }
	                    finally{
	                    	try {
								file=ftp_client.getAllFile();
								model.setRowCount(0);
								CD();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                    }
        
                
            
            }
            }
        });
        delfile.setFont(new Font("Arial", Font.PLAIN, 16));
        delfile.setBackground(UIManager.getColor("Button.highlight"));
        delfile.setBounds(440, 260, 135, 23);
        this.add(delfile);
        
        JLabel modeText = new JLabel("Active");
        modeText.setFont(new Font("Arial", Font.BOLD, 18));
        modeText.setBounds(470, 350, 135, 23);
        this.add(modeText);
        
        JButton mode = new JButton("Đổi chế độ");
        mode.setFont(new Font("Arial", Font.PLAIN, 16));
        mode.setBackground(UIManager.getColor("Button.highlight"));
        mode.setBounds(440, 295, 135, 50);
        mode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(isPassMode) {
					isPassMode=false;
					modeText.setText("Active");
					
				}
				else {
					isPassMode =true;
					modeText.setText("Passive");
					
				}
			}
		});
        this.add(mode);  
        
        		
    }

    
    private void setTableInfo()
    {
        //khởi tạo dữ liệu bảng đọc tất cả các tệp từ ftp
    	//System.out.println(file.length);
        String[][] data1=new String[file.length][3];
        for(int row=0;row<file.length;row++)
        {

            data1[row][0]=file[row].getName();
            if(file[row].isDirectory())
            {
                data1[row][1]="Thư mục";
            }
            else if(file[row].isFile()){
                String geshi[]=file[row].getName().split("\\.");
                System.out.println(file[row].getName());
                	data1[row][1]=geshi[1];
                
                
            }
            
            data1[row][2]=file[row].getSize()+"";
           // data1[row][3]="Tải xuống";
        }



        
       // String[] columnNames = {" Tệp " , " Loại tệp " , " Kích thước tệp (B) " , " "  };
        String[] columnNames = {" Tệp " , " Loại tệp " , " Kích thước tệp (B) "  };
        

        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(32, 78, 398, 400);
        
        
        table = new JTable();
        scrollPane.setViewportView(table);
        
        //DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) table.getModel();
        
        model.setDataVector(data1, columnNames);
        
       // table.setBounds(32, 78, 400, 400);
        //table.setColumnSelectionAllowed(true);
        //table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
       // table.getColumnModel().getColumn(3).setPreferredWidth(100);
       // table.setToolTipText("Bạn có thể bấm vào để tải về");
        table.setModel(model);
       
       // this.getContentPane().add(table);
        this.getContentPane().add(scrollPane);
      //  ButtonColumn buttonsColumn = new ButtonColumn(table, 3);
    }
    
   public void exiit() {
	   this.setVisible(false);
   }
   
    public void CD()
    {
    	this.remove(scrollPane);
    	setTableInfo();
    }
}
