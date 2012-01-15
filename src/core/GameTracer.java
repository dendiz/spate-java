package core;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
/**
 * holds a game trace in memory and dumps the memory to a log file every N lines.
 * Need to call the finish() methods at the simulation end, because the buffer is not cleared by setting the
 * contents to null, but the by setting the current slot pointer to zero, which makes the tracer over write
 * existing entries on the next iteration. This is done to optimize by skipping the clearing of the buffer or
 * allocating a new memory slot (which achieves the same result as clearing the buffer).
 * 
 * the finish() method will set the remainder of the dirty buffer to null and output the final pieces of the trace log.
 * 
 * @author dendiz
 *
 */
public class GameTracer {

	String[] trace_buffer;
	int current_idx = 0;
	int total_idx = 0;
	String log_name;
	PrintStream log;
	boolean trace_enabled = true;

	public GameTracer(int buffer_size, String log_name) {
		trace_buffer = new String[buffer_size];
		this.log_name = log_name;
		try {
			log = new PrintStream(new BufferedOutputStream(new FileOutputStream(log_name, true)));
		} catch (Exception e) {
			System.err.println("Error creating trace file. " + e.getMessage());
			System.err.println("Tracing will be disabled.");
			trace_enabled = false;
		}
	}
	
	public void append(String line) {
		if (!trace_enabled) return;
		trace_buffer[current_idx++] = String.format("%08d. %s", total_idx++, line);
		if (current_idx == trace_buffer.length - 1) {
			dump();
			current_idx = 0; //clear the buffer. 
		}
	}
	
	public void finish() {
		if (!trace_enabled) return;
		for (int i = current_idx;i<trace_buffer.length-1;i++) {
			trace_buffer[i] = null;
		}
		dump();
		log.close();
		
	}
	private void dump() {
		try {
			for (int i = 0;i< trace_buffer.length;i++) {
				if (trace_buffer[i] == null) break;
				log.println(trace_buffer[i]); //finish() will put null for stale log entries.
			}
			log.flush();
		} catch (Exception e) {
			System.err.println("Error dumping trace data to file. " + e.getMessage());
		}
	}
}
