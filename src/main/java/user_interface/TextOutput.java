package user_interface;

public class TextOutput {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			final char[] chars = Character.toChars(0x1D7CE + i);
			final String s = new String(chars);
			System.out.println(s);
		}
	}
}
