package plus;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class FridaCheck {


//    public static boolean fridaAuth(){
//
//        for (int i = 0; i < 65535; i++){
//            String a = "\\x00";
//            String s = "AUTH\r\n";
//        }
//    }

    public static boolean checkFridaLib(){
        boolean isExist = false;
        try {
            File maps = new File("/proc/" + Process.myPid() + "/maps");

            if (!maps.exists()){
                return false;
            }

            FileReader fr = new FileReader(maps);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                if (line.contains("frida")){
                    isExist = true;
                }
                Log.i("adTest", "line===>"+line);
            }
            br.close();
            fr.close();
        }catch (Throwable throwable){

        }
        return isExist;
    }

    //Check frida
    public static boolean checkFridaProcesse(Context context) {
        boolean returnValue = false;
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            // Get currently running application processes
            List<ActivityManager.RunningServiceInfo> list = manager.getRunningServices(300);
            if(list != null){
                String tempName;
                for(int i=0; i<list.size(); ++i){
                    tempName = list.get(i).process;
                    if(tempName.contains("fridaserver")) {
                        returnValue = true;
                    }
                }
            }
        }catch (Throwable throwable){
        }
        return returnValue;
    }

    public static boolean isFridaRunning(){
        return isLocalPortUsing(27047) || isLocalPortUsing(23946);
    }

    /**
     * 测试本机端口是否被使用
     * @param port
     * @return
     */
    public static boolean isLocalPortUsing(int port) {
        boolean flag = true;
        try {
            //如果该端口还在使用则返回true,否则返回false,127.0.0.1代表本机
            flag = isPortUsing("127.0.0.1", port);
        } catch (Throwable e) {
        }
        return flag;
    }

    /***
     * 测试主机Host的port端口是否被使用
     * @param host
     * @param port
     */
    private static boolean isPortUsing(String host, int port) {
        boolean flag = false;
        try {
            InetAddress Address = InetAddress.getByName(host);
            Socket socket = new Socket(Address, port);  //建立一个Socket连接
            flag = true;
        } catch (Throwable e) {
        }
        return flag;
    }
}
