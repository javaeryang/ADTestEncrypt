package com.example.adtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.codec.digest.DigestUtils;
import com.example.adtest.rsa.RSA;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.javaer.plusxposed.log.Vlog;
import com.javaer.plusxposed.util.net.NetUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    InterstitialAd mInterstitialAd;

    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxuIvR/MQDpNly8CCpiisJwmu79uvQJzgmq4SE6klPj4zIBu1XPedoIg9ADAGPQ+zHmHAhzd6HK++TQKhOMrzQkG90kiTi9QBxdOhYt8RjOBSD9W1FYvMpfDtoYn8Gsgij/26hYz9ri9aqCqxstUDJAcrTOX63jHnc5TGL6Q8DIvoNx5keeXmIWdJ/to5SlNF+Its5+fOXKnMKPu5FfdCwxBoPdFDMUnBrKJhl5oGVZEWNDKtVeh9r4vL1A85i1uu4B7Bll5BGIkAMHx5QY5GtSE87MwVLC/1oyyCJ4812p6xjpINajnRR8r7xzqr2/l82y1vslt2LScYLXyB5ltK4QIDAQAB";
    private static final String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDG4i9H8xAOk2XLwIKmKKwnCa7v269AnOCarhITqSU+PjMgG7Vc952giD0AMAY9D7MeYcCHN3ocr75NAqE4yvNCQb3SSJOL1AHF06Fi3xGM4FIP1bUVi8yl8O2hifwayCKP/bqFjP2uL1qoKrGy1QMkBytM5freMedzlMYvpDwMi+g3HmR55eYhZ0n+2jlKU0X4i2zn585cqcwo+7kV90LDEGg90UMxScGsomGXmgZVkRY0Mq1V6H2vi8vUDzmLW67gHsGWXkEYiQAwfHlBjka1ITzszBUsL/WjLIInjzXanrGOkg1qOdFHyvvHOqvb+XzbLW+yW3YtJxgtfIHmW0rhAgMBAAECggEBAIFEoKCuXqBqs76UeRbQ1txk0dy7OJTRordHLx6yUS+8s/RJrj8n/r9J7gRCMrvbiXvU45O/vlII9LMPY4ULfMNzAZM9cxFuklxn6rzd6oVbhFsgd5vyiujMyVe23TL3JGfc7jBB7N0bkFw/q9Jl262+LynDTaTAYQTiDVMH97Pa0oiodD7egtE5cpF8G6dKKsIYRR8iSqcsl7MdLvszfLARy5mteGWYAHGx1qlueFocLJ/5p4dLcbJTBhAuEnkLq/3MF2a152pFcSfbwnCqfVOGMSdI8k8B/P7E+Kc1dYF75z++S0IrH1tJ/vMqGxrHkUKSplR/KYCdjGw54uoi1fECgYEA5lC/1uVvSox9TMkm030gFQjDBhbbUWIgujIs3VB1h/sUldVAb8gdOs4lPTRrt5Vj+9Cb/adsNOjvOJD3N6rtNeoslA77ywAoHJVia9qgN0qCBbbZI2Bm4+jq6y+P2mUdbBSYvNasnD+s978OHqIXcuM+sv5MNx6x9ghAD0lgnnUCgYEA3RAW7DPWyN+MXQx1Fkuch+cwSTQtnAvtJXYxVzkH08XMmUnRsHUWyHlaoCn17k09ybTVZtGkn0hPoqJLXokHqx2ldh3WpGr1zb2IUZAUXF21sb3wPhdEUxFVJKMQ7oHoIYWZgBM9Yz6kZMuCuYmCSlUubgEErNCldUM0iC0gRT0CgYEA4EyJ2C5PqWmS6cIIpvT31qRW0kpWQzbuqocM/GhMXibfUGCDxZk9JjT3PVkdAohh/C6YjHqNviWQPCWqLbOwGs65pWPGQlgfuF6foGwlllNgt1HwvCw5aAJBLtR+I3JQKh8Q9O+Y4P5+ZDTT+m9fqMq5GDbiYNfq9rHoOTr79rUCgYEA0ivN31uZxPjklkKVv1C7zWm7up1lD4s3N/qbBnYuPuU7lDFerwljU84sn0HFk37UU8Udbte6T8WMEO+EAKj/JWzWNuo/e6pFcAnWZNU6xmZBrG8MzbuJNgCwlliCbIANlnFSczXXk1enLD1gV82olctOjT9ilnr4E/iFJULh/3ECgYB5ECt28l3fZMFTFViVX8qthy1p7JsTzIwPQkHsCSQBxMdjO1Bz7p4GTVpJPtswkXm3rUwDO/UteJOdndeg10eeM8CHbr6kv40XhyBUqyZ4/WVK3MLKQsOtghQCGz1qKC/fQNvi+hokXJKIEFVdBoXbr4cmPbMoK8NNZL7XMHbInw==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8888);
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        
                        try{
//                            Map<String, Object> map = new HashMap<String, Object>();
//                            map = RSA.init();
//                            Vlog.log("公钥："+RSA.getPublicKey(map));
//                            Vlog.log("私钥："+RSA.getPrivateKey(map));


//                            String pub = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pub.txt";
//                            String pri = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pri.txt";
//
//
//                            writeToFile(RSA.getPublicKey(map), pub);
//                            writeToFile(RSA.getPrivateKey(map), pri);
                            //由前四行代码获得公、私密钥



                            String str = "本地加解密测试hello123456";
                            // 公钥加密，私钥解密
                            String enStr1 = RSA.encryptByPublic(str, publicKey);
                            Vlog.log("公钥加密后："+enStr1);

                            String deStr1 = RSA.decryptByPrivate(enStr1, privateKey);
                            Vlog.log("私钥解密后："+deStr1);


                            // 私钥加密，公钥解密
                            String enStr2 = RSA.encryptByPrivate(str, privateKey);
                            Vlog.log("私钥加密后："+enStr2);


                            String deStr2 = RSA.decryptByPublic(enStr2, publicKey);
                            Vlog.log("公钥解密后："+deStr2);

                            // 产生签名
                            String sign = RSA.sign(enStr2, privateKey);
                            Vlog.log("签名:"+sign);

                            // 验证签名
                            boolean status = RSA.verify(enStr2, publicKey, sign);



                            Vlog.log("状态:"+status);


                            // http://127.0.0.1:10001/api/hello

                            String local_ip = "192.168.1.123";
                            String remote_ip = "47.100.207.253";

                            //单次最长加密长度
                            //明文bytes===>214
                            //明文长度===>130
                            String clear_txt = "testkey";
                            Vlog.log("明文bytes===>" + clear_txt.getBytes().length);
                            Vlog.log("明文长度===>" + clear_txt.length());

                            clear_txt = URLEncoder.encode(clear_txt, "utf-8");

                            String de = NetUtil.getMessage("http://" + local_ip + ":10001/api/encode?key="+clear_txt);

                            Vlog.log("服务加密后===>"+de);


                            Vlog.log("服务加密后然后md5===>"+ DigestUtils.md5Hex(de));

                            String s3 = RSA.decryptByPrivate(de, privateKey);
                            Vlog.log("本地解密后===>"+s3);

                            String url_en = URLEncoder.encode(de, "utf-8");

                            Vlog.log("本地url_en===>"+url_en);

                            String d_de_str = NetUtil.getMessage("http://" + local_ip + ":10001/api/decode?txt="+ url_en);

                            Vlog.log("服务解密后===>"+d_de_str);

                            Vlog.log("====================================================================>");

                            String en_local = RSA.encryptByPublic(clear_txt, publicKey);

                            Vlog.log("本地加密===>"+en_local);

                            en_local = URLEncoder.encode(en_local, "utf-8");

                            String result = NetUtil.getMessage("http://" + local_ip + ":10001/api/core/decrypt?code="+en_local);

                            Vlog.log("返回结果===>"+result);


                            Vlog.log("====================================================================>");


                            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mt.apk";

                            if (NetUtil.download(savePath, "http://" + local_ip + ":10001/api/core/download?code="+en_local)){
                                Vlog.log("下载成功===>");
                            }else {
                                Vlog.log("下载失败===>");
                            }


//                            String en = "O8Mitqlsq+bouWNPttuAgTSLZt6NNv7IAfGcHsTJ6qGFQB0k47PC6BwRFCteJDP5JmMzWVYuJY9Xbd8k85/bZQBtMmdGafuqjul7fdePDC5O+SrdkUJ0Md9+hubT1K11wZWRsSm8R71K2iRF/QhV80xvqRgLNHWkk2/lAs9wBB24JZbUI3/i/XVAt+ymGRgXwAlEeuU3nbhBFg2H01N+UGw0fVc70VlPTd7Q0qDzDA52ICzQbkSR3olIMSvqm8AZ4oe5Hmue3Mz7VdhYTtGKeJGLo7316Iu8k7qsAtYwk8BgP6rma4rQF7wN//sWWN2vmCXkJMeUXHEztIpqp62Vyg==";
//
//                            String de = RSA.decryptByPrivate(en, privateKey);
//
//                            Vlog.log("de===>"+de + "===>"+de.length());


//                            String json = NetUtil.getMessage("http://192.168.1.136:8080/hello");
//
//
//                            Vlog.log("json===>"+json);
//                            if (json != null && !json.isEmpty()){
//                                JSONObject jsonObject = new JSONObject(json);
//                                try {
//                                    String publicKey = jsonObject.optString("pub");
//                                    String privateKey = jsonObject.optString("pri");
//
//                                    String ori = jsonObject.optString("oriStr");
//                                    String encodeStr = jsonObject.optString("encodeStr");
//
//                                    String s1 = RSA.decryptByPrivate(encodeStr, privateKey);
//
//                                    String s2 = RSA.encryptByPrivate(ori, privateKey);
//
//
//                                    Vlog.log("s1===>"+s1);
//                                    Vlog.log("s2===>"+s2);
//
//
//
//
//                                    String de = NetUtil.getMessage("http://192.168.1.136:8080/encode?key=你好");
//
//                                    Vlog.log("de===>"+de);
//
//                                    String s3 = RSA.decryptByPrivate(de, privateKey);
//                                    Vlog.log("s3===>"+s3);
//
//                                    String d_de_str = NetUtil.getMessage("http://192.168.1.136:8080/decode?txt="+de);
//
//                                    Vlog.log("d_de_str===>"+d_de_str);
//                                }catch (Throwable throwable){
//                                    Vlog.log(throwable);
//                                }
//                            }

                        }catch (Throwable throwable){
                            Vlog.log(throwable);
                        }
                    }
                }).start();
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Toast.makeText(MainActivity.this, "初始化完成", Toast.LENGTH_SHORT).show();
            }
        });


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544~3347511713");
        AdRequest request = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(request);


        byte[] e1 = encrypt("hello");
        String de = decrypt(e1);
        Vlog.log("解密后===>"+de);
    }

    public static byte[] encrypt(String strToEncrypt){
        try {
            byte[] data = strToEncrypt.getBytes("utf-8");

            byte[] res = new byte[data.length];
            for (int i = 0; i< data.length; i++){
                res[i] = (byte) (data[i] ^ 1);
            }
            return res;

        }catch (Throwable throwable){

        }
        return null;
    }

    public static String decrypt(byte[] byteToDecrypt){
        try {
            byte[] res = new byte[byteToDecrypt.length];

            for (int i = 0; i< byteToDecrypt.length; i++){
                res[i] = (byte) (byteToDecrypt[i] ^ 1);
            }

            return new String(res);
        }catch (Throwable throwable){
            Vlog.log(throwable);
        }
        return null;
    }

    /**
     * 本地测试
     */
    private void test(){
        try {
            String de = NetUtil.getMessage("http://192.168.1.136:10001/api/encode?key=哈哈9889auaik ajcdao jaiosjaj");

            Vlog.log("服务加密后===>"+de);


            Vlog.log("服务加密后然后md5===>"+ DigestUtils.md5Hex(de));

            String s3 = RSA.decryptByPrivate(de, privateKey);
            Vlog.log("本地解密后===>"+s3);

            String url_en = URLEncoder.encode(de, "utf-8");

            Vlog.log("本地url_en===>"+url_en);

            String d_de_str = NetUtil.getMessage("http://192.168.1.136:10001/api/decode?txt="+ url_en);

            Vlog.log("服务解密后===>"+d_de_str);
        }catch (Throwable throwable){
            Vlog.log(throwable);
        }
    }


    private static void writeToFile(String str, String fileName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                    fileOutputStream.write(str.getBytes());
                } catch (Throwable e) {
                    Vlog.log(e);
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
