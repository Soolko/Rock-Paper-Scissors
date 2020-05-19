package rps.server

import rps.Option
import rps.Session
import java.io.Closeable
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

class Server : Session(), Closeable
{
	val socket: ServerSocket = ServerSocket(27015)
	val client: Socket
	
	init
	{
		print("Awaiting connection...")
		client = socket.accept()
		println("Connected!")
	}
	
	@Throws(IOException::class)
	override fun close()
	{
		client.close()
		socket.close()
	}
	
	override fun round(): RoundStatus
	{
		// Get opponent choice
		print("Awaiting response from opponent...")
		val input = ObjectInputStream(client.getInputStream())
		val opponent = Option.byteToObject(input.readObject() as Byte)
		if(opponent == null) error("Opponent sent invalid choice.")
		else println("Received.")
		
		// Get your choice
		val yours = Option.ask()
		
		// Check
		val status: RoundStatus = when
		{
			yours.beats == opponent -> RoundStatus.Won
			opponent.beats == yours -> RoundStatus.Lost
			else -> RoundStatus.Drawn
		}
		val output = ObjectOutputStream(client.getOutputStream())
		output.writeObject(status.ordinal)
		
		// Cleanup
		input.close()
		output.close()
		
		return status
	}
}