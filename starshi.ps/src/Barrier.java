

public class Barrier {
	
//	the obstacle object for starshi.ps. Barriers swing left across the screen, predictably
//	and slowly at first, but randomly and quickly later in the game.
	
	private boolean[] coverage = new boolean[5]; //which parts of the screen the Barrier will swipe across.
	final static int blockSize = Zen.getZenHeight() / 10; //how large a block will be one side across
	static int startPoint = Zen.getZenWidth() - blockSize; //the draw position on the screen
	int h = Zen.getZenHeight();
	int w = Zen.getZenWidth();
	
	private boolean placed;
	private boolean completed;
	
	//int startTime; //time in ms after the game starts that this Barrier will appear
	int blinkTime; //time in ms after startTime that this Barrier will blink to warn player
	int crossTime; //time in ms after blinkTime that this Barrier moves left.
	
	void setcoverage(boolean[] input)
	{
		if (input.length == this.coverage.length)
		{
			for (int i = 0; i < input.length; i++)
			{
				this.coverage[i] = input[i];
				//if (i == input.length - 1) javax.swing.JOptionPane.showMessageDialog(null, "coverage set", "coverage set", JOptionPane.ERROR_MESSAGE); 
			}
		}
	}
	
	public static int getBlockSize()
	{
		return blockSize;
	}
	
	public static int getStartPoint()
	{
		return startPoint;
	}
	
	public Barrier(/*int start,*/ int blink, int cross)
	{		
		//startTime = start;
		blinkTime = blink;
		crossTime = cross;
		
		placed = completed = false;
	}
	
	public boolean getplaced()
	{
		return placed;
	}
	
	public void truthifyplaced()
	{
		placed = true;
	}
	
	public boolean getcompleted()
	{
		return completed;
	}
	
	public void truthifycompleted()
	{
		completed = true;
	}
	
	public void draw(int b)
	{
		if (b > 0 && b < Game.w())
		{
			for (int i = 0; i < this.coverage.length; i++)
			{
				if (coverage[i] == true)
					{
						//Zen.flipBuffer();
						Zen.setColor(255, 153, 255);
						Zen.fillRect(startPoint, h/2 + i*h/10, blockSize, blockSize);
					}
			}
		}
		
		
//		long tick = System.currentTimeMillis();
//		
//		this.truthifyplaced();
//		
//		int b = this.blinkTime;
//		int c = this.crossTime;
//		
//		while (this.completed == false)
//		{
//			if ((System.currentTimeMillis() - tick > 0 && System.currentTimeMillis() - tick < b / 4) || (System.currentTimeMillis() - tick > 3*b / 4 && System.currentTimeMillis() - tick < b))
//			{
//				for (int i = 0; i < this.coverage.length; i++)
//				{
//					if (coverage[i] == true)
//					{
//						Zen.flipBuffer();
//						Zen.setColor(255, 153, 255);
//						Zen.fillRect(startPoint, h/2 + i*h/10, blockSize, blockSize);
//					}
//				}
//			}
//			if (System.currentTimeMillis() > tick + b && System.currentTimeMillis() < tick + b + c)
//			{
//				for (int i = 0; i < this.coverage.length; i++)
//				{
//					if (coverage[i] == true)
//					{
//						Zen.flipBuffer();
//						Zen.fillRect((int) (startPoint - ((System.currentTimeMillis() - b - tick) / c)*startPoint), h/2 + i*h/10, blockSize, blockSize);
//					}
//				}	
//			}
//			if (System.currentTimeMillis() > tick + b + c)
//			{
//				this.truthifycompleted();
//			}
//		}
		
	}
	
	public void draw(double d)
	{
		if (d > 0 && d < 1)
		{
			for (int i = 0; i < this.coverage.length; i++)
			{
				if (coverage[i] == true)
					{
						Zen.setColor(255, 153, 255);
						Zen.fillRect((int) ((1 - d) * startPoint), h/2 + i*h/10, blockSize, blockSize);
					}
			}
		}
	}
	
	public int len()
	{
		return this.coverage.length;
	}
	
	public boolean filled(int v)
	{
		if (v >= 0 && v < len())
		{
			return this.coverage[v];
		}
		else return false;
	}
	
}
