package rps.server

import rps.Option
import rps.Option.Companion.Paper
import rps.Option.Companion.Rock
import rps.Option.Companion.Scissors
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
		var opponent: Option? = null
		
		print("Awaiting response from opponent...")
		ObjectInputStream(client.getInputStream()).use { stream -> Option.byteToObject(stream.read()) }
		if(opponent == null)
		{
			error("Opponent sent invalid choice.")
		}
		
		// Get your choice
		val yours = Option.ask()
		
		// Check
		val status: RoundStatus = when
		{
			yours.beats == opponent -> RoundStatus.Won
			opponent.beats == yours -> RoundStatus.Lost
			else -> RoundStatus.Drawn
		}
		ObjectOutputStream(client.getOutputStream()).use { stream -> stream.write(status.ordinal) }
		
		return status
	}
}