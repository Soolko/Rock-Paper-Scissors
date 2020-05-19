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
		ObjectOutputStream(server.getOutputStream()).use { stream -> stream.write(Option.ask().byte) }
		
		// Receive status
		var ordinal: Int? = null
		ObjectInputStream(server.getInputStream()).use { stream -> ordinal = stream.read() }
		if(ordinal == null) error("Invalid round status received.")
		
		return RoundStatus.values()[ordinal as Int]
	}
}