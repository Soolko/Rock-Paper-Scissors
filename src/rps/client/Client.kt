package rps.client

import rps.Option
import rps.Session
import java.io.Closeable
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket

class Client(address: String) : Session(), Closeable
{
	val server: Socket
	
	init
	{
		print("Attempting connection...")
		server = Socket(InetAddress.getByName(address), 27015)
		println("Complete.")
	}
	
	@Throws(IOException::class)
	override fun close()
	{
		server.close()
	}
	
	override fun round(): RoundStatus
	{
		// Send it
		val option = Option.ask()
		val output = ObjectOutputStream(server.getOutputStream())
		output.writeObject(option.byte)
		
		// Receive status
		print("Awaiting opponent's choice...")
		val input = ObjectInputStream(server.getInputStream())
		val ordinal = input.readObject() as Int
		if(ordinal.toInt() == -1) error("Invalid round status received.")
		else println("Received.")
		
		// Cleanup
		output.close()
		input.close()
		
		// Returned flipped server status
		return when(RoundStatus.values()[ordinal.toInt()])
		{
			RoundStatus.Lost -> RoundStatus.Won
			RoundStatus.Drawn -> RoundStatus.Drawn
			RoundStatus.Won -> RoundStatus.Lost
		}
	}
}