package cjy.njit.nj.snake;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.ImageIcon;

public class Snake {
	private String name;

	private Node head;
	private Node tail;
	private int length;
	private LinkedList<Node> snake;

	private boolean growflag;

	private Thread snakeThread;

	public Snake(String name, int length, Garden garden) {
		this.name = name;

		this.length = length;

		this.growflag = false;

		snake = new LinkedList<Node>();

		tail = new Node(SnakeBodyAttribute.TAIL, Direction.RIGHT, 0, 0);
		snake.addFirst(tail);

		Node node = new Node(SnakeBodyAttribute.BODY, tail.direction, tail.x + 1, tail.y);
		snake.addFirst(node);
		for (int i = 0; i < length - 1; i++) {
			node = new Node(SnakeBodyAttribute.BODY, node.direction, node.x + 1, node.y);
			snake.addFirst(node);
		}

		head = new Node(SnakeBodyAttribute.HEAD, Direction.RIGHT, node.x + 1, node.y);
		snake.addFirst(head);

		snakeThread = new Thread(new SnakeRunable(garden));
		snakeThread.start();
	}

	public Snake(String name, Garden garden) {
		this.name = name;

		this.length = 1;

		this.growflag = false;

		snake = new LinkedList<Node>();

		tail = new Node(SnakeBodyAttribute.TAIL, Direction.RIGHT, 0, 0);
		snake.addFirst(tail);

		Node node = new Node(SnakeBodyAttribute.BODY, tail.direction, 1, 0);
		snake.addFirst(node);

		head = new Node(SnakeBodyAttribute.HEAD, Direction.RIGHT, 2, 0);
		snake.addFirst(head);

		snakeThread = new Thread(new SnakeRunable(garden));
		snakeThread.start();
	}

	public Snake getSnake() {
		return this;
	}

	public LinkedList<Node> getsnake() {
		return snake;
	}

	public boolean isGrowflag() {
		return growflag;
	}

	public void setGrowflag(boolean growflag) {
		this.growflag = growflag;
	}

	public boolean eat(Egg egg) {
		if (head.x == egg.getX() && head.y == egg.getY()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean beOverlap(Garden garden) {
		ListIterator<Snake> snakeListIterator = garden.getSnakes().listIterator();
		while (snakeListIterator.hasNext()) {
			Snake snake = (Snake) snakeListIterator.next();

			int i;
			if (snake != this) {
				i = 0;
			} else {
				i = 1;
			}

			for (; i < snake.getsnake().size(); i++) {
				if (snake.getsnake().get(i).getX() == head.x && snake.getsnake().get(i).getY() == head.y) {
					return false;
				}
			}
		}

		return true;
	}

	public void move() {
		snake.removeLast();
		tail = snake.getLast();
		tail.bodyAttribute = SnakeBodyAttribute.TAIL;

		snake.getFirst().bodyAttribute = SnakeBodyAttribute.BODY;

		switch (head.direction) {
		case UP:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x, head.y - 1);
			break;

		case DOWN:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x, head.y + 1);
			break;

		case LEFT:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x - 1, head.y);
			break;

		case RIGHT:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x + 1, head.y);
			break;

