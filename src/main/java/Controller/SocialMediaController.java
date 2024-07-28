package Controller;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("accouts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String body = context.body();
        Optional<Account> accOpt = Optional.empty();
        
        try {
            Account acc = om.readValue(body, Account.class);
            accOpt = SocialMediaService.createAccount(acc.getUsername(), acc.getPassword());
            if(accOpt.isPresent()) {
                String json = om.writeValueAsString(accOpt.get());
                context.status(200).json(json);
            } else {
                context.status(400);
            }
        } catch (JsonProcessingException err) {
            System.err.println(err.getMessage());
            context.status(400);
        }
        
    }

    private void loginHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String body = context.body();
        Optional<Account> accOpt = Optional.empty();

        try {
            Account acc = om.readValue(body, Account.class);
            accOpt = SocialMediaService.login(acc.getUsername(), acc.getPassword());
            if(accOpt.isPresent()) {
                String json = om.writeValueAsString(accOpt.get());
                context.status(200).json(json);
            } else {
                System.err.println("acc not present in route handler");
                context.status(401);
            }
        } catch (JsonProcessingException err) {
            System.err.println(err.getMessage());
            context.status(401);
        }
    }

    private void createMessageHandler(Context context) {
        ObjectMapper om = new ObjectMapper();
        String body = context.body();
        Optional<Message> msgOpt = Optional.empty();

        try {
            Message msg = om.readValue(body, Message.class);
            msgOpt = SocialMediaService.createMessage(msg.getMessage_text(), msg.getPosted_by(), msg.getTime_posted_epoch());
            if(msgOpt.isPresent()) {
                String json = om.writeValueAsString(msgOpt.get());
                context.status(200).json(json);
            } else {
                context.status(400);
            }
        } catch (JsonProcessingException err) {
            System.err.println(err.getMessage());
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) {
        context.json("get all msgs");
    }

    private void getMessageHandler(Context context) {
        context.json("get a message");
    }

    private void deleteMessageHandler(Context context) {
        context.json("delete a message");
    }

    private void updateMessageHandler(Context context) {
        context.json("update a message");
    }

    private void getMessagesByUserHandler(Context context) {
        context.json("get a users messages");
    }
}