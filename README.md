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

 ![stack Overflow](https://scontent.fcai20-4.fna.fbcdn.net/v/t1.15752-9/241004658_2949826991945775_4212666001145548843_n.jpg?_nc_cat=107&ccb=1-5&_nc_sid=ae9488&_nc_eui2=AeF2OXBvDv_W-Ue_CSGAUpA8IFuF1UZo2D0gW4XVRmjYPciYlb4hcFQXPDbAtXOKDRFOzqrSL1TpM-EhJ2XeBzT2&_nc_ohc=wCZU5QBrijUAX9EcyYo&_nc_ht=scontent.fcai20-4.fna&oh=f9f0b46e105d01e8c1a630d19e873279&oe=6156F338)
![stack Overflow](https://scontent.fcai20-4.fna.fbcdn.net/v/t1.15752-9/241159576_559584655460376_3607386523307528977_n.jpg?_nc_cat=106&ccb=1-5&_nc_sid=ae9488&_nc_eui2=AeGmzJ84SzLpPuvvzsU7DPjxwcLOMYXPqXDBws4xhc-pcO8fVXiWSYY7Qk7GVPJiTqd89pCZv2KhWYqfEJ2nmL4d&_nc_ohc=4yvY70akuHMAX9ZpS6X&tn=FjFhx3Ta03IebPj8&_nc_ht=scontent.fcai20-4.fna&oh=bc06e36947aa519d233a9abbeaaeae07&oe=6153B328)

![stack Overflow](https://scontent.fcai20-4.fna.fbcdn.net/v/t1.15752-9/241043273_3147401568827902_7968897454293483005_n.jpg?_nc_cat=111&ccb=1-5&_nc_sid=ae9488&_nc_eui2=AeEp7hsPERCikoFieePwX6bunR_ye0tdQtCdH_J7S11C0Gq2sYTyISvZ1l8oOOIE7CwMv4EkmIUAE91NokA614I9&_nc_ohc=jV0iYh6wGNMAX8a0tVb&tn=FjFhx3Ta03IebPj8&_nc_ht=scontent.fcai20-4.fna&oh=818dd2f25694651841e506b3bb2ae0ea&oe=61560302)

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



