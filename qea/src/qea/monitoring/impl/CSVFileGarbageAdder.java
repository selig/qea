package qea.monitoring.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;


public class CSVFileGarbageAdder {

	public static void reverseFile(String in, String out) throws IOException, InterruptedException{
		File rev_sh_file = new File("./rev.sh");
		PrintWriter rev_sh = new PrintWriter(rev_sh_file);
		rev_sh.println("tail -r $1");
		rev_sh.close();
		ProcessBuilder builder = new ProcessBuilder("sh","./rev.sh",in);
		builder.redirectOutput(new File(out));
		builder.redirectError(new File(out));
		final Process p = builder.start();		
		p.waitFor();
		rev_sh_file.delete();
	}
	
	public static void rewrite(String tracename, String new_tracename) throws IOException, InterruptedException {

		if(!new File(tracename).exists()) throw new RuntimeException(tracename+" does not exist");

		// reverse trace into trace_tmp
		String tmp_tracename = tracename+"_tmp";
		reverseFile(tracename,tmp_tracename);
		
		String tmp_new_tracename = new_tracename+"_tmp";
		File new_trace = new File(tmp_new_tracename);
		if(new_trace.exists()) throw new RuntimeException(tmp_new_tracename+" already exists");
		
		BufferedReader reader = new BufferedReader(new FileReader(tmp_tracename));
		PrintWriter writer = new PrintWriter(new_trace);
		
		String line;
		Set<String> seen = new HashSet<String>();
		while ((line = reader.readLine()) != null) {

			String[] parts = line.split(",");
			for(int i=1;i<parts.length;i++){
				parts[i].intern();
				if(seen.add(parts[i])){
					writer.println("garbage,"+parts[i]);
				}
			}
			writer.println(line);
		}
		
		reader.close();
		writer.close();
		reverseFile(tmp_new_tracename,new_tracename);		
		(new File(tmp_tracename)).delete();
		new_trace.delete();
	}

	public static void main(String[] args) throws IOException, InterruptedException{
		// Testing
		CSVFileGarbageAdder.rewrite(
				"/Users/giles/git/crv15/Java/sql/sql_trace_test",
				"/Users/giles/git/crv15/Java/sql/sql_trace_test_garbage");
		
		
	}
	
}
