package user_interface;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;

import javax.swing.*;

import basic.*;
import exceptions.UnsolvableException;
import logic.BoardSolver;
import logic.ErrorFinder;

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
	private BoardImporter importer;
	
	private Position activeFieldPos;

	public GUI() {
		super("Sudoku Solver");

		d = new Draw();
		importer = new BoardImporter();
		
		gameBoard = importer.importBoard(0);
		boardSolver = new BoardSolver(gameBoard, BoardSolver.STANDARD_PROCEDURE);
		gameBoard = boardSolver.getGameBoard();
		
		
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
		//System.setErr(new CustomOutputStream(d, Color.RED, oldErr));
		
		
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		newGame.setToolTipText("This will start a new game with a new board.");
		reset = new JButton("Reset");
		reset.addActionListener(this);
		reset.setToolTipText("This button will clear the current board.");
		solve = new JButton("Solve");
		solve.addActionListener(this);
		solve.setToolTipText("Press this button to solve the board.");
		check = new JButton("Check");
		check.addActionListener(this);
		check.setToolTipText("Press this button to check for errors!");
		
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(newGame);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(reset);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(solve);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(check);

		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));

		mainPanel.add(drawPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.PAGE_START);
		mainPanel.add(controlPanel, BorderLayout.LINE_START);

		add(mainPanel);
		setFocusable(true);
		addKeyListener(this);
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setAlwaysOnTop(true);
		setVisible(true);
		setResizable(false);
		setBackground(Color.WHITE);
		d.repaint();
		
		//System.out.println("This is a test");
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == this.drawPanel) {
			Position clickedFieldPos = d.calculateClickedField(e.getX(), e.getY());
			activeFieldPos = (clickedFieldPos != null)? clickedFieldPos : activeFieldPos;
			
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
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		boolean working = false;
		if (!working) {
			working = true;
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				setNumber(0);
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				boardSolver = new BoardSolver(gameBoard, BoardSolver.STANDARD_PROCEDURE);
				try {
					boardSolver.solveWithRecursion();
				} catch (UnsolvableException exc) {
					exc.printStackTrace();
				}
				gameBoard = boardSolver.getGameBoard();
			} else if (e.getKeyCode() == KeyEvent.VK_R) {
				gameBoard.clearAllEditableFields();
			} else if (Character.getType(e.getKeyChar()) == Character.DECIMAL_DIGIT_NUMBER) {
				setNumber(Character.getNumericValue(e.getKeyChar()));
			}
			d.repaint();
			working = false;
		} else {
			System.err.printf("Stop");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.solve) {
			boardSolver = new BoardSolver(gameBoard, BoardSolver.STANDARD_PROCEDURE);
			try {
				boardSolver.solveWithRecursion();
			} catch (UnsolvableException exc) {
				exc.printStackTrace();
			}
			gameBoard = boardSolver.getGameBoard();
		} else if (e.getSource() == this.newGame) {
			//TODO add board generation
		} else if (e.getSource() == this.reset) {
			gameBoard.clearAllEditableFields();
		} else if (e.getSource() == this.check) {
			ErrorFinder ef = new ErrorFinder(gameBoard);
			gameBoard = ef.getGameBoard();
			System.err.println("Es wurden " + ef.getAmountOfErrors() + " Fehler gefunden!");
		}
		d.repaint();
	}
	

	private void setNumber(int number) {
		if (activeFieldPos.getX() != -1 && activeFieldPos.getY() != -1) {
			Field activeField = gameBoard.getField(activeFieldPos.getX(), activeFieldPos.getY());
			activeField.setNumber(number);
			gameBoard.saveField(activeField);
			gameBoard.resetPossibleNumbers();
			boardSolver = new BoardSolver(gameBoard, BoardSolver.STANDARD_PROCEDURE);
			gameBoard = boardSolver.getGameBoard();
			errorFinder = new ErrorFinder(gameBoard);
			gameBoard = errorFinder.getGameBoard();
		}
		
	}


	private class Draw extends Component {
		private static final long serialVersionUID = 1L;

		private Dimension preferredDimension;
		private int xPosActive, yPosActive;
		private Graphics2D hiddenG2D;
		
		private String outputString;
		private Color outputColor;

		public Draw() {
			preferredDimension = new Dimension(546, 546);
			xPosActive = -1;
			yPosActive = -1;
			
			outputString = "";
			
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
			paintOutput(g2d);
		}

		private void paintNumbers(Graphics2D g2d) {
			LinkedList<Field> allFields = gameBoard.getArea(0, 0, 8, 8);
			
			g2d.setColor(Color.BLACK);
			
			Font customFont = g2d.getFont();
			
			// import custom font
			try {
				customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./src/main/resources/Fonts/VanillaExtractRegular.ttf")).deriveFont(40f);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			g2d.setFont(customFont);

			for (Field f : allFields) {
				// paint number of active field red, if the field is marked as incorrect
				if (!f.isCorrect() && f.getPos().equals(activeFieldPos)) {
					g2d.setColor(new Color(166, 11, 0));
				} else {
					g2d.setColor(Color.BLACK);
				}
				
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
					LinkedList<Integer> allPossibleNumbers = f.getPossibleNumbers().asList();
					
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
					if (field.getPossibleNumbers().getAmount() == 1 && field.getNumber() == 0) {
						fieldColor = Color.GREEN;
					} else if (!field.isCorrect()) {
						fieldColor = Color.RED;
					}
				} else {
					fieldColor = new Color(100, 100, 100);
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
		
		public void paintOutput(Graphics2D g2d) {
			g2d.setColor(outputColor);
			g2d.drawString(outputString, 60, 80);
		}

		public void setTextAndColor(String s, Color c) {
			outputString = s;
			outputColor = c;
		}


		@Override
		public Dimension getPreferredSize() {
			return preferredDimension;
		}
		
		public Position calculateClickedField(int mousePointerX, int mousePointerY) {
		    int xPos = (mousePointerX > 1 && mousePointerX < 543)? (int) (mousePointerX - 3) / 60 : -1;
			int yPos = (mousePointerY > 1 && mousePointerY < 543)? (int) (mousePointerY - 3) / 60 : -1;
			if (!gameBoard.getField(xPos, yPos).isEditable()) {
				return null;
			}
			xPosActive = xPos;
			yPosActive = yPos;
			return new Position(xPosActive, yPosActive);
		}

	}

	public class CustomOutputStream extends PrintStream {

		private Color c = Color.BLACK;
		
		public CustomOutputStream(Draw d, Color c, OutputStream out) {
			super(out);
			this.c = c;
		}


		@Override
		public void println(String s) {
			d.setTextAndColor(s, c);
		}

		@Override
		public void print(String s) {
			d.setTextAndColor(s, c);
		}
	}
}
