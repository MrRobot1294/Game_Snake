package cjy.njit.nj.snake;

import java.util.Vector;

public class Signal {
	private int snakeNum;
	private int eggNum;
	private int snakePEggFlag;
	private Vector<Boolean> snakeAlive;
	private Vector<Boolean> eggAlive;
	
	public Signal(int snakeNum, int eggNum) {
		super();
		this.snakeNum = snakeNum;
		this.eggNum = eggNum;
		this.snakePEggFlag = 0;
		
		snakeAlive = new Vector<>();
		for (int i = 0; i < snakeNum; i++) {
			snakeAlive.add(new Boolean(true));
		}
		
		eggAlive = new Vector<>();
		for (int i = 0; i < eggNum; i++) {
			eggAlive.add(new Boolean(true));
		}
	}
	
	public int getSnakeNum() {
		return snakeNum;
	}
	public void setSnakeNum(int snakeNum) {
		this.snakeNum = snakeNum;
	}
	public int getEggNum() {
		return eggNum;
	}
	public void setEggNum(int eggNum) {
		this.eggNum = eggNum;
	}
	public int getSnakePEggFlag() {
		return snakePEggFlag;
	}
	public void setSnakePEggFlag(int snakePEggFlag) {
		this.snakePEggFlag = snakePEggFlag;
	}

	public Vector<Boolean> getSnakeAlive() {
		return snakeAlive;
	}

	public void setSnakeAlive(Vector<Boolean> snakeAlive) {
		this.snakeAlive = snakeAlive;
	}

	public Vector<Boolean> getEggAlive() {
		return eggAlive;
	}

	public void setEggAlive(Vector<Boolean> eggAlive) {
		this.eggAlive = eggAlive;
	}
}
