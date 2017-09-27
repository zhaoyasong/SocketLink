package com.example.administrator.socket_chat.chat_init;

import android.os.Handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Administrator on 2016/12/15 11:36
 */

public class ChatInit extends ChannelInitializer<SocketChannel> {
    private final Handler handler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new StringEncoder());
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new ChatHandler(handler));

    }

    public ChatInit(Handler handler) {
        this.handler = handler;
    }
}
