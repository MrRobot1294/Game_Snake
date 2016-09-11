package cjy.njit.nj.snake;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.ImageIcon;

public class Egg {
	private int x;
	private int y;

	private Thread eggThread;

	public Egg(Garden garden) {
		Random random = new Random();

		x = random.nextInt(garden.getColumn());
		y = random.nextInt(garden.getRow());

		while (!beOverlap(garden)) {
			x = random.nextInt(garden.getColumn());
			y = random.nextInt(garden.getRow());
		}
		
		eggThread = new Thread(new EggRunable(garden));
		eggThread.start();
	}

	public Egg(int x, int y, Garden garden) {
		if (x < garden.getColumn() && y < garden.getRow() && x >= 0 && y >= 0) {
			this.x = x;
			this.y = y;
		} else {
			Random random = new Random();

			this.x = random.nextInt(garden.getColumn());
			this.y = random.nextInt(garden.getRow());

			while (!beOverlap(garden)) {
				x = random.nextInt(garden.getColumn());
				y = random.nextInt(garden.getRow());
			}
		}
		
		eggThread = new Thread(new EggRunable(garden));
		eggThread.start();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Egg getEgg() {
		return this;
	}

	public boolean beOverlap(Garden garden) {
		ListIterator<Snake> snakeListIterator = garden.getSnakes().listIterator();
		while (snakeListIterator.hasNext()) {
			Snake snake = (Snake) snakeListIterator.next();
			for (int i = 0; i < snake.getsnake().size(); i++) {
				if (snake.getsnake().get(i).getX() == x && snake.getsnake().get(i).getY() == y) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean beAte(Garden garden) {
		ListIterator<Snake> snakeListIterator = garden.getSnakes().listIterator();
		while (snakeListIterator.hasNext()) {
			Snake snake = (Snake) snakeListIterator.next();

			if (snake.eat(this)) {
				snake.setGrowflag(true);
				return true;
			}
		}
		return false;
	}
	
	public void relocate(Garden garden) {
		Random random = new Random();

		x = random.nextInt(garden.getColumn());
		y = random.nextInt(garden.getRow());

		while (!beOverlap(garden)) {
			x = random.nextInt(garden.getColumn());
			y = random.nextInt(garden.getRow());
		}
	}

	public void paint(Garden garden, Graphics g) {
		Image egg = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/Egg.gif").getImage();
		g.drawImage(egg, x * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
				y * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2,
				(x + 1) * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
				(y + 1) * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2, 0, 0, 500, 500,
				garden);
	}

	private class EggRunable implements Runnable {
		private Garden garden;

		public EggRunable(Garden garden) {
			super();
			this.garden = garden;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (garden.getSignal().getSnakeNum() != 0) {
				if (garden.getSignal().getSnakePEggFlag() == (garden.getEggs().indexOf(getEgg()) + 1)) {
					if (beAte(garden)) {
						garden.getSignal().getEggAlive().set(garden.getEggs().indexOf(getEgg()), false);
						garden.getSignal().setEggNum(garden.getSignal().getEggNum() - 1);
						garden.getSignal().setSnakePEggFlag(garden.getSignal().getSnakePEggFlag() - 1);
						
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
												
						relocate(garden);
						garden.getSignal().setEggNum(garden.getSignal().getEggNum() + 1);
						garden.getSignal().getEggAlive().set(garden.getEggs().indexOf(getEgg()), true);
					} else {
						garden.getSignal().setSnakePEggFlag(garden.getSignal().getSnakePEggFlag() - 1);
					}		
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
