package org.ivo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Files;

public class Tests {
	public static final String[] tests = {
		"", "",
		"a", "",
		"?", "",
		"*", "",
		"*?", "",
		"**", "",
		"", "a",
		"a", "a",
		"b", "a",
		"?", "a",
		"*", "a",
		"*?", "a",
		"**", "a",
		"ab", "aab",
		"a?b", "aab",
		"a*b", "aab",
		"a*?b", "aab",
		"a*a*b", "aab",
		"aa", "aaa",
		"a?a", "aaa",
		"a*a", "aaa",
		"a*?a", "aaa",
		"a*a*a", "aaa",
		"a*b*c", "abc",
		"a*b*c", "axbxc",
		"*a", "aaa",
		"*a", "aaab",
		"*a", "bbba",
		"*b", "aaa",
		"*?", "aaa",
		"a*", "aaa",
		"a*", "baaa",
		"b*", "aaa",
		"?*", "aaa",
		"a*?", "abba",
		"a**", "aa"
	};
	
	static String file;

	static {
		try {
			file = Files.toString(
					new File("data/text2.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			file = "";
			e.printStackTrace();
		}		
	}
	
	protected static String[] benchmarksFail = {
		"*b?????????a*", "adbfmDLELblSAzQ6kXU3"
		};
	
	static final String[] benchmarks = {
		"o*e*n", "onetwothreefourfivesixseveneightnineteneleventwelvethirteen",
		"?*a", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
		"*a*a", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
		"*a*a*a", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
		"*a*a*a*a", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
		"*b?????????", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaa",
		"*b?????????a*", file,
		"*a*a*a*", file,
		"*permitted*employe?s*?indows*www.nexus.hu/upx*information*-------", file,
		"*permitted*employe?s*?indows*www.nexus.hu/upx*information*-------*\n", file
	};
}
