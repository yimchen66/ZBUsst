package com.example.zbusst.Util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @Author 陈一鸣
 * @ClassName UpLoadToUpYun
 * @Description 上传文件至又拍云
 * @date 2022/10/27 9:29
 */
public class UpLoadToUpYun {
    public static final String CHEN_PASSWORD = "T1u6T5SgDa84mag06pX09BiAq17tu06B";
    public static final String PATH_HTTP = "http://v0.api.upyun.com";
    public static final String PATH_FILE = "/zbusst-image/test/";
    public static final String METHOD = "PUT";
    public static String date = getRfc1123Time();
    private static String signature;
    private static String authorization;
    public static int code_result = 400;

    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * @Description: TODO
     * @DateTime:
     * @Params: myfile 上传文件路径
     * @Params: picname 又拍云存储文件名称
     * @Return getResponseCode() code
     */
    public static int getuploadResult(File myfile,String picname) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                String password_md5 = EnCodeToolsUtil.getMd5(CHEN_PASSWORD);
                String msg = METHOD+"&"+PATH_FILE+picname+"&"+date;
                byte[] bys = EnCodeToolsUtil.HmacSHA1Encrypt(msg,password_md5);
                signature = EnCodeToolsUtil.getBase64Encode(bys);
                authorization = "UPYUN chen:"+signature;
                URL url = null;
                try {
                    url = new URL(PATH_HTTP+PATH_FILE+picname);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod(METHOD);
                    conn.setUseCaches(false);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Authorization",authorization);
                    conn.setRequestProperty("date",date);
                    conn.connect();
                    FileInputStream fis = new FileInputStream(myfile);
                    BufferedInputStream bis = new BufferedInputStream(fis);

                    OutputStream os = conn.getOutputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    while((len = bis.read(bytes)) != -1){
                        os.write(bytes,0,len);
                    }
                    os.close();
                    fis.close();
                    code_result = conn.getResponseCode();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//            }
//        }).start();
        return code_result;
    }

    public static String getRfc1123Time() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}
