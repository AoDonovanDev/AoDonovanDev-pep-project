package Service;

import java.util.List;
import java.util.Optional;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    static SocialMediaDAO dao = new SocialMediaDAO();

    private SocialMediaService(){}

    public static Optional<Account> createAccount(String username, String password) {
        if(dao.accountExists(username) || username.length()<1 || password.length()<4) {
            System.err.println("[service layer] account exists already or invalid username/password");
            return Optional.empty();
        }
        dao.createAccount(username, password);
        return dao.login(username, password);
    }

    public static Optional<Account> login(String username, String password) {
        if(!dao.accountExists(username)) {
            System.err.println("[service layer] no account with that username found");
            return Optional.empty();
        }
        return dao.login(username, password);
    }

    public static Optional<Message> createMessage(String message_text, int posted_by, long time_posted_epoch) {
        if(message_text.length() > 255 || message_text.length() == 0 || !dao.accountExists(posted_by)) {
            System.err.println("[service layer] message or account_id is invalid");
            return Optional.empty();
        }
        Optional<Integer> msgIdOpt =  dao.createMessage(message_text, posted_by, time_posted_epoch);
        if(msgIdOpt.isPresent()) {
            return dao.getMessageByID(msgIdOpt.get());
        } else {
            return Optional.empty();
        }
    }

    public static List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    public static Optional<Message> getMessageById(int message_id) {
        return dao.getMessageByID(message_id);
    }

    public static Optional<Message> deleteMessage(int message_id) {
        Optional<Message> msg = dao.getMessageByID(message_id);
        dao.deleteMessage(message_id);
        return msg;
    }

    public static Optional<Message> updateMessage(int message_id, String message_text) {
        if(message_text.length() > 255 || message_text.length() == 0) {
            return Optional.empty();
        }
        dao.updateMessage(message_id, message_text);
        Optional<Message> msg = dao.getMessageByID(message_id);
        return msg;
    }

    public static List<Message> getMessagesByUser(int posted_by) {
        return dao.getMessagesByUser(posted_by);
    }
}
