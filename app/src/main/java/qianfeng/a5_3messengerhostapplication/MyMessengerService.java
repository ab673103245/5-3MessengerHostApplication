package qianfeng.a5_3messengerhostapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import sang.User;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class MyMessengerService extends Service {

    // 在onCreate方法中，初始化好一个Messenger实例,毫无疑问传参用到的是Handler实例
    private Messenger messenger;

    @Override
    public void onCreate() {
        super.onCreate();

        messenger = new Messenger(new Handler(){ // new Messenger()

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 0:
                        // 客户端发送的消息，服务端能收到
                        //获取从客户端发来的消息
                        Log.d("google-my:", "handleMessage: arg1:" + msg.arg1 + ";arg2:" + msg.arg2);

                        int arg1 = msg.arg1; // msg是从客户端发送过来的消息
                        int arg2 = msg.arg2;
                        Bundle data = msg.getData();
                        User user = (User) data.getSerializable("user"); // 这个User类的包要强调和客户端的包名一模一样，就直接从客户端拷贝过来就可以了。
                        Log.d("google-my:", "handleMessage: user------->" + user.toString());
                        Messenger replyTo = msg.replyTo;

                        Message msg2 = Message.obtain();
                        msg2.arg1 = arg1 + arg2;
                        msg2.what = 1;
                        try {
                            replyTo.send(msg2);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {  // 这个Handler会被在这里，打包成
        return messenger.getBinder(); // 注意返回的这个是一个IBinder对象
    }
}
