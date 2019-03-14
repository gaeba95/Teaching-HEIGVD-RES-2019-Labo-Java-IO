package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;
import java.util.Arrays;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 * 
 * @author Olivier Liechti
 * Modified by : Bacso Gaetan
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
	  
	  if(!rootDirectory.exists()) {
      	vistor.visit(rootDirectory);
      } else {
    	  if(rootDirectory.isFile()) {
    		  vistor.visit(rootDirectory);
            }else if(rootDirectory.isDirectory()) {
            	
              File[] files = rootDirectory.listFiles();
              Arrays.sort(files);
              vistor.visit(rootDirectory);
              for(File son : files) {
            	 explore(son, vistor);
            	  
              }
              
            }
      }
  }

}
