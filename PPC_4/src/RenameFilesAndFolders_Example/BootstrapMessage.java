package RenameFilesAndFolders_Example;


import library.Address;
import library.Message;

public class BootstrapMessage extends Message {

		private String oldName;
		private String newName;
	    private Address replyAdd;
	    
	    public BootstrapMessage(String oldName, String newName, Address replyAdd) {
	        super();
	        this.oldName = oldName;
			this.newName = newName;
	        this.replyAdd = replyAdd;
	    }
	    
		public String getOld() {
			return oldName;
		}
		public String getNew() {
			return newName;
		}
		public Address getReplyAdd() {
			return replyAdd;
		}



}

