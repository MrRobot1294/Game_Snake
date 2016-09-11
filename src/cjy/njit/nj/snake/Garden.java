package cjy.njit.nj.snake;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.Timer;

public class Garden extends Canvas {

	private int row = 12;
	private int column = 18;
	private int block_size = 50;

	private LinkedList<Snake> snakes = new LinkedList<Snake>();
	private LinkedList<Egg> eggs = new LinkedList<Egg>();

	private Image offScreenImage;

	private Signal signal;

	private int delayTime = 1000;
	private Timer timer = new Timer(delayTime, new TimerLinster());

	public Garden() {
		// TODO Auto-generated constructor stub
		setSize(1000, 700);

		timer.start();

		signal = new Signal(1, 1);

		snakes.add(new Snake("Hello World", this));
		eggs.add(new Egg(this));

		addKeyListener(new KeyMonitor());

		// timer.start();
	}

	public Garden(int row, int column, int block_size) {
		super();

		if (row * block_size <= 700 || column * block_size <= 1000) {
			this.row = row;
			this.column = column;
			this.block_size = block_size;
		}

		setSize(1000, 700);

		timer.start();

		signal = new Signal(1, 1);

		snakes.add(new Snake("Hello World", this));
		eggs.add(new Egg(this));

		addKeyListener(new KeyMonitor());

	}

	public Garden getGarden() {
		return this;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getBlock_size() {
		return block_size;
	}

	public void setBlock_size(int block_size) {
		this.block_size = block_size;
	}

	public LinkedList<Snake> getSnakes() {
		return snakes;
	}

	public void setSnakes(LinkedList<Snake> snakes) {
		this.snakes = snakes;
	}

	public LinkedList<Egg> getEggs() {
		return eggs;
	}

	public void setEggs(LinkedList<Egg> eggs) {
		this.eggs = eggs;
	}

	public Signal getSignal() {
		return signal;
	}

	public void setSignal(Signal signal) {
		this.signal = signal;
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		setBackground(new Color(154, 205, 50));

		int i, j;
		Color color = g.getColor();

		g.setColor(new Color(240, 230, 140));
		for (j = ((700 - row * block_size) / 2); j < (700 - ((700 - row * block_size) / 2)); j += (2 * block_size)) {
			for (i = ((1000 - column * block_size) / 2); i < (1000 - ((1000 - column * block_size) / 2)); i += (2
					* block_size)) {
				g.fillRect(i, j, block_size, block_size);
			}
		}
		for (j = ((700 - row * block_size) / 2 + block_size); j < (700 - ((700 - row * block_size) / 2)); j += (2
				* block_size)) {
			for (i = ((1000 - column * block_size) / 2 + block_size); i < (1000
					- ((1000 - column * block_size) / 2)); i += (2 * block_size)) {
				g.fillRect(i, j, block_size, block_size);
			}
		}

		g.setColor(new Color(139, 69, 19));
		g.drawLine(((1000 - column * block_size) / 2), ((700 - row * block_size) / 2),
				(1000 - ((1000 - column * block_size) / 2)), ((700 - row * block_size) / 2));
		g.drawLine(((1000 - column * block_size) / 2), ((700 - row * block_size) / 2),
				((1000 - column * block_size) / 2), (700 - ((700 - row * block_size) / 2)));
		g.drawLine(((1000 - column * block_size) / 2), (700 - ((700 - row * block_size) / 2)),
				(1000 - ((1000 - column * block_size) / 2)), (700 - ((700 - row * block_size) / 2)));
		g.drawLine((1000 - ((1000 - column * block_size) / 2)), ((700 - row * block_size) / 2),
				(1000 - ((1000 - column * block_size) / 2)), (700 - ((700 - row * block_size) / 2)));

		g.setColor(color);

		ListIterator<Snake> snakeIterator = snakes.listIterator();
		while (snakeIterator.hasNext()) {
			Snake snake = snakeIterator.next();
			if(signal.getSnakeAlive().get(snakes.indexOf(snake))) {
				snake.paint(this, g);
			}	
		}

		ListIterator<Egg> eggIterator = eggs.listIterator();
		while (eggIterator.hasNext()) {
			Egg egg = eggIterator.next();
			if (signal.getEggAlive().get(eggs.indexOf(egg))) {
				egg.paint(this, g);
			}		
		}

		// Snake snake = new Snake(5);
		// snake.paint(this, g);
		// egg.paint(this, g);
	}

	@Override
	public void update(Graphics arg0) {
		// TODO Auto-generated method stub
		if (offScreenImage == null) {
			offScreenImage = this.createImage(1000, 700);
		}
		Graphics offScreenGraphics = offScreenImage.getGraphics();
		paint(offScreenGraphics);
		arg0.drawImage(offScreenImage, 0, 0, this);
	}

	private class TimerLinster implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			signal.setSnakePEggFlag(signal.getEggNum() + signal.getSnakeNum());

			while (signal.getSnakePEggFlag() != 0) {
				
				try {// 判断执行频率过高，编译器可能将while中判断的结果默认为一个定值，而不判断，
					Thread.sleep(10);// 所以睡10ms降低频率。（此问题仅个人观点，待解）
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			ListIterator<Snake> snakeListIterator = snakes.listIterator();
			
			while (snakeListIterator.hasNext()) {
				Snake snake = (Snake) snakeListIterator.next();
				if(!snake.beOverlap(getGarden())) {
					signal.setSnakeNum(signal.getSnakeNum() - 1);
					signal.getSnakeAlive().set(snakes.indexOf(snake), false);
				}
			}
			
			repaint();
		}
	}

	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if ((arg0.getKeyCode() == KeyEvent.VK_UP || arg0.getKeyCode() == KeyEvent.VK_DOWN
					|| arg0.getKeyCode() == KeyEvent.VK_LEFT || arg0.getKeyCode() == KeyEvent.VK_RIGHT)
					&& timer.isRunning() == true) {
				 snakes.getFirst().control(arg0);
			} else if ((arg0.getKeyCode() == KeyEvent.VK_W || arg0.getKeyCode() == KeyEvent.VK_S
					|| arg0.getKeyCode() == KeyEvent.VK_A || arg0.getKeyCode() == KeyEvent.VK_D)
					&& timer.isRunning() == true) {
				 snakes.getLast().control(arg0);
			} else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (timer.isRunning() == true) {
					timer.stop();
				} else {
					timer.start();
				}
			}
		}
	}
}