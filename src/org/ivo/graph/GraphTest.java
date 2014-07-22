package org.ivo.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.ivo.Benchmarker;

public class GraphTest implements Runnable {
	
	private ArrayList<String> strings;
	private BasicWordGraph basicGraph;

	public GraphTest() {
		strings = new ArrayList<String>();
		try {
			File file = new File("data/BTB-FreqList100000.txt");
			FileInputStream is = new FileInputStream(file);
			is.read();
			is.read();
			Scanner scanner = new Scanner(is, "UTF-16");
			scanner.useDelimiter(Pattern.compile("\n|\r|\t"));
			while(scanner.hasNext()) {
				strings.add(scanner.next());
			}
			scanner.close();
			basicGraph = new BasicWordGraph();
			basicGraph.build(strings);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean result = true;
		for(String str : strings) {
			Node current = basicGraph.getStart();
			for(int i=0; i<str.length(); i++)
				current = current.next(str.charAt(i));
			result = result && current.isFinal() == 1;
		}
	}

	public static void exec() {
		Benchmarker.benchmark(new GraphTest());
	}

}
