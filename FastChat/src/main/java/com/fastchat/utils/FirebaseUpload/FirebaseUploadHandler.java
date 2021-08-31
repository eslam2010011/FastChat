package com.fastchat.utils.FirebaseUpload;

import android.content.Context;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class FirebaseUploadHandler  {



    public static Observable<FileUploadResult> uploadFile2(Context context,final File data, final String name, final String mimeType) {
        return Observable.create((ObservableOnSubscribe<FileUploadResult>) e -> {
            final String fullName = generateRandomName();
            SharedPreferencesUtils.saveUid(context,fullName);
            String uid=SharedPreferencesUtils.getUid(context);
            StorageReference filesRef = storage().getReference().child(name).child(uid);
           StorageReference fileRef = filesRef.child(uid);
            final FileUploadResult result = new FileUploadResult();
            UploadTask uploadTask = fileRef.putBytes(ParseFileUtils.readFileToByteArray(data));
            uploadTask.addOnProgressListener(taskSnapshot -> {
                result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getBytesTransferred());
                result.progress1 = (int) ((int)(100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                 // e.onNext(result);
            }).addOnSuccessListener(taskSnapshot -> {
                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    result.name = uid;
                    result.mimeType = mimeType;
                    result.url = uri.toString();
                    result.progress.set(taskSnapshot.getTotalByteCount(), taskSnapshot.getTotalByteCount());
                     e.onNext(result);
                    e.onComplete();
                });
            });

        }).subscribeOn(Schedulers.single());
    }



    public static Observable<String[]> observeImageSources(Context context,final String name, List<String> uriList) {
        List<String> hashMap = new ArrayList<>();
        return Observable.create((ObservableOnSubscribe<String[]>) e -> {
            for (String a :uriList){
                FirebaseUploadHandler.uploadFile2(context,new File(a.toString()), name, "") .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<FileUploadResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FileUploadResult fileUploadResult) {
                        hashMap.add(fileUploadResult.url);
                        String[] stringA = new String[hashMap.size()];
                        hashMap.toArray(stringA);
                        if (uriList.size()-1==hashMap.size()){
                            e.onNext(stringA);
                            e.onComplete();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }



        }).subscribeOn(Schedulers.single());

    }




    public Observable<String> ShakeDetector2(final Context context) {
        return Observable.create((ObservableOnSubscribe<String>) e -> {

        }).subscribeOn(Schedulers.single());
    }






    public void uploadFile(){

    }

    public boolean shouldUploadAvatar () {
        return true;
    }

    public static FirebaseStorage storage () {
        return FirebaseStorage.getInstance();


    }
    public static String generateRandomName() {
        return new BigInteger(20, new Random()).toString(50);
    }

}