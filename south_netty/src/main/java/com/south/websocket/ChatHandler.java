package com.south.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;


/**
 * 描述:  用于处理消息的handler
 * 用于它的传输数据的载体是frame,这个frame 在netty中，是用于为websocket专门处理文本对象的，frames是消息载体，（TextWebSocketFrame）
 **/
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //记录和管理所有客户端的channel
    private static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取客户端所传输的消息
        String content = msg.text();
        System.out.println("接收到的数据："+content);

        //将数据刷新到客户端上
        clients.writeAndFlush(
                new TextWebSocketFrame(
                        "[服务器在：]"+ LocalDateTime.now()
                        +"接收到消息，消息内容为："+content
                )
        );
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长ID为："+ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短ID为："+ctx.channel().id().asShortText());
    }
}
