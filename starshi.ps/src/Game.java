

public class Game {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int progress = 0; //a counter that denotes how many barriers have been passed
		int totalBarriers = 210; //the total number of barriers
		Barrier[] obstacles = new Barrier[totalBarriers]; //an array of Barriers
		
		int initial = 700; //the initial crosstime
		int finpt = initial / 10; //the crosstime if the player makes it to barrier 210
		
		long runtime = System.currentTimeMillis(); //immutable clock
		long comp = System.currentTimeMillis(); //mutable clock for local comparisons
		int fortyFiveMinutes = 1000 * 60 * 45;
		int bl = -1;
		int cr = -1; //dummy variables for blinkTime and crossTime fetching from Barrier
		
		boolean collision = false; //boolean to check for collisions
		
		//loadStartScreen(); //as-of-yet-unfulfilled lifecycle management piece
		
		
		
			//int curTime = 0;
			
		
		populate(totalBarriers, obstacles, initial, finpt); //fills up the Barrier array
		
		while (collision == false && elapsed(runtime) < fortyFiveMinutes)
		{
			double d = 0.0; //tracking variable for crossing Barriers
			int y = Zen.getMouseY() > ht()/2 ? Zen.getMouseY() : ht()/2; //grabs a vertical position for
//the GamePiece, confining it to the lower half of the screen.
			
			
			drawField(0, 0, 0); //draws the background in a white-to-black gradient
			GamePiece.draw(y); //draws the gamepiece
			
			int block = Barrier.getBlockSize(); //fetches the blockSize
			//int start = Barrier.getStartPoint(); //vestige that might be reintroduced
			
			if (progress >= totalBarriers) break; //makes sure the game ends
			
			if (obstacles[progress].getplaced() == false && obstacles[progress].getcompleted() == false)
			//enters if the barrier is uninitiated
			{
				comp = System.currentTimeMillis(); //grabs the local time to make comparisons
				bl = obstacles[progress].blinkTime;
				cr = obstacles[progress].crossTime;
				
				obstacles[progress].truthifyplaced(); //sets placed to true
			}
			else if (obstacles[progress].getplaced() == true && obstacles[progress].getcompleted() == false)
			//enters if the barrier is initiated, but has not traversed the entire screen
			{
				if (elapsed(comp) > 0 && elapsed(comp) < bl)
				//covers the interval between initiation 
				{
					if (elapsed(comp) < bl / 7 || (elapsed(comp) > 2 * bl / 7 && elapsed(comp) <  3 *bl/7) || (elapsed(comp) > 5*bl/7 && elapsed(comp) < 6*bl/7))
					//ensures three blinks
					{
						obstacles[progress].draw(wd() - block); //draws the block at the end of the screen
					}
				}
				else if (elapsed(comp) > bl)
				//covers the period after blinkTime has elapsed
				{
					if (elapsed(comp) < bl + cr)
					//if crossTime hasn't completed yet
					{
						d = (double) (elapsed(comp) - bl) / cr; //converts the times into a ratio...
						obstacles[progress].draw(d); //...and plugs it into the draw feature
					}
					else if (elapsed(comp) >= bl + cr)
					//if the Barrier has hit the left side of the screen
					{
						obstacles[progress].truthifycompleted(); //sets completed to true
						progress++; //increments progress; the cycle repeats
					}
				}
			}
			
			if (collision((wd() / 2) - (GamePiece.size() / 2), y, GamePiece.size(), (int) ((1 - d) * Barrier.startPoint), ht()/2, Barrier.blockSize, obstacles, progress) == true)
			//collision check
			{
				drawField(155, 48, 255); //draws the death screen on a white-purple gradient
				collision = true; //registers the collision
			}
		}
		

	}
	
	public static void populate(int count, Barrier[] fill, int init, int fin) {
		for (int i = 0; i < count; i++)
		{
			int c = (int) (init - (init - fin)/(count+1)*i);
			int b = (int) (0.777 * c);
			fill[i] =  new Barrier(b, c);
			
			boolean trig = false;
			int ran = (int) (Math.random() * 100);
			if (ran < 12.5*i) trig = true;
			
			boolean[] input = new boolean[5];
			String s = "";
			for (int j = 0; j < input.length; j++)
			{
				if (trig == true)
				{
					if (Math.random() < .5 + (i * i)/(count * count)*.46)
					{
						input[j] = true;
						s += "1";
					}
					else 
					{
						s += "0";
					}
					if (j == input.length - 1)
					{
						if (s.equals("11111"))
						{
							input[(int) (Math.random() * 5)] = false;
							String q = "";
							for (int w = 0; w < input.length; w++)
							{
								if (input[w] == true) q += 1;
								else
								{
									q += 0;
								}
							}
							s = q;
						}
						else if (s.equals("00000"))
						{
							String q = "";
							for (int p = 0; p < input.length; p++)
							{
								if (Math.random() < .7)
								{
									input[j] = true;
									q += "1";
								}
								else
								{
									q += 0;
								}
							}
							s = q;
						}
						
					}
				}	
				else if (trig == false)
				{
					if (i % 2 == 0)
					{
						if (j == 0 || j == 3 || j == 4) 
						{
							input[j] = true;
							s += ("1");
						}
						else
						{
							s += ("0");
						}
					
					}
					else
					{
						if (j == 1 || j == 2)
						{
							input[j] = true;
							s += ("1");
						}
						else
						{
							s += ("0");
						}
					}
				}
			}
			TextIO.putln(i + ": " + s);
			fill[i].setcoverage(input);
			
		}
	}
	
	public static void drawField(int r, int g, int b)
	{
			Zen.flipBuffer();
			for (int a = 0; a < ht(); a++)
			{
				Zen.setColor(255 - a*(255-r)/ht(), 255 - a*(255-g)/ht(), 255 - a*(255-b)/ht());
				Zen.fillRect(0, a, wd(), 1);
			}
	}
	
	public static int w()
	{
		return Zen.getZenWidth();
	}
	
	public static int h()
	{
		return Zen.getZenHeight();
	}
	
	public static int elapsed(long c)
	{
		return (int) (System.currentTimeMillis() - c);
	}
	
	public static boolean collision(int pieceX, int pieceY, int block1, int barX, int barY, int block2, Barrier[] a, int prog)
	{
		if (overlap(pieceX, block1, barX, block2) == true)
		{
			for (int i = 0; i < 5; i++)
			{
				if (a[prog].filled(i) == true)
				{
					if (overlap(pieceY, block1, barY + i*Barrier.getBlockSize(), block2) == true) 
						{
							drawField(155, 48, 255);
							GamePiece.draw(pieceY);
							for (int n = 0; n < 5; n++)
							{
								if (a[prog].filled(n) == true)
								{
									Zen.setColor(255, 153, 255);
									Zen.fillRect(barX, barY + n*Barrier.getBlockSize(), block2, block2);
								}
							}
							TextIO.putln("Hit @ block " + (prog + 1));
							return true;
						}
				}
				
			}
			
		}
		return false;
	}
	
	public static boolean overlap(int start1, int range1, int start2, int range2)
	{
		if (start1 >= start2 && start1 <= start2 + range2) return true;
		if (start2 >= start1 && start2 <= start1 + range1) return true;
		if (start1 + range1 >= start2 && start1 + range1 <= start2 + range2) return true;
		if (start2 + range2 >= start1 && start2 + range2 <= start1 + range1) return true;
		return false;
	}
	
	public static void loadStartScreen()
	{
		
	}
	
	public static int ht()
	{
		return Zen.getZenHeight();
	}
	
	public static int wd()
	{
		return Zen.getZenWidth();
	}
	
	
	

}
