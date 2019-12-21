package user_interface;

public class TextOutput {
	public static void main(String[] args) {
		System.out.println("9 + \u1D7D7");
		
		final char[] chars = Character.toChars(0x1D7D7);
		final String s = new String(chars);
		System.out.println(s);
	}
}
