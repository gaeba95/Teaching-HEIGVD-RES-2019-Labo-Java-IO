package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;


/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  private boolean start;
  private boolean back;
  private int cnt;
  

  public FileNumberingFilterWriter(Writer out) {
    super(out);
    cnt = 1;
    start = true;
    back = false;
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
	  char[] cbuf = str.toCharArray();
      this.write(cbuf,off,len);
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
	  for(int i = off; i < off + len; i++){
          this.write(cbuf[i]);
      }
  }

  @Override
  public void write(int c) throws IOException {
	  if(start){
          start = false;
          this.out.write(cnt++ + "\t");
      }
	  
	  if(c == '\r') {
		  if(back){
              this.out.write("\r" + cnt++ + "\t");
          }
		  back = true;
	  } else if(c == '\n') {
		  if(back) {
			  this.out.write("\r\n" + cnt++ + "\t");
			  back = false;
		  } else {
			  this.out.write("\n" + cnt++ + "\t");
		  }

	  } else {
		  if(back) {
              this.out.write("\r"  + cnt++ + "\t" + (char)c);
              back = false;
		  } else {
			  this.out.write((char) c);
		  }
	  }
  }
}
