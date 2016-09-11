package cjy.njit.nj.snake;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JFrame {

	public Game(String title) {
		// TODO Auto-generated constructor stub
		super(title);

		setLocation(100, 100);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);            //设置窗口关闭响应为程序结束
		setLayout(new BorderLayout());

		// Garden garden = new Garden();
		// add(garden);

		Panel gPanel = new Panel();
		// gPanel.setSize(1000, 900);
		Garden garden = new Garden();
		gPanel.add(garden);
		add(gPanel, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		Game myGame = new Game("Snake");
	}
}
