package RenameFilesAndFolders_Example;


import java.io.File;
import java.util.ArrayList;

import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;

public class RenameActor extends Actor {
	
    File file;
    ArrayList<Address> workers;
    ArrayList<Message> messages;

    public RenameActor(File name) {
        this.file = name;
        workers = new ArrayList<>();
        messages = new ArrayList<>();        
        createNewRamification();       
    }
    
    public RenameActor(File name, Address address) {
    	super(address);
        this.file = name;
        workers = new ArrayList<>();
        messages = new ArrayList<>();      
        createNewRamification();
    }
    
    public void createNewRamification() {
    	if(file.listFiles()!= null) {
	    	for (File fil : file.listFiles()) {
	        	RenameActor renameActor = new RenameActor (fil, this.getAddress());
	        	workers.add(renameActor.getAddress());           
	        }  
    	}
    }
    
    @Override
    protected void handleMessage(Message m) {
    	
        if (m instanceof SystemKillMessage) {
        	
            for (Address worker : workers) {
            	worker.sendMessage(m);
            }
            
        } else if (m instanceof RenameFileMessage) {
            int renamesNumber= 0;
            RenameFileMessage renameMessage = (RenameFileMessage) m;
            
            for (Address worker : workers) {
            	RenameFileMessage newMessage = new RenameFileMessage(renameMessage.getOldName(), renameMessage.getNewName(), this.getAddress());
            	worker.sendMessage(newMessage);
            }
            int respostas = 0;
            
            while (respostas < workers.size()) {
                Message nextMessage = mailbox.poll();
                
                if (nextMessage == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                
                if (nextMessage instanceof ResponseMessage) {
                	ResponseMessage response = (ResponseMessage) nextMessage;
                	renamesNumber += response.getN();
                    respostas++;
                    
                } else {
                    messages.add(nextMessage);
                }
            }            
            String oldName = file.getName();
            
            if (oldName.contains(renameMessage.getOldName())) {           	
                String newName = oldName.replace(renameMessage.getOldName(), renameMessage.getNewName());
                boolean bool = false;
                File newFile = new File(file.getParent() + "\\" + newName); 
                if (file.renameTo(newFile)) {               	
                    file = newFile;
                    bool = true;
                }else {
                	int i = 0;
                	while(i<100){
                		try {
							Thread.sleep(100);
							if (file.renameTo(newFile)) {
			                    file = newFile;
			                    bool = true;
			                    i = 100;
			                }
							i++;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                	}
                }
                
                file = new File(file.getParent() + "\\" + newName);
                renameMessage.getReplyAdd().sendMessage(new ResponseMessage(renamesNumber + (bool ? 1 : 0)));
                
            } else {
                renameMessage.getReplyAdd().sendMessage(new ResponseMessage(renamesNumber));
            }
            
            if(!messages.isEmpty()) {
            Message message = messages.get(0);
            messages.remove(0);
            handleMessage(message);
            
            }
        }
    }
    
}