		default:
			break;
		}

		snake.addFirst(head);
	}

	public void grow() {
		snake.getFirst().bodyAttribute = SnakeBodyAttribute.BODY;

		switch (head.direction) {
		case UP:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x, head.y - 1);
			break;

		case DOWN:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x, head.y + 1);
			break;

		case LEFT:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x - 1, head.y);
			break;

		case RIGHT:
			head = new Node(SnakeBodyAttribute.HEAD, head.direction, head.x + 1, head.y);
			break;

		default:
			break;
		}

		snake.addFirst(head);
	}

	public void control(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (head.direction != Direction.DOWN) {
				head.direction = Direction.UP;
			}
			break;

		case KeyEvent.VK_DOWN:
			if (head.direction != Direction.UP) {
				head.direction = Direction.DOWN;
			}
			break;

		case KeyEvent.VK_LEFT:
			if (head.direction != Direction.RIGHT) {
				head.direction = Direction.LEFT;
			}
			break;

		case KeyEvent.VK_RIGHT:
			if (head.direction != Direction.LEFT) {
				head.direction = Direction.RIGHT;
			}
			break;

		case KeyEvent.VK_W:
			if (head.direction != Direction.DOWN) {
				head.direction = Direction.UP;
			}
			break;

		case KeyEvent.VK_S:
			if (head.direction != Direction.UP) {
				head.direction = Direction.DOWN;
			}
			break;

		case KeyEvent.VK_A:
			if (head.direction != Direction.RIGHT) {
				head.direction = Direction.LEFT;
			}
			break;

		case KeyEvent.VK_D:
			if (head.direction != Direction.LEFT) {
				head.direction = Direction.RIGHT;
			}
			break;

		default:
			break;
		}
	}

	public void paint(Garden garden, Graphics g) {
		ListIterator<Node> snakeListIterator = snake.listIterator(snake.size());

		Node p = null;
		Node q = null;

		while (snakeListIterator.hasPrevious()) {
			p = snakeListIterator.previous();
			p.paint(q, garden, g);

			q = p;
		}
	}

	class Node {
		private SnakeBodyAttribute bodyAttribute;
		private Direction direction;
		private int x;
		private int y;
		// private Node next;

		public Node(SnakeBodyAttribute bodyAttribute, Direction direction, int x, int y) {
			super();
			this.bodyAttribute = bodyAttribute;
			this.direction = direction;
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public SnakeBodyAttribute getBodyAttribute() {
			return bodyAttribute;
		}

		public Direction getDirection() {
			return direction;
		}

		public void paint(Node front, Garden garden, Graphics g) {
			Image image = null;

			switch (bodyAttribute) {
			case HEAD: {

				switch (direction) {
				case UP: {
					image = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeHead_Up.gif")
							.getImage();
				}
					break;
				case DOWN: {
					image = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeHead_Down.gif")
							.getImage();
				}
					break;
				case LEFT: {
					image = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeHead_Left.gif")
							.getImage();
				}
					break;
				case RIGHT: {
					image = new ImageIcon(
							System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeHead_Right.gif").getImage();
				}
					break;

				default:
					break;
				}

				g.drawImage(image,
						x * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
						y * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2,
						(x + 1) * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
						(y + 1) * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2, 0, 0,
						500, 500, garden);
			}
				break;

			case BODY: {
				if (front.direction == this.direction) {
					if (this.direction == Direction.LEFT || this.direction == Direction.RIGHT) {
						image = new ImageIcon(
								System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeBody_Left.gif")
										.getImage();
					} else {
						image = new ImageIcon(
								System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeBody_Up.gif").getImage();
					}
				} else {
					if ((this.direction == Direction.RIGHT && front.direction == Direction.DOWN)
							|| (front.direction == Direction.LEFT && this.direction == Direction.UP)) {
						image = new ImageIcon(
								System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeBody_TurnRight.gif")
										.getImage();
					}
					if ((this.direction == Direction.DOWN && front.direction == Direction.RIGHT)
							|| (front.direction == Direction.UP && this.direction == Direction.LEFT)) {
						image = new ImageIcon(
								System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeBody_TurnLeft.gif")
										.getImage();
					}
					if ((this.direction == Direction.UP && front.direction == Direction.RIGHT)
							|| (front.direction == Direction.DOWN && this.direction == Direction.LEFT)) {
						image = new ImageIcon(
								System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeBody_TurnUp.gif")
										.getImage();
					}
					if ((this.direction == Direction.RIGHT && front.direction == Direction.UP)
							|| (front.direction == Direction.LEFT && this.direction == Direction.DOWN)) {
						image = new ImageIcon(
								System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeBody_TurnDown.gif")
										.getImage();
					}
				}

				g.drawImage(image,
						x * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
						y * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2,
						(x + 1) * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
						(y + 1) * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2, 0, 0,
						500, 500, garden);
			}
				break;

			case TAIL: {

				switch (direction) {
				case UP: {
					image = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeTail_Up.gif")
							.getImage();
				}
					break;
				case DOWN: {
					image = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeTail_Down.gif")
							.getImage();
				}
					break;
				case LEFT: {
					image = new ImageIcon(System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeTail_Left.gif")
							.getImage();
				}
					break;
				case RIGHT: {
					image = new ImageIcon(
							System.getProperty("user.dir") + "/src/cjy/njit/nj/source/SnakeTail_Right.gif").getImage();
				}
					break;

				default:
					break;
				}

				g.drawImage(image,
						x * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
						y * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2,
						(x + 1) * garden.getBlock_size() + (1000 - garden.getColumn() * garden.getBlock_size()) / 2,
						(y + 1) * garden.getBlock_size() + (700 - garden.getRow() * garden.getBlock_size()) / 2, 0, 0,
						500, 500, garden);
			}
				break;

			default:
				break;
			}
		}
	}

	public enum SnakeBodyAttribute {
		HEAD, BODY, TAIL;
	}

	private class SnakeRunable implements Runnable {
		private Garden garden;

		public SnakeRunable(Garden garden) {
			super();
			this.garden = garden;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (garden.getSignal().getSnakeAlive().get((garden.getSignal().getSnakeAlive().indexOf(getSnake())))) {

				if (garden.getSignal().getSnakePEggFlag() == (garden.getSignal().getEggNum()
						+ garden.getSnakes().indexOf(getSnake()) + 1)) {
					if (growflag) {
						grow();
						setGrowflag(false);
					} else {
						move();
					}
					garden.getSignal().setSnakePEggFlag(garden.getSignal().getSnakePEggFlag() - 1);
				}

				try { // 判断执行频率过高，编译器可能将if的结果默认为一个定值，而不判断，
					Thread.sleep(10); // 所以睡10ms降低频率。（此问题仅个人观点，待解）
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
