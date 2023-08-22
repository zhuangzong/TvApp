package org.tvapp.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils = new OkHttpUtils();
    public final static int CONNECT_TIMEOUT =60*5;
    public final static int READ_TIMEOUT=100*5;
    public final static int WRITE_TIMEOUT=60*5;
    private static OkHttpClient okHttpClient;
    public final static String TAG="OkHttpUtils";
    private final Handler mHandler;


    static OkHttpUtils getInstance() {
        if (okHttpUtils==null){
            okHttpUtils=new OkHttpUtils();
        }
        return okHttpUtils;
    }


    private OkHttpUtils() {
        okHttpClient=new OkHttpClient();
        okHttpClient.newBuilder()
                .proxy(Proxy.NO_PROXY)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS).build();

        mHandler=new Handler(Looper.getMainLooper());
    }

    public interface CallBack{
        void requestSuccess(String result) throws Exception;
        void requestFailure(Request request, IOException e);
    }


    public static void get(String url, Map<String,String> params,CallBack callBack){
        getInstance().doGetFromAsync(url,params,callBack);
    }


    public static void post(String url, Map<String,String> params,CallBack callBack){
        getInstance().doPostFromAsync(url,params,callBack);
    }


    public static void upLoadFile(String url,Map<String,Object> params,CallBack callBack){
        getInstance().doUpLoadFile(url,params,callBack);
    }

    public static void downLoadFile(String url,String fileName,String destFileDir,CallBack callBack){
        getInstance().doDownLoadFile(url,fileName,destFileDir,callBack);
    }

    private void doDownLoadFile(String url, String fileName, String destFileDir, final CallBack callBack) {
        File file = new File(destFileDir, fileName);
        if(!new File(destFileDir).exists()){
            boolean mkdirs = new File(destFileDir).mkdirs();
            if(!mkdirs){
                deliverDataFailure(null,new IOException("mkdirs failed"),callBack);
                return;
            }
        }
        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                deliverDataFailure(null,new IOException("delete file failed"),callBack);
                return;
            }
        }
        file = new File(destFileDir, fileName);
        final Request request = new Request.Builder().url(url).build();
        final Call call = okHttpClient.newCall(request);
        File finalFile = file;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                deliverDataFailure(request,e,callBack);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];

                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body() != null ? response.body().contentLength() : 0;
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(finalFile);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    deliverDataSuccess("Download success",callBack);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    deliverDataFailure(request,e, callBack);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }


    private void doUpLoadFile(String requestUrl, Map<String, Object> params, final CallBack callBack) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String key : params.keySet()) {
                Object object = params.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object != null ? object.toString() : "");
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(file, null));
                }
            }
            RequestBody body = builder.build();
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            final Call call = okHttpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, e.toString());
                    deliverDataFailure(request,e,callBack);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body() != null ? response.body().string() : null;
                        deliverDataSuccess(string, callBack);
                    } else {
                        throw new IOException(response+"");
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    private void doGetFromAsync(String url, Map<String, String> params, final CallBack callBack) {
        if (params==null){
            params=new HashMap<>();
        }
        final String doUrl=urlJoint(url,params);
        final Request request= new Request.Builder().url(doUrl).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                deliverDataFailure(request,e,callBack);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String result= Objects.requireNonNull(response.body()).string();
                    deliverDataSuccess(result,callBack);
                }
            }
        });
    }



    private void doPostFromAsync(String url, Map<String, String> params, final CallBack callBack) {
        RequestBody requestBody;
        if (params==null){
            params=new HashMap<>();
        }
        FormBody.Builder builder=new FormBody.Builder();
        for (Map.Entry<String,String> entry :params.entrySet()){
            String key=entry.getKey();
            String value;
            if (entry.getValue()==null){
                value="";
            }else {
                value=entry.getValue();
            }
            builder.add(key,value);
        }
        requestBody=builder.build();
        final Request request=new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                deliverDataFailure(request,e,callBack);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String result= response.body() != null ? response.body().string() : null;
                    deliverDataSuccess(result,callBack);
                }else {
                    throw new IOException(response+"");
                }
            }
        });
    }


    private String urlJoint(String url, Map<String, String> params) {
        StringBuilder endUrl=new StringBuilder(url);
        boolean isFirst=true;
        Set<Map.Entry<String,String>> entrySet=params.entrySet();
        for (Map.Entry<String,String> entry: entrySet){
            if (isFirst&&!url.contains("?")){
                isFirst=false;
                endUrl.append("?");
            }else {
                endUrl.append("&");
            }
            endUrl.append(entry.getKey());
            endUrl.append("=");
            endUrl.append(entry.getValue());
        }
        return endUrl.toString();
    }


    private void deliverDataSuccess(final String result, final CallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                    try {
                        callBack.requestSuccess(result);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void deliverDataFailure(final Request request, final IOException e, final CallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack!=null){
                    callBack.requestFailure(request,e);
                }
            }
        });
    }

    public static String readFile(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
