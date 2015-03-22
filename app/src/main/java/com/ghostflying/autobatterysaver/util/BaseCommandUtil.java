package com.ghostflying.autobatterysaver.util;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Ghost on 3/23/2015.
 */
public class BaseCommandUtil {
    static void runCommandWithRoot(String command){
        try{
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes(command);
            os.writeBytes("exit\n");
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
