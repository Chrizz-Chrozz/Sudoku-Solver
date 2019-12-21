package basic_tester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import basic.Field;
import basic.Position;

class FieldTester {
	private static Field field;
	
	@BeforeEach
	void init() {
		field = new Field(new Position(0, 0), 4, false);
	}
	
	@Test
	void settingTheNumberWhenFieldIsNotEditableDoesNotWork() {
		field.setNumber(6);
		
		assertNotEquals(6, field.getNumber());
	}
	
	@Test
	void settingTheNumberWhenFieldIsEditableDoesWork() {
		field.setEditable(true);
		
		field.setNumber(6);
		
		assertEquals(6, field.getNumber());
	}
	
	
	private void setNumber(int number) {
		field.setEditable(true);
		field.setNumber(number);
		field.setEditable(false);
	}
	
	@Test
	void possibleNumbersAreUpdatedWhenSettingNumber() {
		setNumber(8);

		for (int i = 1; i <= 9; i++) {
			if (i == 8) {
				assertTrue(field.getPossibleNumber(i));
			} else {
				assertFalse(field.getPossibleNumber(i));
			}
		}
	}
	
	@Test
	void allNumbersArePossibleForEmptyField() {
		setNumber(0);
		
		for (int i = 1; i <= 9; i++) {
			assertTrue(field.getPossibleNumber(i));
		}
	}
	
	@Test
	void cantSetEmptyFieldToNotEditable() {
		setNumber(0);
		
		assertTrue(field.isEditable());
	}
	
	@Test
	void checkListOfAllRemainingPossibleNumbers() {
		setNumber(0);
		
		boolean[] possibleNumbers = new boolean[] {true, false, false, true, true, false, false, true, false};
		
		for (int i = 0; i < possibleNumbers.length; i++) {
			field.setPossibleNumber(i + 1, possibleNumbers[i]);
		}
		
		List<Integer> remainingPossibleNumbers = field.getRemainingPossibleNumbers();
		
		assertEquals(4, remainingPossibleNumbers.size());
	}
}