/*
 * Copyright 2010 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.netty.example.echo;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ObjectEchoServerHandler extends ChannelInboundHandlerAdapter {
	
	private List<User> Users;
	private List<Room> Rooms;

	private static final Logger logger = Logger.getLogger(
            ObjectEchoServerHandler.class.getName());

    public final AtomicLong transferredMessages = new AtomicLong();
    
    public ObjectEchoServerHandler(List<User> users,List <Room> rooms){
    	Users = users;
    	Rooms = rooms;
    }
    
    @Override
    public void channelRead(
            ChannelHandlerContext ctx, Object msg) throws Exception {
        // Echo back the received object to the client.
    	transferredMessages.incrementAndGet();
    	System.out.println((String)msg);
        //ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
