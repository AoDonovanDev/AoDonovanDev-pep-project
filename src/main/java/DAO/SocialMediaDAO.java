package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

interface SocialMediaDAOInter {
    public Optional<Account> createAccount(String username, String password);
    public Account login(String username, String password);
    public Message createMessage(String messageBody, int posted_by);
    public List<Message> getAllMessages();
    public Message getMessageByID(int message_id);
    public Message deleteMessage(int message_id);
    public Message updateMessage(int message_id);
    public List<Message> getMessagesByUser(int posted_by);
    public Boolean accountExists(String username);
    public Boolean messageExists(int message_id);
}


public class SocialMediaDAO implements SocialMediaDAOInter {

    @Override
    public Boolean accountExists(String username) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException err){
            System.err.println(err.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Account> createAccount(String username, String password) {
        //passwords are not hashed :(((((( this makes me sad.
        Optional<Account> accountOpt = Optional.empty();
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet accResult = ps.executeQuery();
            while(accResult.next()) {
                Account account = new Account();
                account.setAccount_id(accResult.getInt("account_id)"));
                account.setUsername(accResult.getString(username));
                account.setPassword(accResult.getString(password));
                accountOpt = Optional.of(account);
            }
        } catch(SQLException err) {
            System.err.println(err);
        }
        return accountOpt;
    }
}


