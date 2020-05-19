package rps;

import rps.client.Client;
import rps.server.Server;

import java.io.Closeable;
import java.io.IOException;
import java.util.Scanner;

public abstract class Session implements Closeable
{
	private static Session session;
	public static Session getSession() { return session; }
	
	public static void main(String[] args) throws IOException
	{
		String address;
		try(final Scanner sc = new Scanner(System.in))
		{
			System.out.print("Connect to (Leave blank for host): ");
			address = sc.nextLine().trim();
		}
		
		if(address.isEmpty()) session = host();
		else session = connect(address);
		
		RoundStatus status = session.round();
		session.close();
	}
	
	private static Session host() throws IOException { return new Server(); }
	private static Session connect(String address) throws IOException { return new Client(address); }
	
	public abstract RoundStatus round();
	
	public enum RoundStatus { Won, Lost, Drawn }
}
