package DAO;

import java.util.List;

import Model.Account;
import Model.Message;

interface SocialMediaDAOInter {
    public Account createAccount(String username, String password);
    public Account login(String username, String password);
    public Message createMessage(String messageBody, int posted_by);
    public List<Message> getAllMessages();
    public Message getMessageByID(int message_id);
    public Message deleteMessage(int message_id);
    public Message updateMessage(int message_id);
    public List<Message> getMessagesByUser(int posted_by);
}


public class SocialMediaDAO {
    
}


