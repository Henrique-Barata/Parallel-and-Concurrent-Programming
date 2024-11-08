package RenameFilesAndFolders_Example;

import library.Address;
import library.Message;

public class RenameFileMessage extends Message {
    private String oldName;
    private String newName;
    private Address replyAdd;

    public RenameFileMessage(String oldName, String newName, Address replyAdd) {
        this.oldName = oldName;
        this.newName = newName;
        this.replyAdd = replyAdd;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }

    public Address getReplyAdd() {
        return replyAdd;
    }
}