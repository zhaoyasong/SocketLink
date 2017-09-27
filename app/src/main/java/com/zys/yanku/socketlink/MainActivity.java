package com.zys.yanku.socketlink;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.socket_chat.chat_init.Cfg;
import com.example.administrator.socket_chat.chat_init.ChatConnectionUtils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class MainActivity extends AppCompatActivity {
    //保存socket的channel
    private Channel socketChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化socket连接
        initSocket();
        //初始化完成之后需要开始socket连接
        socketConnect("120.xx.x.120", 2346);

        //发送消息 将要发送的消息保存成一个bean类 然后再将bean类转换成json字符串发送给服务器
        sendMeesage(socketBean);
    }

    /**
     * 发送消息的方法
     *
     * @param socketBean
     */
    private void sendMessage(SocketBean socketBean) {
        String content = new Gson().toJson(socketBean);
        socketChannel.writeAndFlush(content).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                socketHandler.obtainMessage(Cfg.SEND_SUCCESS).sendToTarget();
            }
        });
    }

    /**
     * 开启socket连接
     * 需要传入服务器的ip和端口号
     *
     * @param ip
     * @param port
     */
    private void socketConnect(String ip, int port) {
        ChatConnectionUtils.getInstance().setIp(ip, port);
        ChatConnectionUtils.getInstance().connect(socketHandler);
    }

    /**
     * 初始化socket连接
     */
    private void initSocket() {
        ChatConnectionUtils.getInstance().getChannel(new ChatConnectionUtils.GainChannel() {

            @Override
            public void gainChannel(Channel channel) {
                socketChannel = channel;
            }
        });
    }

    /**
     * 创建socket消息的Handler
     */
    private Handler socketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case Cfg.EXCEPTION:
                    //socket连接异常的回调
                    break;
                case Cfg.ONLINE:
                    //socket连接成功在线的回调
                    break;
                case Cfg.REVICE_SUCCESS:
                    //接受消息成功 首先要判断是否是自己的连麦消息
                    String m = String.valueOf(msg.obj);
                    //为了防止json串的UTF-8 BOM头问题 所以需要添加筛选条件
                    if (m != null && m.startsWith("\ufeff")) {
                        m = m.substring(1);
                    }

                    //根据获取的消息判断是否是自己需要接受判断的消息

                    break;

                case Cfg.SEND_SUCCESS:
                    //消息发送成功

                    break;
                case Cfg.SEND_TEXT:
                    break;
                case Cfg.ACTIVI:
                    //在线状态
                    break;
                case Cfg.INACTIVI:
                    //离线状态
                    break;
            }
        }
    };
}
