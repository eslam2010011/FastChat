package com.fastchat.utils.FirebaseUpload;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;


public class FileUploadResult  {

    public String name;
    public String mimeType;
    public String url;
    public Progress progress = new Progress();
    public int progress1;
    public HashMap<String, String> meta = new HashMap<>();

    public boolean urlValid() {
        return url != null && !url.isEmpty();
    }

}