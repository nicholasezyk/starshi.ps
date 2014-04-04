
public class GamePiece {
	public static int size()
	{
		return Barrier.blockSize / 3;
	}
	
	public static void draw(int r)
	{
		
		if (r < Game.h() / 2)
		{
			r = Game.h() / 2;
			//automatically sets the gamePiece to the bottom half of the screen
			//technically redundant, but whatever
		}
		
		Zen.setColor(176, 224, 230);
		Zen.fillRect((Game.w() / 2) - (size() / 2), r, size(), size());
		//draws a blue game piece
	}
}
