package backend;

import java.io.*;

public class IO
{
	private static BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

	public static String readString()
	{
		while (true) {
			try {
				return kb.readLine();
			} catch (IOException e) {
				// should never happen
			}
		}
	}

	public static int readInt()
	{
		while (true) {
			try {
				String s = kb.readLine();
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				System.out.print("That is not an integer.  Enter again: ");
			} catch (IOException e) {
				// should never happen
			}
		}
	}

	public static boolean readBoolean()
	{
		String s = null;

		while (true) {
			try {
				s = kb.readLine();
			} catch (IOException e) {
				// should never happen
			}

			if (s.equalsIgnoreCase("yes") ||
					s.equalsIgnoreCase("y") ||
					s.equalsIgnoreCase("true") ||
					s.equalsIgnoreCase("t")) {
				return true;
			} else if (s.equalsIgnoreCase("no") ||
					s.equalsIgnoreCase("n") ||
					s.equalsIgnoreCase("false") ||
					s.equalsIgnoreCase("f")) {
				return false;
			} else {
				System.out.print("Enter \"yes\" or \"no\": ");
			}
		}
	}
}
