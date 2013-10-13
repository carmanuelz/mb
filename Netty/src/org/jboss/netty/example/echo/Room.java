package org.jboss.netty.example.echo;

public class Room {
	private User PlayerA;
	private User PlayerB;
	private boolean Full = false;
	
	private int ID;
	
	public Room(User Player){
		PlayerA = Player;
	}
	
	public void JoinPlayer(User Player){
		PlayerB = Player;
		Full = true;
	}
	
	public boolean stade(){
		return Full;
	}
	
	public int getID(){
		return ID;
	}

}
