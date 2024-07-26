package Service;

import java.util.List;
import java.util.Optional;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    private SocialMediaDAO dao;

    private SocialMediaService(){
        dao = new SocialMediaDAO();
    }

    public Optional<Account> createAccount(String username, String password) {
        if(dao.accountExists(username)) {
            return Optional.empty();
        }
        return dao.createAccount(username, password);
    }

    public Optional<Account> login(String username, String password) {
        if(!dao.accountExists(username)) {
            return Optional.empty();
        }
        return dao.login(username, password);
    }

    public Optional<Message> createMessage(String message_text, int posted_by, long time_posted_epoch) {
        return dao.createMessage(message_text, posted_by, time_posted_epoch);
    }

    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    public Optional<Message> getMessageById(int message_id) {
        return dao.getMessageByID(message_id);
    }

    public Optional<Message> deleteMessage(int message_id) {
        Optional<Message> msg = dao.getMessageByID(message_id);
        dao.deleteMessage(message_id);
        return msg;
    }

    public Optional<Message> updateMessage(int message_id, String message_text) {
        dao.updateMessage(message_id, message_text);
        Optional<Message> msg = dao.getMessageByID(message_id);
        return msg;
    }

    public List<Message> getMessagesByUser(int posted_by) {
        return dao.getMessagesByUser(posted_by);
    }
}
