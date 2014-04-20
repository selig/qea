package benchmark.rovers.mop;

import java.io.File;
import java.io.IOException;

public class MopCompiler {

	private static String javamop = "/Users/giles/lib/javamop3.0/bin/javamop";
	private static final String dir="src/benchmark/rovers/mop";
	
	public static void main(String[] args) throws IOException{
		compile("GrantCancel");
	}

	public static void compile(String property) throws IOException{
		String[] params = new String[2];
		params[0] = javamop;
		params[1] = dir+"/"+property+"MOP.mop";
		//check if it exists
		if((new File(params[1])).exists()){
			Runtime.getRuntime().exec(params);
		}
		else System.err.println(property+" not found");
	}
	
}
