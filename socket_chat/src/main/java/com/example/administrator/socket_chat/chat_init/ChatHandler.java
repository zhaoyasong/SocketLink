package com.example.administrator.socket_chat.chat_init;

import android.os.Handler;
import android.os.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Administrator on 2016/12/15 11:31
 */

public class ChatHandler extends SimpleChannelInboundHandler<String> {

    final Handler handler;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Message message = handler.obtainMessage(Cfg.EXCEPTION);
        message.obj = cause.getMessage();
        message.sendToTarget();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        Message message = handler.obtainMessage(Cfg.EXCEPTION);
        message.sendToTarget();
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    public ChatHandler(Handler handler) {
        this.handler = handler;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Message message = handler.obtainMessage(Cfg.REVICE_SUCCESS);
        message.obj = s;
        message.sendToTarget();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接开始
        handler.obtainMessage(Cfg.ACTIVI).sendToTarget();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //连接断开
        handler.obtainMessage(Cfg.INACTIVI).sendToTarget();

    }
}
