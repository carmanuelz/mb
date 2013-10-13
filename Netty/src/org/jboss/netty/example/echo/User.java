package org.jboss.netty.example.echo;
import io.netty.channel.Channel;

public class User {
	private Channel Channel;
	
	public User(Channel channel){
		Channel = channel;
	}
	public Channel getChannel(){
		return Channel;
	}
}
