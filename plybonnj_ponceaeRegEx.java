import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.*;
import java.util.stream.Stream;

public class plybonnj_ponceaeRegEx {

	public static void main(String[] args) throws FileNotFoundException {
		String fn;
		// check for more than 1 arg or no arg
		if (args.length > 1 || args.length == 0) {
			throw new IllegalArgumentException("Illegal number of args!");
		} else {
			fn = args[0];
		}
		File file = new File(fn);
		// regex pattern for local@domain
		String regex = "([!#$%*+-/=?^_`{|}~a-z0-9\"][!#$%*+-/=?^_`{|}~a-z0-9.]+)?[!#$%*+-/=?^_`{|}~a-z0-9\"]{1,64}@(?:[\\[a-z0-9.]+[a-z0-9-]*[a-z0-9]{1,63}\\.)[a-z0-9\\]]{2,63}";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			HashSet<String> set = new HashSet<>();
			ArrayList<String> list = new ArrayList<>();
			// read file line by line
			while ((line = br.readLine()) != null) {
				//do nothing if line contains .. and doesn't contain a quote
				if (line.contains("..") && !line.contains("\"")) {
				
				} else {
					Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(line);
					// pattern matched, add email to data structures
					if (m.find()) {
						set.add(m.group(0));
						list.add(m.group(0));
					}
				}		
			}
			
			// map for keeping track of counts
			HashMap<String, Integer> map = new HashMap<>();
			Collections.sort(list);
			for (String s : list) {
				if (map.containsKey(s)) {
					map.put(s, map.get(s) + 1);
				} else {
					map.put(s, 1);
				}
			}
			// sort map in reverse order
			Stream<Map.Entry<String, Integer>> sorted = map.entrySet().stream()
					.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

			// output
			System.out.println("Our regex is " + regex);
			System.out.println("Our regex works like this...");
			description();
			System.out.println("The total number of emails is " + list.size());
			System.out.println("The total number of unique emails is " + set.size());
			sorted.forEach(System.out::println);

		} catch (IOException e) {
			System.out.println("An error occured.");
			e.printStackTrace();
		}

	}
	public static String description() {
		System.out.println("It checks that the local part doesn't exceed 64 characters, domain 255 and total length 254.");
		System.out.println("The local part checks for quotes at beginning or end and any number number of dots in between. We used an if statement to check if the line contains double dots and no quotes so that John..Doe@jmu.edu would be invalid. We also allow characters a-z0-9!#$%*+-/=?^_`{|}~ and . but with restrictions");
		System.out.println("We require that an @ symbol be in the middle to aquire the full email");
		System.out.println("The domain part requires a hyphen to be in the middle, this can be any number denoted by the [a-z0-9-]* check which requires characters before and after the hyphen but any number of hyphens can be in between, and the characters a-z0-9- and brackets are allowed but no special characters are permitted in the domain part");
		System.out.println("We would like to give credit to the website https://regexr.com/ which was a tool we used for this project to create our regex");
		return "";
	}

}
