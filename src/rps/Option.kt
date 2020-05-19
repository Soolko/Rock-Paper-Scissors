package rps

class Option(val byte: Byte, val lookup: String, var beats: Option? = null)
{
	companion object
	{
		private var _Rock: Option = Option(1, "R")
		val Paper: Option = Option(2, "P", _Rock)
		val Scissors: Option = Option(3, "S", Paper)
		
		val Rock: Option
		
		init
		{
			_Rock.beats = Scissors
			Rock = _Rock
		}
		
		fun ask(): Option
		{
			var selected: Option? = null
			while(selected == null)
			{
				print("Enter your choice (R/P/S): ")
				
				// Read, if null, restart
				var answer: String = Workarounds.getInput()
				
				// Check and return relevant one
				answer = answer.toUpperCase()
				when
				{
					answer.contains(Rock.lookup) -> selected = Rock
					answer.contains(Paper.lookup) -> selected = Paper
					answer.contains(Scissors.lookup) -> selected = Scissors
				}
			}
			return selected
		}
		
		fun byteToObject(byte: Byte): Option?
		{
			return when(byte)
			{
				Rock.byte -> Rock
				Paper.byte -> Paper
				Scissors.byte -> Scissors
				else -> {
					System.err.println("Invalid byte received from opponent: $byte")
					null
				}
			}
		}
	}
}