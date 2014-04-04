import java.util.Scanner;

public class Game {
	
	private static boolean collision = false; //boolean to check for collisions
	private static int progress = 0; //a counter that denotes how many barriers have been passed
	private static int totalBarriers = 210; //the total number of barriers
	private static Barrier[] obstacles = new Barrier[totalBarriers]; //an array of Barriers
	
	private static int initial = 700; //the initial crosstime
	private static int finpt = initial / 10; //the crosstime if the player makes it to barrier 210
	
	private static int thick = 10;

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
		//int fortyFiveMinutes = 1000 * 60 * 45;
		int bl = -1;
		int cr = -1; //dummy variables for blinkTime and crossTime fetching from Barrier
		
		//boolean collision = false; //boolean to check for collisions
		
		loadStartScreen(); //lifecycle management piece			
		
		populate(totalBarriers, obstacles, initial, finpt); //fills up the Barrier array
		
		//int bet = getBet(); //bet collection system
		
		while (collision == false)
		{
			double d = 0.0; //tracking variable for crossing Barriers
			int y = Zen.getMouseY() > h()/2 ? Zen.getMouseY() : h()/2; //grabs a vertical position for
//the GamePiece, confining it to the lower half of the screen.
			
			
			drawField(0, 0, 0); //draws the background in a white-to-black gradient
			GamePiece.draw(y); //draws the gamepiece
			divide(thick);
			
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
						obstacles[progress].draw(w() - block); //draws the block at the end of the screen
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
			
			if (collision((w() / 2) - (GamePiece.size() / 2), y, GamePiece.size(), (int) ((1 - d) * Barrier.startPoint), h()/2, Barrier.blockSize, obstacles, progress) == true)
			//collision check
			{
				drawField(155, 48, 255); //draws the death screen on a white-purple gradient
				collision = true; //registers the collision
				
				rebirth((w() / 2) - (GamePiece.size() / 2), y, GamePiece.size(), (int) ((1 - d) * Barrier.startPoint), h()/2, Barrier.blockSize, obstacles, progress); //lifecycle management
			}
		}
		

	}
	
	public static void populate(int count, Barrier[] fill, int init, int fin) {
		for (int i = 0; i < count; i++)
		{
			int b = (int) (init - (init - fin)/(count)*i); //steps blinkTime down from init to fin linearly
			int c = (int) (0.777 * b); //sets crossTime as a fraction of blinkTime
			fill[i] =  new Barrier(b, c); //instantiates the Barrier in that slot
			
			boolean trig = false; //decides whether the barriers are randomized
			int ran = (int) (Math.random() * 100);
			if (ran < 12.5*i) trig = true; //barriers necessarily randomize after 8
			
			boolean[] input = new boolean[5]; //the array to be plugged into the Barrier constructor
			String s = ""; //string value that represents the filling of the array
			for (int j = 0; j < input.length; j++)
			{
				if (trig == true)
				//if we're randomizing
				{
					if (Math.random() < .5 + (i * i)/(count * count)*.46)
					//probability of a given block spawning, increasing in square pattern 50-96%
					{
						input[j] = true;
						s += "1";
						//a 1 is a filled barrier
					}
					else 
					{
						s += "0";
						//a 0 is an unfilled barrier
					}
					if (j == input.length - 1)
					//full and empty checks at the end of the array
					{
						if (s.indexOf("0") == -1)
						//if the index is all 1s; works 100% of the time
						{
							input[(int) (Math.random() * input.length)] = false; //one of the positions gets set to false
							String q = ""; //temp string
							for (int w = 0; w < input.length; w++)
							{
								if (input[w] == true) q += "1";
								else
								{
									q += "0";
								}
							}
							s = q;
						}
						else if (s.indexOf("1") == -1)
						//if the index is all 0s; works ~99.992% of the time
						{
							String q = "";
							for (int p = 0; p < input.length; p++)
							{
								if (Math.random() < .7)
								//70% chance of a given  block filling
								{
									input[j] = true;
									q += "1";
								}
								else
								{
									q += "0";
								}
							}
							s = q;
						}
						
					}
				}	
				else if (trig == false)
				//if we aren't randomizing
				{
					if (i % 2 == 0)
					{
						if (j == 0 || j == 3 || j == 4) 
						{
							input[j] = true;
							s += ("1");
							//blocks 0, 3, and 4 fill on an even Barrier
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
							//blocks 1 and 2 fill on an odd Barrier
						}
						else
						{
							s += ("0");
						}
					}
				}
			}
			TextIO.putln(i + ": " + s); //prints the block number and Barrier layout to the console
			fill[i].setcoverage(input); //fills in the Barrier
			
		}
	}
	
	public static void drawField(int r, int g, int b)
	{
			Zen.flipBuffer();
			for (int a = 0; a < h(); a++)
			{
				Zen.setColor(255 - a*(255-r)/h(), 255 - a*(255-g)/h(), 255 - a*(255-b)/h());
				//gradient from white to the RGB input
				Zen.fillRect(0, a, w(), 1);
			}
	}
	
	public static int w() //fetches width because it's shorter
	{
		return Zen.getZenWidth();
	}
	
	public static int h() //fetches height because it's shorter
	{
		return Zen.getZenHeight();
	}
	
	public static int elapsed(long c) //an easier way of determining time elapsed
	{
		return (int) (System.currentTimeMillis() - c);
	}
	
	public static boolean collision(int pieceX, int pieceY, int block1, int barX, int barY, int block2, Barrier[] a, int prog)
	{
	//collision detection; typically used only for the gamePiece (pieceX, Y, and block1)
	//and for the Barriers (barX, Y, and block2)
		if (overlap(pieceX, block1, barX, block2) == true)
		//if there's an overlap in horizontal position...
		{
			for (int i = 0; i < 5; i++)
			{
				if (a[prog].filled(i) == true)
				{
					if (overlap(pieceY, block1, barY + i*Barrier.getBlockSize(), block2) == true)
					//...then we check for a vertical position
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
							//drawing the killscreen, the gamePiece, and the Barriers
							TextIO.putln("Hit @ block " + (prog + 1));
							return true;
						}
				}
				
			}
			
		}
		return false;
	}
	
	public static boolean overlap(int start1, int range1, int start2, int range2)
	//checks for a one-dimensional overlap
	{
		if (start1 >= start2 && start1 <= start2 + range2) return true;
		if (start2 >= start1 && start2 <= start1 + range1) return true;
		if (start1 + range1 >= start2 && start1 + range1 <= start2 + range2) return true;
		if (start2 + range2 >= start1 && start2 + range2 <= start1 + range1) return true;
		return false;
	}
	
	public static void loadStartScreen() //loads the initial screen and waits for a key to start
	{
		boolean start = false;
		while (start == false)
		{
			drawField(238, 64, 0);
			Zen.setFont("Helvetica-33");
			Zen.setColor(255 - 238, 255 - 64, 255 - 0);
			Zen.drawText("Press any key to start", 0, 30);
			String TT = Zen.getEditText(); //opens up text input
			Zen.setEditText(""); //resets the text input after each cycle inside the while loop
			if (TT.length() > 0)
			{
				char t = TT.charAt(0); //grabs the first character
				if ((t >= 'a' && t <= 'z') || (t >= 'A' && t <= 'Z') || (t >= '0' && t <= '9') || t == ' ')
				{
					start = true;
				}
			}
			
		}
		//part of lifecycle management
		
	}
	
	public static void rebirth(int pieceX, int pieceY, int block1, int barX, int barY, int block2, Barrier[] a, int prog)
	//restarts the game
	{
		if (collision == true)
		{
			boolean out = false;
			while (out == false)
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
				Zen.setFont("Helvetica-33");
				Zen.setColor(255 - 155, 255 - 48, 255 - 255);
				Zen.drawText("Continue? Press any key", 0, 30);
				String TT = Zen.getEditText(); //opens up text input
				Zen.setEditText(""); //resets the text input after each cycle inside the while loop
				if (TT.length() > 0)
				{
					char t = TT.charAt(0); //grabs the first character
					if ((t >= 'a' && t <= 'z') || (t >= 'A' && t <= 'Z') || (t >= '0' && t <= '9') || t == ' ')
					{
						out = true;
					}
				}
				
			}
			populate(totalBarriers, obstacles, initial, finpt);
			progress = 0;
			collision = false;
		}
	} //Lifecycle
	
	public static void divide(int thick)
	{
		Zen.setColor(67, 205, 128);
		Zen.fillRect(0, h()/2 - thick, w(), thick);
	}
	
	

}
