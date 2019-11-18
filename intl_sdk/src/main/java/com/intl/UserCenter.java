package com.intl;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Dictionary;

/**
 * @Author: yujingliang
 * @Date: 2019/11/18
 */
public class UserCenter {
    private static final String LOGIN_CENTER_WEB_COMMAND_DOMAIN = "yc.mobilesdk.logincenter";
    private static UserCenter _instance;
    private Uri _uri;
    private  int _dialogWidth;
    private  int _dialogHeight;
    private WebSession _webSession;
    private Context context;
    public static void init(Context context,Uri uri, int  width, int height){

        if (_instance == null)
            _instance = new UserCenter( context,uri, width, height);
    }
    public static UserCenter getInstance() {

        return _instance;
    }
    private UserCenter(Context context, Uri uri,int width,int height)
    {
        this.context = context;
        _uri = uri;
        _dialogWidth = width;
        _dialogHeight = height;
        _webSession = new WebSession();
        registCommand();
    }
    public void showLoginWebView(Activity activity) {
        _webSession.showDialog(activity,
                _dialogWidth,
                _dialogHeight,
                _uri, false);
    }
    private void registCommand()
    {
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Google",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("UserCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GoogleSDK googleSDK = new GoogleSDK((Activity) context);
                                googleSDK.SignIn();
                            }
                        }).start();

                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Facebook",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("UserCenter", "handleCommand: "+command);
                        WebSession.currentWebSession().forceCloseSession();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FaceBookSDK faceBookSDK = new FaceBookSDK((Activity) context);
                                faceBookSDK.login();
                            }
                        }).start();

                    }
                }

        );
        _webSession.regisetCommandListener(LOGIN_CENTER_WEB_COMMAND_DOMAIN,"Guest",
                new WebSession.IWebCommandListener() {

                    @Override
                    public void handleCommand(WebCommandSender sender, String commandDomain, String command, Dictionary<String, String> args) {
                        Log.d("UserCenter", "handleCommand: "+command);
                    }
                }

        );

    }
}
