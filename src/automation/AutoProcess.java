package automation;

import download.DownloadUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by xianyu.hxy on 2015/7/23.
 */
public class AutoProcess {
    public static final String BASE_FOLDER = "com" + File.separator + "alipay" + File.separator + "sdk" + File.separator + "cons";
    public static boolean isContain = false;
    public static ArrayList<String> appList;



    //public static String RequestApp="΢��";
    public static String RequestPac="com.jumi";
    public static String choose_url;
    public static String app_verison=null;
    public static String sql_version=null;
    public static String  app_url=null;
    public static String app_name=null;
    public static boolean IsNewest=true;
    public static boolean HasApp=false;
    public static ArrayList<DownloadItem> itemList;
    public final static String FileDirBase="e:"+File.separator+"temp2";
    public static String decode_app="";
    public static String has_sdk="";
    public static ArrayList<String> pacPath;

    public static int main(String RequestApp){


        try {
            System.out.println("RequestApp: "+RequestApp);
            InvokeBat invokeBat=new InvokeBat();
            pacPath=new ArrayList<String>();
            String RequestApp1=URLEncoder.encode(RequestApp,"utf-8");
            String requestUrl="http://apps.wandoujia.com/search?key="+RequestApp1+"&source=search";
            System.out.println("requestUrl:" + requestUrl);

            Class.forName("org.gjt.mm.mysql.Driver");
            System.out.println("Success loading Mysql Driver!");
            Connection connect= DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "1");
            System.out.println("Success connect Mysql server!");
            Statement stmt=connect.createStatement();
            String sql="SELECT app_version FROM app_info.`pack_only_copy_7.22` WHERE app_name='"+RequestApp+"'";
            ResultSet result=stmt.executeQuery(sql);
            if(result.next()){
                HasApp=true;}
            System.out.println("query result: "+HasApp);


            URL url=new URL(requestUrl);
            HtmlCleaner cleaner=new HtmlCleaner();
            TagNode node=cleaner.clean(url);
            itemList=new ArrayList<DownloadItem>();
            spiderUrl(cleaner, itemList, url);
            System.out.println("itemList.size:" + itemList.size());

            if(itemList.size()>1){
                DownloadItem downloadItems=itemList.get(0);
                String appName=downloadItems.getDownload_name();
                String appUrl1=downloadItems.getDownload_url();
                String appUrl=appUrl1.replaceAll("&amp;","&");
                String pn=downloadItems.getPn();
                String md5=downloadItems.getMd5();
                String vc=downloadItems.getVc();
                String id=downloadItems.getApkid();
                String size=downloadItems.getSize();
                String icon=downloadItems.getIcon();
                System.out.println("appName:"+appName+",app_version:"+vc);

                String sql1="INSERT INTO app_info.`pack_only_copy_7.22`\n" +
                        "(package_name,app_name,app_version,app_md5,app_url,app_id,app_size,app_icon)\n" +
                        "SELECT "+"'"+pn+"','"+appName+"','"+vc+"','"+md5+"','"+appUrl+"','"+id+"','"+size+"','"+icon+"'"+"\n" +
                        "FROM DUAL\n" +
                        "WHERE NOT EXISTS(\n" +
                        "SELECT * FROM app_info.`pack_only_copy_7.22`\n" +
                        "WHERE package_name='" +pn+
                        "');";
                int result1=stmt.executeUpdate(sql1);
                String sql3="UPDATE app_info.`pack_only_copy_7.22` \n" +
                        "SET app_version='"+vc+"'"+
                        "WHERE package_name='"+pn+"'\n"
                        ;
                stmt.executeUpdate(sql3);
                File app=new File(FileDirBase+File.separator+ appName+"_"+pn+"_"+vc+".apk");
                File jarFile=new File(FileDirBase+File.separator+ appName+"_"+pn+"_"+vc+"-enjarify"+".jar");
                File file=new File(FileDirBase+File.separator+ appName+"_"+pn+"_"+vc+"-enjarify");

                DownloadUtils.download(appUrl, appName + "_" + pn + "_" + vc + ".apk", FileDirBase, 1);
                Thread.sleep(20000);
                System.out.println("download_app Length:"+app.length());
                while(app.length()<100){
                    Thread.sleep(10000);
                }
                //������
                if(app.exists()&&app.isFile()&&app.length()>5000){
                    String Path=app.getAbsolutePath();
                    String cmdStr="cmd /c enjarify "+Path;
                    long start=System.currentTimeMillis();
                    invokeBat.runbat(cmdStr);
                    long end=System.currentTimeMillis();
                    System.out.println("finish:"+(end-start)/1000+" s");
                }
                if(jarFile.length()<10000){decode_app="false";
                    String sql4="UPDATE app_info.`pack_only_copy_7.22` \n" +
                            "SET decode_app='false' "+
                            "WHERE package_name='"+pn+"'\n"
                            ;
                    stmt.executeUpdate(sql4);
                }else{decode_app="true";};
                //��ѹ
                if(jarFile.exists()&&jarFile.isFile()&&jarFile.length()>10000){
                    String filePath=jarFile.getAbsolutePath();
                    String folerName=filePath.substring(0, filePath.lastIndexOf("."));
                    System.out.println("filePath:" + folerName);
                    long start=System.currentTimeMillis();
                    ZipUtil.unzip(filePath,folerName);
                    long end=System.currentTimeMillis();
                    System.out.println("untar time:"+(end-start)/1000+" s");
                }


                if (file.exists() && file.isDirectory()) {
                    //�������app��ѹ�ļ��и�Ŀ
                    String path = file.getAbsolutePath();
                    pacPath.clear();
                    getFileList(path);
                    for (String s : pacPath) {
                        boolean b = s.contains(BASE_FOLDER);
                        if (b) {
                            has_sdk="true";
                            System.out.println(pacPath);
                            isContain = true;
                            String sql5="UPDATE app_info.`pack_only_copy_7.22` \n" +
                                    "SET decode_app='true',has_sdk='true' "+
                                    "WHERE package_name='"+pn+"'\n"
                                    ;
                            stmt.executeUpdate(sql5);

                            // System.out.println(s);
                            String tag[] = s.trim().split("\\\\");
                            String tags[] = tag[2].split("_");
                            String packName = tags[1];
                            String version_enjar=tags[2];
                            String versionCode=version_enjar.substring(0,version_enjar.indexOf("-"));
                            System.out.println(packName);
                            System.out.println(versionCode);

                            String in=s+File.separator+"GlobalConstants.class";
                            String out = s + File.separator + "GlobalConstantsHXY.txt";
                            System.out.println("***in :"+in);
                            System.out.println("***in :" + out);
                            String cmdStr = "cmd /c javap -constants " + in + " > " + out;
                            invokeBat.runbat(cmdStr);

                            File outFile=new File(out);

                            //���ļ�
                            String[] lines = readTXT(out);
                            for (String line : lines) {
                                System.out.println(line);
                                if (line.trim().startsWith(test.G)) {
                                    test.msp_version = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                                    break;
                                }
                            }
                            if(test.msp_version==null||test.msp_version.length()>15){test.msp_version="not found";}
                            System.out.println("msp_version: " + test.msp_version);
                            String sql_1="UPDATE app_info.`pack_only_copy_7.22` \n" +
                                    "SET sdk_version='"+test.msp_version.trim()+"'," +"decode_app='true',has_sdk='true' "+
                                    "WHERE package_name='"+packName+"'\n"
                                   ;
                            stmt.executeUpdate(sql_1);
                            //查询表二有无重复元素（区分版本号）
                            String sql_end="SELECT app_versioncode FROM app_info.`msp_table_copy_7.27_copy` WHERE package_name='"+packName+"'\n";
                            ResultSet set=stmt.executeQuery(sql_end);
                            boolean bool=false;
                            while(set.next()){
                                String query=set.getString(1);
                                if(query.equals(vc)||query.substring(query.indexOf(":")+1,query.indexOf(")")).trim().equals(vc)){
                                    bool=true;
                                    System.out.println("*********");
                                }
                            }
                            if(!bool) {
                                String sql_first = "INSERT INTO app_info.`msp_table_copy_7.27_copy`(package_name,app_name,app_version,app_versioncode,app_size,app_url,msp_version,app_icon)\n" +
                                        "VALUE\n" +
                                        "('" + pn + "','" + appName + "','" + vc + "','" + versionCode + "','" + size + "','" + appUrl + "','"+ test.msp_version.trim() + "','" + icon + "');";
                                stmt.executeUpdate(sql_first);
                            }
                            test.msp_version=null;
                        }
                    }
                }
                if(isContain){
                    isContain=false;
                }else {
                    String sql6="UPDATE app_info.`pack_only_copy_7.22` \n" +
                            "SET decode_app='true',has_sdk='false' "+
                            "WHERE package_name='"+pn+"'\n"
                            ;
                    stmt.executeUpdate(sql6);
                }

            }

            has_sdk="";
            decode_app="";
          //  pacPath.clear();
            System.out.println("This app finish deteck msp_version");



          //  System.out.println(app_verison);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
           e.printStackTrace();
        }

