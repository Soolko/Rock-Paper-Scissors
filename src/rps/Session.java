package rps;

import rps.client.Client;
import rps.server.Server;

import java.io.Closeable;
import java.io.IOException;

public abstract class Session implements Closeable
{
	public static void main(String[] args) throws IOException
	{
		String address;
		
		System.out.print("Connect to (Leave blank for host): ");
		address = Workarounds.getInput().trim();
		
		final Session session;
		if(address.isEmpty()) session = host();
		else session = connect(address);
		
		RoundStatus status = session.round();
		session.close();
		
		System.out.println(status.message);
	}
	
	private static Session host() throws IOException { return new Server(); }
	private static Session connect(String address) throws IOException { return new Client(address); }
	
	public abstract RoundStatus round();
	
	public enum RoundStatus
	{
		Won("You won."),
		Drawn("It's a draw."),
		Lost("You lost.");
		
		final String message;
		
		RoundStatus(String message) { this.message = message; }
	}
}
