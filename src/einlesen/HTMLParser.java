package einlesen;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {

	public static Map<String, String[]> loadHTML(String path) throws IOException {
		StringBuffer sb = new StringBuffer(5000);

		Pattern names = Pattern.compile("basic_1..([a-zA-Z]*)&nbsp;([a-zA-Z]*)");
		Matcher name_matcher;

		Pattern geb = Pattern.compile("basic_2..(\\d\\d).(\\d\\d).(\\d*)");
		Matcher geb_matcher;

		Pattern martikel = Pattern.compile("basic_5..(\\d\\d\\d\\d\\d*)");
		Matcher martikel_matcher;

		Pattern p_text = Pattern.compile("alignleft.*valign.*>\\s*([a-zA-Z].*|&.*[^<])");
		Matcher p_text_matcher;

		Pattern p_dncv = Pattern.compile("aligncenter.*valign.*>\\s*(\\d.*|&.*[^<])");
		Matcher p_dncv_matcher;

		Pattern p_ss = Pattern.compile("aligncenter.*valign.*>\\s*([a-zA-Z].*|&.*[^<])");
		Matcher p_ss_matcher;

		Pattern p_pn = Pattern.compile("alignleft.*valign.*>\\s*(\\d.*|&.*[^<])");
		Matcher p_pn_matcher;

		ArrayList<String> prufungstext = new ArrayList<String>();
		ArrayList<String> dncvtext = new ArrayList<String>();
		ArrayList<String> sstext = new ArrayList<String>();
		ArrayList<String> pntext = new ArrayList<String>();

		String vorname = "", nachname = "", geburtsdatum = "", martikelnummer = "", pstring_text = "", p_dncv_text = "",
				p_ss_text = "", p_pn_text = "";

		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream("FH Aachen.html");

			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				sb.append(line);
				sb.append('\n');
			}
			// System.out.println(sb);
			name_matcher = names.matcher(sb);
			geb_matcher = geb.matcher(sb);
			martikel_matcher = martikel.matcher(sb);
			p_text_matcher = p_text.matcher(sb);
			p_dncv_matcher = p_dncv.matcher(sb);
			p_ss_matcher = p_ss.matcher(sb);
			p_pn_matcher = p_pn.matcher(sb);

			while (name_matcher.find()) {
				// System.out.println("group 1: " + name_matcher.group(1));
				vorname = name_matcher.group(1);
				// System.out.println("group 2: " + name_matcher.group(2));
				nachname = name_matcher.group(2);
			}
			while (geb_matcher.find()) {
				// System.out.println("group 1: " + geb_matcher.group(1));
				// System.out.println("group 2: " + geb_matcher.group(2));
				// System.out.println("group 3: " + geb_matcher.group(3));
				geburtsdatum = geb_matcher.group(1) + "." + geb_matcher.group(2) + "." + geb_matcher.group(3);
			}
			while (martikel_matcher.find()) {
				martikelnummer = martikel_matcher.group(1);
			}
			while (p_text_matcher.find()) {
				pstring_text = pstring_text + "_" + p_text_matcher.group(1);
			}
			while (p_dncv_matcher.find()) {
				p_dncv_text = p_dncv_text + "_" + p_dncv_matcher.group(1);
			}
			while (p_ss_matcher.find()) {
				p_ss_text = p_ss_text + "x" + p_ss_matcher.group(1);
			}
			while (p_pn_matcher.find()) {
				p_pn_text = p_pn_text + "," + p_pn_matcher.group(1);
			}

			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
		}

		// Parsen into Array
		// System.out.println(p_dncv_text);

		String[] ssstrings = p_ss_text.replaceFirst("^x", "").split("x");// semester
																			// +
																			// bestanden
		String[] pnstrings = p_pn_text.replaceFirst("^,", "").split(",");// prufungnummer
																			// X
		String[] pdncvstrings = p_dncv_text.replaceFirst("^_", "").split("_");// datum
																				// +
																				// note
																				// +
																				// credits
																				// +
																				// versuch
																				// X
		String[] pstrings = pstring_text.replaceFirst("^_", "").split("_");// prufungstext
																			// X

		// System.out.println(pstrings.length);

		int rows = pnstrings.length;
		// System.out.println(pnstrings.length);
		// create row -> this will be the standard length for the colums in the
		// row
		String strings[][] = new String[rows][8];

		// start from 1 because index 0 is empty
		for (int i = 0; i < pnstrings.length; i++) {
			strings[i][0] = pnstrings[i];
		}
		for (int i = 0; i < pstrings.length; i++) {
			strings[i][1] = pstrings[i];
		}

		int saver = 2;
		for (int i = 0; i < (pdncvstrings.length / 4); i++) {
			for (int j = i * 4; j < (i * 4) + 4; j++) {
				strings[i][saver] = pdncvstrings[j];
				saver++;
			}
			saver = 2;
		}
		// System.out.println(pdncvstrings.length-1);
		// System.out.print(pdncvstrings[pdncvstrings.length-1] + " ");

		saver = 6;
		for (int i = 0; i < ssstrings.length / 2; i++) {
			for (int j = i * 2; j < (i * 2) + 2; j++) {
				// System.out.print(ssstrings[j]);
				strings[i][saver] = ssstrings[j];
				saver++;
			}
			saver = 6;
			// System.out.println();
		}

		// AUSGABEN
		// System.out.println(
		// vorname + " " + nachname + " " + geburtsdatum + " " +
		// martikelnummer);

		// for (int i = 0; i < rows; i++) {
		// for (int j = 0; j < 8; j++) {
		// System.out.print(strings[i][j] + "\t");
		//
		// }
		// System.out.println();
		// }

		// von lukas
		Map<String, String[]> output = new HashMap<>();
		for (int x = 0; x < strings.length; x++) {
			String[] string = new String[7];
			for (int y = 1; y < strings[x].length; y++) {
				string[y - 1] = strings[x][y];
			}
			output.put(strings[x][0], string);
		}

		return output;
	}

}