package com.south.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 描述:  启动类
 **/

public class WebSocketServer {
    public static void main(String[] args) throws InterruptedException {
        //创建主从线程池
        EventLoopGroup mainGroup=new NioEventLoopGroup();
        EventLoopGroup subGroup=new NioEventLoopGroup();

        try{
            //创建服务器类
            ServerBootstrap server=new ServerBootstrap();
            server.group(mainGroup,subGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitialzer());
            ChannelFuture future = server.bind(8888).sync();
            future.channel().closeFuture().sync();
        }finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
