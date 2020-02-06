package basic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BoardImporter {
	private String filePath;

	public BoardImporter() {
		filePath = "./src/main/resources/Boards/Sudoku_?.txt";
	}

	public GameBoard importBoard(int boardNumber) {
		filePath = filePath.replace("?", Integer.toString(boardNumber));
		GameBoard gameBoard = new GameBoard();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = "";
			int y = 0;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");

				for (int x = 0; x < 9; x++) {
					int currentNumber = Integer.parseInt(parts[x]);
					boolean editable = (currentNumber != 0) ? false : true;

					Field f = new Field(new Position(x, y), currentNumber, editable);

					gameBoard.saveField(f);
				}
				y++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Das Programm kann den Pfad " + filePath + " nicht finden.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return gameBoard;
	}
}
