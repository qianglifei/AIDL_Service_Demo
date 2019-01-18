package com.qlf.aidl_service_demo;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qlf.aidl_client_demo.Info;
import com.qlf.aidl_client_demo.MessageCenter;

import java.util.ArrayList;
import java.util.List;

public class AIDLService extends Service {
    public final String TAG = this.getClass().getSimpleName();

    private List<Info> messages = new ArrayList<>();

    private final MessageCenter.Stub messageCenter = new MessageCenter.Stub() {
        @Override
        public List<Info> getInfo() throws RemoteException {
            synchronized (this){
                Log.e(TAG, "getInfo invoking getInfo() method , now the list is : " + messages.toString());
                if (messages != null){
                    return messages;
                }
            }
            return new ArrayList<>();
        }

        @Override
        public void addInfo(Info info) throws RemoteException {
            synchronized (this){
                if (messages == null){
                    messages = new ArrayList<>();
                }

                if (info == null){
                    Log.e(TAG, "Info is null in In");
                    info = new Info();
                }
                //尝试修改的参数，主要为了观察其到客户端的反馈
                //info.setContent("qlf");
                if (!messages.contains(info)){
                    messages.add(info);
                }
                //打印mBooks列表，观察客户端传来的值
                Log.i(TAG, "客户传来了数据" + messages.toString());

            }
        }
    };

    @Override
    public void onCreate() {
        Info message = new Info();
        message.setContent("消息");
        messages.add(message);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getSimpleName(),String.format("onbind,intent = %s",intent.toString()));
        return messageCenter;
    }
}
