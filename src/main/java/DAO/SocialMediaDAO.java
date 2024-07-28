package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Statement;


import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

interface SocialMediaDAOInter {
    public void createAccount(String username, String password);
    public Optional<Account> login(String username, String password);
    public Optional<Integer> createMessage(String messageBody, int posted_by, long time_posted_epoch);
    public List<Message> getAllMessages();
    public Optional<Message> getMessageByID(int message_id);
    public void deleteMessage(int message_id);
    public void updateMessage(int message_id, String message_text);
    public List<Message> getMessagesByUser(int posted_by);
    public Boolean accountExists(String username);
    public Boolean accountExists(int account_id);
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
    public Boolean accountExists(int account_id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(SQLException err){
            System.err.println(err.getMessage());
        }
        return false;
    }

    @Override
    public Boolean messageExists(int message_id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
        return false;
    }

    @Override
    public void createAccount(String username, String password) {
        //passwords are not hashed :(((((( this makes me sad.
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            System.err.println(ps.toString());
            ps.execute();
        } catch(SQLException err) {
            System.err.println("the sql is fucking up: " + err.getMessage());
        }
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
                if(rs.getString("password").compareTo(password) == 0) {
                    Account account = new Account();
                    account.setUsername(username);
                    account.setPassword(password);
                    account.setAccount_id(rs.getInt("account_id"));
                    accountOpt = Optional.of(account);
                }
            }
        } catch (SQLException err) {
            System.err.println("error in login sql: " + err.getMessage());
        }
        return accountOpt;
    }

    @Override
    public Optional<Integer> createMessage(String message_text, int posted_by, long time_posted_epoch) {
        //this should actually return an int of the message id
        Optional<Integer> msgIdOpt = Optional.empty();
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (message_text, posted_by, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, message_text);
            ps.setInt(2, posted_by);
            ps.setLong(3, time_posted_epoch);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()){
                msgIdOpt = Optional.of(rs.getInt("message_id"));
            }
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
        return msgIdOpt;
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<Message>();

        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message();
                msg.setPosted_by(rs.getInt("posted_by"));
                msg.setMessage_id(rs.getInt("message_id"));
                msg.setMessage_text(rs.getString("message_text"));
                msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                allMessages.add(msg);
            }
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

        return allMessages;
    }

    @Override
    public Optional<Message> getMessageByID(int message_id) {
        Optional<Message> msgOpt = Optional.empty();

        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message();
                msg.setMessage_id(message_id);
                msg.setMessage_text(rs.getString("message_text"));
                msg.setPosted_by(rs.getInt("posted_by"));
                msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                msgOpt = Optional.of(msg);
            }

        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
        System.err.println("[data layer - getById] msgOpt: " + msgOpt.toString());
        return msgOpt;
    }

    @Override
    public void deleteMessage(int message_id) {
        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "DELETE * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ps.execute();
        } catch (SQLException err) {
            System.err.println(err.getMessage()); 
        }
    }

    @Override
    public void updateMessage(int message_id, String message_text) {

        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(2, message_id);
            ps.executeUpdate();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    @Override
    public List<Message> getMessagesByUser(int posted_by) {
        List<Message> messagesByUser = new ArrayList<Message>();

        try {
            Connection conn = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message msg = new Message();
                msg.setPosted_by(rs.getInt("posted_by"));
                msg.setMessage_id(rs.getInt("message_id"));
                msg.setMessage_text(rs.getString("message_text"));
                msg.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messagesByUser.add(msg);
            }
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }

        return messagesByUser;
    }
}