        //  DownloadUtils.download(requestUrl
       //           , appName + "_" + pn + "_" + vc + ".apk", FileDirBase + File.separator + folerName, 1);
            return -1;
    }

    public static void spiderUrl(HtmlCleaner cleaner,ArrayList<DownloadItem> itemList,URL url){
        // HtmlCleaner cleaner=new HtmlCleaner();
        // itemList=new ArrayList<DownloadItem>();

        TagNode node= null;
        try {
            node = cleaner.clean(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TagNode[] title=node.getElementsByName("a", true);
        for(TagNode t:title) {
            String s=t.getText().toString();
                //System.out.println("s:"+s+"\n");
            String base= null;
            String s1=null;
            try {
                base = new String("安装".getBytes("gbk"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(s.equals(base)) {
                System.out.println("**s:"+s);
                String download_name=t.getAttributeByName("download");
                String download_detail= t.getAttributeByName("href");
                String download_url=download_detail.replace(";", "&");

                DownloadItem downloadItem=new DownloadItem();
                downloadItem.setDownload_detail(download_detail);
                downloadItem.setDownload_name(download_name);
                downloadItem.setDownload_url(download_url);
                String[] mesg=download_detail.split(";");
                String md5=mesg[3].substring(mesg[3].indexOf("=") + 1, mesg[3].indexOf("&"));
                String pn=mesg[2].substring(mesg[2].indexOf("=") + 1, mesg[2].indexOf("&"));
                String vc=mesg[5].substring(mesg[5].indexOf("=")+1,mesg[5].indexOf("&"));
                String folderName= String.valueOf(pn.charAt(pn.lastIndexOf(".")+1)).toLowerCase();
                downloadItem.setMd5(md5);
                downloadItem.setPn(pn);
                downloadItem.setVc(vc);
                downloadItem.setFolderName(folderName);
                //add
                String id=mesg[4].substring(mesg[4].indexOf("=") + 1, mesg[4].indexOf("&"));
                String size=mesg[6].substring(mesg[6].indexOf("=") + 1, mesg[6].indexOf("&"));
                String icon=mesg[8].substring(mesg[8].indexOf("=") + 1, mesg[8].indexOf("&"));
                downloadItem.setApkid(id);
                downloadItem.setSize(size);
                downloadItem.setIcon(icon);
                itemList.add(downloadItem);
                System.out.println("调试:itemList.size;"+itemList.size());

                System.out.println(download_name + "," + download_detail + "    \n" + download_url + "\n");
            }
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
