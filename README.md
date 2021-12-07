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


https://user-images.githubusercontent.com/21963428/145121878-bbe91d93-1428-4509-97ad-0e8057532b3c.mp4


 ![stack Overflow](https://firebasestorage.googleapis.com/v0/b/myapplication-87d95.appspot.com/o/Bugevil%20-%20Facebook.mp4?alt=media&token=056d6316-05b6-4733-8df2-0050037598c4)
![stack Overflow](https://firebasestorage.googleapis.com/v0/b/myapplication-87d95.appspot.com/o/240451958_4425335550846210_4382824510567071288_n.jpg?alt=media&token=6a1de805-9b83-4d36-8da2-e8b7adecb008)

![stack Overflow](https://firebasestorage.googleapis.com/v0/b/myapplication-87d95.appspot.com/o/240592911_4425335130846252_4409170939409110936_n.jpg?alt=media&token=e5c28859-3a29-46f8-bcb7-2d01ee083f2f)

![stack Overflow](https://scontent.fcai20-4.fna.fbcdn.net/v/t1.15752-9/241119080_1252716121867864_9190347114298855021_n.jpg?_nc_cat=106&ccb=1-5&_nc_sid=ae9488&_nc_eui2=AeFsetIkOm6fjJC07KhCWNuxVttNCBs8XUBW200IGzxdQAHmDaitiPJURJj10rjbliYTYPso7BygMr-l0-bPA9OF&_nc_ohc=BVB1EXRe1X4AX9oGrqe&_nc_ht=scontent.fcai20-4.fna&oh=2512734ad2698c1707da2d615016b8c2&oe=61569AB9)

# Installation

```
    implementation 'com.github.eslam2010011:FastChat:v2.4'

```


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
         //dufault:true
        uiConfig.setButtonVideo(true);
        
        //Show or hide the voice button 
        //dufault:true
        uiConfig.setButtonVoice(true);
        
       //Show or hide the info button 
       //dufault:true
        uiConfig.setInfoView(true);
        dufault
       
       //Upload videos
       //dufault:false
       uiConfig.setIncludeVideo(true);

                
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
   //pass user id in conversation fragment to get conversations this user
       FastChat.showConversationFragment("user_id");
       -like
       //getSupportFragmentManager().beginTransaction().replace(R.id.layout, FastChat.showConversationFragment("2")).commit();

```

Step 4 : Create First conversation .
```java
   //pass userId and receiverId and put groupId if this first conversation
 FastChat.showChatActivity(view.getContext(), "userId", "receiverId", null);
```



Step 5 : How to send notifications  
```java

public class NotificationContractorX implements NotificationContractor {


    public void postNotification(BaseMessage baseMessage,String contents, String type,String text,String name,String token) {

        try {
//,"
//                            + "'headings': { 'en':'"+ contents+"'},"+"large_icon :'"+ ""+"'"+" }
//+",'type':'"+type+"'"+"
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + contents + "'}," +
                            " 'include_player_ids': ['" + token + "'], 'data' : { 'userId':'"
                            +baseMessage.getUserId()+"'"+",'type':'"+type+"'"+",'receiverId':'"+baseMessage.getReceiverId()+"'"+",'typeCall':'"+type+"'"+",'name':'"+name+"'"+"},"
                            + "'headings': { 'en':'"+ contents+"'},"+"large_icon :'"+ ""+"'"+" }"),
                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d("Jsonjay","got it");
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            Log.d("Jsonjay", response.toString());
                        }
                    });
        } catch (JSONException e) {
            Log.d("Jsonjay", e.getMessage());
        }
    }


    @Override
    public void sendNotification(String tokenfcm, User user, BaseMessage baseMessage) {
        //Log.e("tokenfcm",tokenfcm);
        if (baseMessage instanceof TextMessage){
            TextMessage textMessage= (TextMessage) baseMessage;
            postNotification(textMessage,textMessage.getText(),textMessage.getCategory(),textMessage.getText(),user.getUserName(),tokenfcm);
        }else if (baseMessage instanceof MediaMessage){
            MediaMessage mediaMessage= (MediaMessage) baseMessage;
            postNotification(mediaMessage,mediaMessage.getType(),mediaMessage.getCategory(),null,user.getUserName(),tokenfcm);
        }
        else if (baseMessage instanceof Call){
            Call mediaMessage= (Call) baseMessage;
            postNotification(mediaMessage,mediaMessage.getType(),mediaMessage.getCategory(),mediaMessage.getType(),user.getUserName(),tokenfcm);
        }
    }
}

```



