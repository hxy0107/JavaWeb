package automation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by xianyu.hxy on 2015/7/13.
 */
public class HasPac {
    public static final String FILE_BASE = "e:" + File.separator + "temp";
    public static final String PAC_NAME = "com" + File.separator + "alipay" + File.separator + "android" + File.separator + "app" + File.separator + "pay";
    public static final String PAC_NAME1 = "com" + File.separator + "alipay";

    public static final String BASE_FOLDER = "com" + File.separator + "alipay" + File.separator + "sdk" + File.separator + "cons";
    public static final String GLOBALCONSTANTS = BASE_FOLDER + File.separator + "GlobalConstants.class";
    public static final String OUTPUT_FILE = BASE_FOLDER + File.separator + "GlobalConstantsHXY.txt";
    public static ArrayList<String> appList;
    public static ArrayList<String> pacPath;
    public static boolean isContain = false;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        InvokeBat invokeBat = new InvokeBat();
        pacPath = new ArrayList<String>();
        appList = new ArrayList<String>();


        Class.forName("org.gjt.mm.mysql.Driver");
        System.out.println("Success loading Mysql Driver!");
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "1");
        System.out.println("Success connect Mysql server!");
        Statement stmt = connect.createStatement();
        //String sql=xx;
        // int result=stmt.executeUpdate(sql);


        File baseFile = new File(FILE_BASE);
        if (baseFile.exists() && baseFile.isDirectory()) {
            File[] aFiles = baseFile.listFiles();
            //aFile a,b,c...
            for (File aFile : aFiles) {
                File[] unjarFiles = aFile.listFiles();
                //unjarFile ֧����,�εδ�...
                for (File unjarFile : unjarFiles) {
                    //if(isContain)break;
                    if (unjarFile.exists() && unjarFile.isDirectory()) {
                        //�������app��ѹ�ļ��и�Ŀ
                        String path = unjarFile.getAbsolutePath();
                        getFileList(path);
                        for (String s : pacPath) {
                            boolean b = s.contains(BASE_FOLDER);
                            if (b) {
                                //System.out.println(pacPath);
                                isContain = true;
                                if (!appList.contains(s)) {
                                    appList.add(s);
                                }

                                // System.out.println(s);
                                String tag[] = s.trim().split("\\\\");
                                String tags[] = tag[3].split("_");
                                String packName = tags[1];
                                System.out.println(packName);

                                String in=s+File.separator+"GlobalConstants.class";
                                String out = s + File.separator + "GlobalConstantsHXY.txt";
                                System.out.println("***in :"+in);
                                System.out.println("***in :"+out);
                                String cmdStr = "cmd /c javap -constants " + in + " > " + out;
                                invokeBat.runbat(cmdStr);



                                //���ļ�
                                String[] lines = readTXT(out);
                                for (String line : lines) {
                                    System.out.println(line);
                                    if (line.trim().startsWith(test.G)) {
                                        test.msp_version = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                                        break;
                                    }
                                }
                                if(test.msp_version==null||test.msp_version.length()>15){test.msp_version="999";}
                                System.out.println("msp_version: " + test.msp_version);
                                String sql="UPDATE app_info.`pack_only_copy_7.22` SET sdk_version='"+ test.msp_version.trim()+"'\n" +
                                        " WHERE package_name='"+packName+"'";
                                stmt.execute(sql);
                                test.msp_version=null;
                            }
                        }

                    }


                }
            }
        }
        for (String list : appList) {
            System.out.println("************" + list);
        }

    }

    public static void getFileList(String directory) {
        File f = new File(directory);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //  System.out.println("�ļ���" + files[i]);
            } else {
                //System.out.println("Ŀ¼��" + files[i].getAbsolutePath());
                pacPath.add(files[i].getAbsolutePath());
                getFileList(files[i].getAbsolutePath());
            }
        }
    }

    public static String[] readTXT(String path) {
        File f = new File(path);
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        if (f.exists() && f.isFile()) {
            try {
                //System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
                reader = new BufferedReader(new FileReader(f));
                String tempString = null;
                int line = 1;
                // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
                while ((tempString = reader.readLine()) != null) {
                    // ��ʾ�к�
                    //System.out.println("line " + line + ": " + tempString);
                    buffer.append(tempString + "\n");
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        String[] s = buffer.toString().split("\n");
        return s;
    }

}
