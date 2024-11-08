package RenameFilesAndFolders_Example;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import library.Address;

public class Main {
	
    public static void main(String[] args) {

    	File rootFile = new File ("tree");
        RenameActor root = new RenameActor (rootFile);
               
        Costumer client = new Costumer(root.getAddress());
        client.getAddress().sendMessage(new BootstrapMessage("2","o", root.getAddress()));

    }
}
