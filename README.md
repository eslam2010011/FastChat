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
- Typing

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
        
        //
         UiConfig uiConfig = new UiConfig();
         
         //Show or hide the video button 
        uiConfig.setButtonVideo(true);
        
        //Show or hide the voice button 
        uiConfig.setButtonVoice(true);
        
       //Show or hide the info button 
        uiConfig.setInfoView(true);
                
        //Change all button colors
        uiConfig.setColorGeneral("#3D51FE");
        //activate  reaction 
         uiConfig.setIncludeReactions(true);

          //default reaction
          uiConfig.setReactions(Reaction.getReactions());

         // add more reaction 
        //url_reaction  this link image (go to https://emojipedia.org/facebook/   and copy link any reaction image and pass to url_reaction  )
         //Key_reaction this Sting key unique 
         List<Reaction> list=new ArrayList<>();
        list.add(new Reaction("url_reaction","Key_reaction"));
        
        uiConfig.setReactions(list);
        
        FastChat.init(this, uiConfig,new NotificationContractorX());


    }
}
```


Step 2 : Create User.
```java
        FastChat.getFastChat().getChatInteract().createUser("user_id", "userName");
        
        //Or
        
      FastChat.getFastChat().getChatInteract().createUser(new User("userName","userId","userProfile","userEmail"));

```

Step 3 : Get Conversation.
```java
   //pass user id in conversation fragment to get conversations this uer
       FastChat.showConversationFragment("user_id");
       -like
       //getSupportFragmentManager().beginTransaction().replace(R.id.layout, FastChat.showConversationFragment("2")).commit();

```

Step 4 : Create First conversation .
```java
   //pass userId and receiverId and put groupId if this first conversation
 FastChat.showChatActivity(view.getContext(), userId, key, baseMessage.getGroupId());
```



