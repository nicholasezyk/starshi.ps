

public class Game {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int progress = 0;
		int totalBarriers = 210;
		Barrier[] obstacles = new Barrier[totalBarriers];
		int h = Zen.getZenHeight();
		int w = Zen.getZenWidth();
		int initial = 700;
		int finpt = initial - 1;
		
		long runtime = System.currentTimeMillis();
		long comp = System.currentTimeMillis();
		int fortyFiveMinutes = 1000 * 60 * 45;
		int bl = -1;
		int cr = -1;
		
		boolean collision = false;
		
		
			//int curTime = 0;
			
		
		populate(totalBarriers, obstacles, initial, finpt);
		
		while (collision == false && elapsed(runtime) < fortyFiveMinutes)
		{
			double d = 0.0;
			int y = Zen.getMouseY() > h/2 ? Zen.getMouseY() : h/2;
			
			
			drawField(w, h, collision);
			GamePiece.draw(y); 
			
			int block = Barrier.getBlockSize();
			//int start = Barrier.getStartPoint();
			
			if (progress >= totalBarriers) break;
			
			if (obstacles[progress].getplaced() == false && obstacles[progress].getcompleted() == false)
			{
				comp = System.currentTimeMillis();
				bl = obstacles[progress].blinkTime;
				cr = obstacles[progress].crossTime;
				
				obstacles[progress].truthifyplaced();
			}
			else if (obstacles[progress].getplaced() == true && obstacles[progress].getcompleted() == false)
			{
				if (elapsed(comp) > 0 && elapsed(comp) < bl)
				{
					if (elapsed(comp) < bl / 6 || (elapsed(comp) > bl / 2 && elapsed(comp) < 2*bl/3) || elapsed(comp) > 5*bl/6)
					{
						obstacles[progress].draw(w - block);
					}
				}
				else if (elapsed(comp) > bl)
				{
					if (elapsed(comp) < bl + cr)
					{
						d = (double) (elapsed(comp) - bl) / cr;
						obstacles[progress].draw(d);
					}
					else if (elapsed(comp) >= bl + cr)
					{
						obstacles[progress].truthifycompleted();
						progress++;
					}
				}
			}
			
			if (collision((Game.w() / 2) - (GamePiece.size() / 2), y, GamePiece.size(), (int) ((1 - d) * Barrier.startPoint), h/2, Barrier.blockSize, obstacles, progress) == true)
			{
				drawField(w, h, true);
				collision = true;
			}
		}
		

	}
	
	public static void populate(int count, Barrier[] fill, int init, int fin) {
		for (int i = 0; i < count; i++)
		{
			int b = (int) (init - (init - fin)/(count+1)*i);
			int c = (int) (0.777 * b);
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
					if (Math.random() < .5)
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
	
	public static void drawField(int wi, int he, boolean c)
	{
		if (c == false)
		{
			Zen.flipBuffer();
			for (int a = 0; a < he; a++)
			{
				Zen.setColor(a*255/he, a*255/he, a*255/he);
				Zen.fillRect(0, a, wi, 1);
			}
		}
		else if (c == true)
		{
			Zen.flipBuffer();
			for (int a = 0; a < he; a++)
			{
				Zen.setColor(255 - a*(255 - 3)/he, a*(255 - 168)/he, a*(255 - 158)/he);
				Zen.fillRect(0, a, wi, 1);
			}

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
							TextIO.putln("hit");
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
	
	

}
