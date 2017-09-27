package com.example.administrator.socket_chat.chat_init;

import android.os.Handler;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Administrator on 2016/12/23 12:10
 */

public class ChatConnectionUtils {
    private NioEventLoopGroup group;
    private Channel channel;
    private ChannelFuture channelFuture;
    private static ChatConnectionUtils chatConnectionUtils;
    private String host;
    private int port;
    private GainChannel gainChannel;

    public void setIp(String host, int port) {
        this.host = host;
        this.port = port;

    }

    public static ChatConnectionUtils getInstance() {
        if (chatConnectionUtils == null) {
            chatConnectionUtils = new ChatConnectionUtils();
        }
        return chatConnectionUtils;

    }

    public void getChannel(GainChannel gainChannel) {
        this.gainChannel = gainChannel;
    }

    public void connect(final Handler handler) {

        new Thread() {


            @Override
            public void run() {
                try {
                    group = new NioEventLoopGroup();
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group);
                    bootstrap.channel(NioSocketChannel.class);
                    bootstrap.handler(new ChatInit(handler));
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.option(ChannelOption.TCP_NODELAY, true);
                    channelFuture = bootstrap.connect(new InetSocketAddress(host, port));
                    channel = channelFuture.sync().channel();
                    gainChannel.gainChannel(channel);
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            handler.obtainMessage(Cfg.ONLINE).sendToTarget();
                        }
                    });
                    channel.closeFuture().sync();
                } catch (Exception e) {

                }
            }
        }.start();

    }

    public interface GainChannel {
        void gainChannel(Channel channel);
    }
}
