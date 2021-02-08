package com.south.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 描述:  助手初始化类
 **/

public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel channel) throws Exception {
        //获取管道
        ChannelPipeline pipeline = channel.pipeline();
        //websocket 基于http协议，所需要的http 编解码器
        pipeline.addLast(new HttpServerCodec());
        //在http上有一些数据流产生提供支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage 进行聚合处理，聚合成request或response
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        /**
         * 处理一些繁重复杂的事情
         * 处理握手动作：handshaking(close,ping,pong) ping+pong=心跳
         * 对于websocket来讲，都是以frams进行传输的，不同的数据类型对应的frams也不同
         */
        pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));

        //自定义handler
        pipeline.addLast(new ChatHandler());
    }
}
