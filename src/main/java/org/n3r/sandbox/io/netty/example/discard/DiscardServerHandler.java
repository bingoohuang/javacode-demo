package org.n3r.sandbox.io.netty.example.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // ByteBuf in = (ByteBuf) msg;

        try {
            // System.out.print(in.toString(io.netty.util.CharsetUtil.US_ASCII));

            // Echo
            ctx.writeAndFlush(msg); // (1)
        } finally {
            // ReferenceCountUtil.release(msg); // (2)
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}