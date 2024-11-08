package RenameFilesAndFolders_Example;


import library.Actor;
import library.Address;
import library.Message;
import library.SystemKillMessage;


public class Costumer extends Actor {
	
 	Address serverAddress;
    int messagesToReceive = 0;

    public Costumer(Address address) {
        this.serverAddress = address;
    }

    @Override
    protected void handleMessage(Message m) {
    	
        if (m instanceof BootstrapMessage) {
            System.out.println("Costumer Bootstrap");
            BootstrapMessage bootstrap = (BootstrapMessage) m;
            String oldName = bootstrap.getOld();
            String newName = bootstrap.getNew();
            messagesToReceive++;
            serverAddress.sendMessage(new RenameFileMessage(oldName, newName, this.getAddress()));  
            
        } else if (m instanceof RenameFileMessage) {
            System.out.println("Custumer recebeu menssagem errada");
            
        } else if (m instanceof ResponseMessage) {
        	ResponseMessage replyMessage = (ResponseMessage) m;
            System.out.println("Houve " + replyMessage.getN() + " alteraçoes");
            messagesToReceive--;
            
            if (messagesToReceive == 0) {
            	serverAddress.sendMessage(new SystemKillMessage());
                this.getAddress().sendMessage(new SystemKillMessage());
            }
            
        } else if (m instanceof SystemKillMessage) {
            System.out.println("System Kill");
            
        } else {
            System.out.println("Messagem nao reconhecida " + m.getClass());
        }
    }

}

