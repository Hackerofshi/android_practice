package com.shixin.task;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shixin on 2017/4/6 0006.
 */

public class MultiAsynctaskNetwork extends MultiAsyncTask<Void,Integer,String> {

    public MultiAsynctaskNetwork(NetworkInterface mNetworkInterface) {
        this.mNetworkInterface = mNetworkInterface;
    }

    private NetworkInterface mNetworkInterface;

    @Override
    public String executorTask(Void... voids) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://community.nug-hospital.com:8085/communityhealthNew/admin/ws/nurse/findNurseReserveHislist?tokenid=031e6a0533e549eb9d5978fb964d1559&officeId=cabef69eb34240828a0073009913f16d&pageNo=2&nurseId=e4571830623e4bd1b73620f4207aaeb8");
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            int contentLength = connection.getContentLength();
            System.out.println("-----"+contentLength);
            if (responseCode == 200) {
                int len = 0;
                int count = 0;

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                while ((len = inputStream.read(buffer)) != -1) {
                    count+=len;
                    System.out.println("-----"+count);
                    arrayOutputStream.write(buffer, 0, len);
                }
                inputStream.close();
                arrayOutputStream.flush();
                inputStream.close();
                return new String(arrayOutputStream.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return "请求网络失败";

    }

    @Override
    protected void onResult(String s) {
        mNetworkInterface.onResult(s);
    }
}
