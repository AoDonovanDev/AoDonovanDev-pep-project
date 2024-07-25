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
    public Optional<Account> login(String username, String password);
    public Optional<Message> createMessage(String messageBody, int posted_by, long time_posted_epoch);
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
            System.err.println(err.getMessage());
        }
        return accountOpt;
    }

    @Override
    public Optional<Account> login(String username, String password) {
        Optional<Account> accountOpt = Optional.empty();
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                if(rs.getString(password) == password) {
                    Account account = new Account();
                    account.setUsername(username);
                    account.setPassword(password);
                    account.setAccount_id(rs.getInt("account_id"));
                    accountOpt = Optional.of(account);
                }
            }
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
        return accountOpt;
    }

    @Override
    public Optional<Message> createMessage(String message_text, int posted_by, long time_posted_epoch) {
        Optional<Message> msgOpt = Optional.empty();
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message message_text, posted_by, time_posted_epoch VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(1, posted_by);
            ps.setLong(2, time_posted_epoch);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message();
                msg.setMessage_id(rs.getInt("message_id"));
                msg.setMessage_text(rs.getString("message_text"));
                msg.setPosted_by(rs.getInt("posted_by"));
                msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            }
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
        return msgOpt;
    }
}


