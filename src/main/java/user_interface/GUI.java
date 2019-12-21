package user_interface;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

import javax.swing.*;

import basic.*;

public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, buttonPanel, drawPanel, controlPanel;
	private JButton newGame, reset, solve, check;

	private Draw d;
	private GameBoard gameBoard;
	private BoardSolver boardSolver;
	private ErrorFinder errorFinder;
	
	private Position activeFieldPos;

	public GUI() {
		super("Sudoku Solver");

		d = new Draw();
		gameBoard = new GameBoard();
		Field field = new Field(new Position(0, 0), 9, true);
		gameBoard.saveField(field);
		
		activeFieldPos = new Position(-1, -1);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		drawPanel = new JPanel();
		drawPanel.add(d);
		drawPanel.addMouseListener(this);
		drawPanel.setPreferredSize(d.getPreferredSize());
		
		// reroute output to GUI
		OutputStream oldOut = System.out;
		OutputStream oldErr = System.err;
		
		System.setOut(new CustomOutputStream(d, Color.GREEN, oldOut));
		System.setErr(new CustomOutputStream(d, Color.RED, oldErr));
		

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));

		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));

		mainPanel.add(drawPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_START);
		mainPanel.add(controlPanel, BorderLayout.LINE_START);

		add(mainPanel);
		addKeyListener(this);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setVisible(true);
		d.repaint();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.drawPanel) {
			activeFieldPos = d.calculateClickedField(e.getX(), e.getY());
			d.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (activeFieldPos.getX() != -1 && activeFieldPos.getY() != -1) {
			Field activeField = gameBoard.getField(activeFieldPos.getX(), activeFieldPos.getY());
			
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				activeField.setNumber(0);
			} else if (Character.getType(e.getKeyChar()) == Character.DECIMAL_DIGIT_NUMBER) {
				activeField.setNumber(Character.getNumericValue(e.getKeyChar()));
			}
			
			gameBoard.saveField(activeField);
			gameBoard.reset();
			
			boardSolver = new BoardSolver(gameBoard);
			gameBoard = boardSolver.getGameBoard();
			
			errorFinder = new ErrorFinder(gameBoard);
			gameBoard = errorFinder.getGameBoard();
			
			d.repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}


	private class Draw extends Component {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Dimension preferredDimension;
		private int xPosActive, yPosActive;
		private Graphics2D hiddenG2D;

		public Draw() {
			preferredDimension = new Dimension(544, 544);
			xPosActive = -1;
			yPosActive = -1;
			
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			hiddenG2D = g2d;

			g2d.setColor(Color.WHITE);
			g2d.fillRect(0, 0, 544, 544);


			paintFields(g2d);
			paintActiveField(g2d);
			paintLines(g2d);
			paintNumbers(g2d);
			paintPossibleNumbers(g2d);
		}

		private void paintNumbers(Graphics2D g2d) {
			LinkedList<Field> allFields = gameBoard.getArea(0, 0, 8, 8);
			
			g2d.setColor(Color.BLACK);
			
			Font customFont = g2d.getFont();
			
			// import custom font
			try {
				customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./src/main/resources/VanillaExtractRegular.ttf")).deriveFont(40f);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g2d.setFont(customFont);

			for (Field f : allFields) {
				if (f.getNumber() != 0) {
					g2d.drawString(String.valueOf(f.getNumber()), f.getPos().getX() * 60 + 18, f.getPos().getY() * 60 + 48);
				}
			}
		}
		
		private void paintPossibleNumbers(Graphics2D g2d) {
			LinkedList<Field> allFields = gameBoard.getArea(0, 0, 8, 8);
			
			g2d.setFont(getFont().deriveFont(15f));
			g2d.setColor(new Color(200, 200, 200));
			
			for (Field f : allFields) {
				if (f.getNumber() == 0) {
					LinkedList<Integer> allPossibleNumbers = (LinkedList<Integer>) f.getRemainingPossibleNumbers();
					
					for (Integer i : allPossibleNumbers) {
						int value = i.intValue();
						
						int xPos = f.getPos().getX() * 60 + ((value - 1) % 3) * 20 + 7;
						int yPos = f.getPos().getY() * 60 + ((value - 1) / 3) * 20 + 17;
						
						g2d.drawString(i.toString(), xPos, yPos);
					}
				}
			}
		}

		private void paintActiveField(Graphics2D g2d) {
			if (xPosActive != -1 && yPosActive != -1) {
				g2d.setColor(Color.CYAN);
				g2d.fillRect(xPosActive * 60, yPosActive * 60, 60, 60);
			}
		}

		private void paintFields(Graphics2D g2d) {
			LinkedList<Field> allFields = gameBoard.getArea(0, 0, 8, 8);

			for (Field field : allFields) {
				Color fieldColor = null;

				if (field.isEditable()) {
					if (field.getRemainingPossibleNumbers().size() == 1 && field.getNumber() == 0) {
						fieldColor = Color.GREEN;
					} else if (!field.isCorrect()) {
						fieldColor = Color.RED;
					}
				} else {
					fieldColor = Color.DARK_GRAY;
				}

				if (fieldColor != null) {
					g2d.setColor(fieldColor);
					g2d.fillRect(field.getPos().getX() * 60, field.getPos().getY() * 60, 60, 60);
				}
			}
		}

		public void paintLines(Graphics2D g2d) {
			g2d.setColor(Color.DARK_GRAY);

			for (int i = 0; i < 10; i++) {
				int width = (i % 3 == 0)? 5 : 3;
				g2d.fillRect(i * 60, 0, width, 544);
				g2d.fillRect(0, i * 60, 544, width);
			}
		}

		public void paintText(String s, Color c) {
			//TODO implement this shit you fucking disgrace
			hiddenG2D.setColor(c);
			hiddenG2D.drawString(s, 30, 30);
		}


		@Override
		public Dimension getPreferredSize() {
			return preferredDimension;
		}
		
		public Position calculateClickedField(int mousePointerX, int mousePointerY) {
		    xPosActive = (mousePointerX > 1 && mousePointerX < 543)? (int) (mousePointerX - 3) / 60 : -1;
			yPosActive = (mousePointerY > 1 && mousePointerY < 543)? (int) (mousePointerY - 3) / 60 : -1;
			
			return new Position(xPosActive, yPosActive);
		}

	}

	public class CustomOutputStream extends PrintStream {

		private Color c;
		
		public CustomOutputStream(Draw d, Color c, OutputStream out) {
			super(out);
			this.c = c;
		}


		@Override
		public void println(String s) {
			d.paintText(s, c);
		}

		@Override
		public void print(String s) {
			d.paintText(s, c);
		}
	}
}
