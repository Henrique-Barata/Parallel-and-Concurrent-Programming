package RenameFilesAndFolders_Example;

import library.Message;

public class ResponseMessage extends Message{
	   private int n;

	    public ResponseMessage(int n) {
	        this.n = n;
	    }

	    public int getN() {
	        return n;
	    }
		
}
