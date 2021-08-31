# FastChat


## Features
- Powered by Firebase
- 1-to-1 Messaging
- Delivery receipts
- Typing indicator
- Last online
- Audio messages
- Video messages
- Reply messages
- Link messages
- Text messages
- File Messages
- Reaction 

## Benefits
 - Hassle free coding



# Screenshots



# Installation





# Usage
Step 1 : The best place to initialize the Fast Chat is in the onCreate method of your Application subclass.
```java
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // Fast chat intialization goes here
         UiConfig uiConfig = new UiConfig();
        uiConfig.setButtonVideo(true);
        uiConfig.setButtonVoice(true);
        uiConfig.setInfoView(true);
        uiConfig.setBottomView(true);
        uiConfig.setColorGeneral("#3D51FE");
        uiConfig.setBackgroundChat(R.drawable.chat_bg);
        uiConfig.setIncludeReactions(true);
        uiConfig.setReactions(Reaction.getReactions());
        FastChat.init(this, uiConfig,new NotificationContractorX());


    }
}
```


Step 1 : Ceat User in fierbaes.
```java
        FastChat.getFastChat().getChatInteract().createUser("1", "Eslam");
```



