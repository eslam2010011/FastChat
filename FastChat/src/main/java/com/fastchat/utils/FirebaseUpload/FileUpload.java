package com.fastchat.utils.FirebaseUpload;

import android.net.Uri;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class FileUpload extends ViewModel {
    public static MutableLiveData<Boolean> UploadMutableLiveData=new MutableLiveData<>();
    public void addDataUser(String url,String user_id,String name){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image").child("RandomName");
        storageReference.putFile(Uri.parse(url)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String,String> user=new HashMap<>();
                        user.put("id",user_id);
                        user.put("name",name);
                        user.put("image",uri.toString());
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                        databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                UploadMutableLiveData.setValue(true);

                            }
                        });
                    }
                });

            }
        });

    }


    public MutableLiveData<Boolean> getData() {
        return UploadMutableLiveData;
    }


}
