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
 * Modified by : Bacso Gaetan
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());
  // Variable de détection d'un '\r'
  private boolean back;
  // Compteur du nombre de ligne
  private int cnt;
  

  public FileNumberingFilterWriter(Writer out) {
    super(out);
    // Initalisation
    cnt = 0;
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
	  // Cas pour la première ligne
	  if(cnt == 0){
          this.out.write(++cnt + "\t");
          cnt++;
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
