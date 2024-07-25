package Controller;

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
        app.post("register", this::register);
        app.post("login", this::login);
        app.post("messages", this::createMessage);
        app.get("messages", this::getAllMessages);
        app.get("messages/{message_id}", this::getMessage);
        app.delete("messages/{message_id}", this::deleteMessage);
        app.patch("messages/{message_id}", this::updateMessage);
        app.get("accouts/{account_id}/messages", this::getMessagesByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void register(Context context) {
        context.json("register");
    }

    private void login(Context context) {
        context.json("login");
    }

    private void createMessage(Context context) {
        context.json("create message");
    }

    private void getAllMessages(Context context) {
        context.json("get all msgs");
    }

    private void getMessage(Context context) {
        context.json("get a message");
    }

    private void deleteMessage(Context context) {
        context.json("delete a message");
    }

    private void updateMessage(Context context) {
        context.json("update a message");
    }

    private void getMessagesByUser(Context context) {
        context.json("get a users messages");
    }
}