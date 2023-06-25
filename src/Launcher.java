import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Launcher extends Application implements Initializable {

    private Screen screen;
    @FXML
    private WebView browser,foodpartnernotificationsbrowser;
    private Stage primaryStage;
    private boolean itemsListPrint;
    private boolean isServerPrint = false;

    private JSONObject printInfoObj;
    private String printInvoiceId="";
    private JSObject javascriptConnector,apiJavascriptConnector;
    private String printType="imageprinting";
    private static String reportHtmlContent;

    JSONArray userStores = new JSONArray();
    private static DecimalFormat df = new DecimalFormat("#.##");
    WebEngine engine;
    Preferences prefs = Preferences.userRoot();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new StackPane());
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("views/LauncherView.fxml"));
//            root = loader.load(getClass().getClassLoader().getResource("views/AppointmentsScreen.fxml"));
            scene = new Scene(fxmlLoader.load(), 320, 240);
            primaryStage.setTitle("Hello!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();
        primaryStage.getIcons().add(new Image(Launcher.class.getResourceAsStream("assets/images/icon.png")));

        primaryStage.setTitle("SimplPOS");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = browser.getEngine();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();


        browser.setMaxWidth(bounds.getWidth());
        browser.setMaxHeight(bounds.getHeight());
        browser.setPrefWidth(bounds.getWidth());
        displayWeb();
        displayWebAPIAndNotifications();
    }


    private void displayWeb() {
        WebEngine engine = browser.getEngine();

        browser.getEngine().getLoadWorker()
                .stateProperty()
                .addListener((obs, old, neww) ->
                {
                    if (neww == Worker.State.SUCCEEDED)
                    {
                        // Let JavaScript make calls to adder object,
                        //so we need to inject an [Adder] object into the JS code
                        JSObject bridge = (JSObject) browser.getEngine()
                                .executeScript("window");
                        bridge.setMember("JSInterface", new JSInterface());

//                        ((JSObject)eng.executeScript("window")).setMember("JavaInit", new JavaInit());
                        javascriptConnector = (JSObject) browser.getEngine().executeScript("getJsConnector()");

                    }
                });
        //load the html page

        final JSInterface jsInterface = new JSInterface();
        engine.setJavaScriptEnabled(true);
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                final JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("JSInterface",  jsInterface);
            }
        });

        browser.getEngine().load(Launcher.class.getResource("assets/config.html").toString());
//        VBox box = new VBox(browser);
        Scene scene = new Scene(browser, Color.BLACK);
        browser.setStyle("-fx-background-color: black");


    }
    private void displayWebAPIAndNotifications(){

    }


    public class ApiJSInterface{
        public void  receivedNotificationForNewOrder()
        {
            System.out.println("Received the notification");

            javascriptConnector.call("showResult", "notificationReceivedPopup");
        }
        public void receivedNotificationForBackgroundRefresh()
        {

        }
    }
    public class JSInterface {
        public void showLog(String info) {
            System.out.println(info);
        }

        public void jsconnectorToJavaFX()
        {
            try {
                javascriptConnector = (JSObject) browser.getEngine().executeScript("getJsConnector()");
            }catch (Exception exp){
                exp.printStackTrace();
            }
        }







        public void goToPOS(){

            /* primaryStage = (Stage) browser.getScene().getWindow();

            POSController mc = new POSController();
            try {
                mc.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }

             */
        }
    }
}
