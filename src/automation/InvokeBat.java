package automation;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xianyu.hxy on 2015/7/13.
 */
public class InvokeBat {
    public void runbat(String batName){
        try {
            Process ps=Runtime.getRuntime().exec(batName);
            InputStream in=ps.getInputStream();
            int c;
            while ((c=in.read())!=-1){
                System.out.println(c);
            }
            in.close();
            ps.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("child thread done");
    }
    public void runbat(String[] batName){
        try {
            Process ps=Runtime.getRuntime().exec(batName);
            InputStream in=ps.getInputStream();
            int c;
            while ((c=in.read())!=-1){
                System.out.println(c);
            }
            in.close();
            ps.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("child thread done");
    }
    public void runbat(){
        String path = "D:\\tools\\enjarify-master\\enjarify.bat";
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec("cmd.exe /k start " + path);
            //将调用结果打印到控制台上
            InputStream in = process.getInputStream();
            while (in.read() != -1) {
                System.out.println(in.read());
            }
            in.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static int i=0;
    public static void main(String[] args){
        InvokeBat invokeBat=new InvokeBat();
        String appPath="D:\\AA.apk";
        /*
        String cmdStr="cmd /c enjarify "+appPath;
        long start=System.currentTimeMillis();
       // invokeBat.runbat(cmdStr);
        long end=System.currentTimeMillis();
        System.out.println("finish:"+(end-start)/1000+" s");*/


        File file=new File("e:"+File.separator+"temp");
        if(file.exists()&&file.isDirectory()){
            File[] subFile=file.listFiles();
            for(File f:subFile){
                if(f.exists()&&f.isDirectory()){
                    File[] appFile=f.listFiles();
                    for(File app:appFile){
                        if(app.exists()&&app.isFile()&&app.length()>5000){
                            String Path=app.getAbsolutePath();
                            System.out.println((++i)+".path:"+Path);
                            String cmdStr="cmd /c enjarify "+Path;
                            long start=System.currentTimeMillis();
                            invokeBat.runbat(cmdStr);
                            long end=System.currentTimeMillis();
                            System.out.println("finish:"+(end-start)/1000+" s");
                        }
                    }
                }
            }
        }
    }
}
