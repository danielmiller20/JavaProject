package com.foodie.app.onlineDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;


import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.Helper.HelperClass;
import com.foodie.app.backend.AppContract;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.Converters;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DataStatus;
import com.foodie.app.database.IDBManager;
import com.foodie.app.entities.Activity;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.foodie.app.entities.User;
import com.foodie.app.listsDB.ListDBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.InvalidPropertiesFormatException;

/**
 * Created by David on 9/1/2017.
 */

public class firebaseDB implements IDBManager {

    private ListDBManager localDB;

    private DatabaseReference mDatabase;
    private DatabaseReference CPUserRef;
    private DatabaseReference BusinessRef;
    private DatabaseReference ActivityRef;
    private DatabaseReference UserRef;
    private boolean login; // check if the user did a login
    private boolean dataUpdated;


    private FirebaseUser user = null;

    private FirebaseAuth mAuth;


    public firebaseDB() {

        mAuth = FirebaseAuth.getInstance();
        login = false;
        dataUpdated = false;

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    DebugHelper.Log("User authenticated: " + user.getEmail());
                    login = true;
                    DBManagerFactory.setCurrentUser(new CPUser(user.getUid(),user.getEmail(),user.getDisplayName()));
           //         ListDBManager.removeOthersUsers();
                    dataUpdated = true;
                    onCreate();

                } else {
                    login = false;

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }


    private void onCreate() {

        // User is signed in
        localDB = new ListDBManager();
        // Write a message to the database
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mDatabase.addChildEventListener(new onOnlineDBChange(localDB));

        CPUserRef = mDatabase.child("CPUser");
        BusinessRef = mDatabase.child("Business");
        ActivityRef = mDatabase.child("Activity");
        UserRef = mDatabase.child("User");

    }

    @Override
    public String addCPUser(ContentValues values) throws Exception {
        if (!login)
            throw new InvalidPropertiesFormatException("No user login");

        if (values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        CPUser cpuser = Converters.ContentValuesToCPUser(values);
        CPUserRef.push().setValue(cpuser);
        return "";
    }


    @Override
    public String addBusiness(ContentValues values) throws Exception {
     
        if (values == null)
           throw new NullPointerException("ContentValues is null");

        Business business = Converters.ContentValuesToBusiness(values);
        DatabaseReference toInsert = BusinessRef.push();
        if(business.getBusinessName() != null)
            toInsert.child(AppContract.Business.BUSINESS_NAME).setValue(business.getBusinessName());
        if(business.getBusinessAddress() != null)
            toInsert.child(AppContract.Business.BUSINESS_ADDRESS).setValue(business.getBusinessAddress());

        toInsert.child(AppContract.Business.BUSINESS_CPUSER_ID).setValue(DBManagerFactory.getCurrentUser().get_ID());

        if(business.getBusinessEmail() != null)
            toInsert.child(AppContract.Business.BUSINESS_EMAIL).setValue(business.getBusinessEmail());
        if(business.getBusinessPhoneNo() != null)
            toInsert.child(AppContract.Business.BUSINESS_PHONE_NUMBER).setValue(business.getBusinessPhoneNo());
        if(business.getBusinessWebsite() != null)
            toInsert.child(AppContract.Business.BUSINESS_WEBSITE).setValue(business.getBusinessWebsite());

        String str = HelperClass.fromByteArraytoString(business.getBusinessLogo());
        if(!str.isEmpty())
            toInsert.child(AppContract.Business.BUSINESS_LOGO).setValue(str);

        return "";
    }

    @Override
    public String addActivity(ContentValues values) throws Exception {
        if(values == null)
            throw new NullPointerException("ContentValues is null");
        Activity activity = Converters.ContentValuesToActivity(values);
        DatabaseReference toInsert = ActivityRef.push();
        toInsert.child(AppContract.Activity.ACTIVITY_NAME).setValue(activity.getActivityRating());
        toInsert.child(AppContract.Activity.ACTIVITY_DESCRIPTION).setValue(activity.getActivityDescription());
        toInsert.child(AppContract.Activity.ACTIVITY_COST).setValue(activity.getActivityCost());
        toInsert.child(AppContract.Activity.ACTIVITY_RATING).setValue(activity.getActivityRating());
        toInsert.child(AppContract.Activity.ACTIVITY_BUSINESS_ID).setValue(activity.getBusinessId());
        toInsert.child(AppContract.Activity.ACTIVITY_FEATURE).setValue(activity.getFeature());
        String str = HelperClass.fromByteArraytoString(activity.getActivityImages());
        toInsert.child(AppContract.Activity.ACTIVITY_IMAGE).setValue(str);
        return "";
    }

    @Override
    public String addUser(ContentValues values) throws Exception {

     
        if (values == null) {
            throw new NullPointerException("ContentValues is null");
        }
        User users = Converters.ContentValuesToUser(values);
        UserRef.push().setValue(users);
        return "";
    }

    @Override
    public boolean removeCPUser(String id) throws Exception {

        return false;
    }

    @Override
    public boolean removeBusiness(String id) throws Exception {
        BusinessRef.child(id).removeValue();
        return false;
    }

    @Override
    public boolean removeActivity(String id) throws Exception {
        ActivityRef.child(id).removeValue();
        return false;
    }

    @Override
    public boolean removeUser(String id) throws Exception {
        return false;
    }

    @Override
    public Cursor getCPUser(String[] args, String[] columnsArgs) throws Exception {
     

        return localDB.getCPUser(args, columnsArgs);
    }

    @Override
    public Cursor getBusiness(String[] args, String[] columnsArgs) throws Exception {
     
        return localDB.getBusiness(args, columnsArgs);
    }

    @Override
    public Cursor getActivity(String[] args, String[] columnsArgs) throws Exception {
     
        return localDB.getActivity(args, columnsArgs);
    }

    @Override
    public Cursor getUser(String[] args, String[] columnsArgs) throws Exception {
     
        return localDB.getUser(args, columnsArgs);
    }

    @Override
    public boolean updateCPUser(String id, ContentValues values) throws Exception {

        CPUser toUpdate = Converters.ContentValuesToCPUser(values);
        toUpdate.set_ID(DBManagerFactory.getCurrentUser().get_ID());
        CPUserRef.child(DBManagerFactory.getCurrentUser().get_ID()).updateChildren(toUpdate.toMap());
        return true;
    }

    @Override
    public boolean updateBusiness(String id, ContentValues values) throws Exception {

        Business toUpdate = Converters.ContentValuesToBusiness(values);
        toUpdate.setCpuserID(DBManagerFactory.getCurrentUser().get_ID());
        BusinessRef.child(toUpdate.get_ID()).updateChildren(toUpdate.toMap());
        return true;
    }

    @Override
    public boolean updateActivity(String id, ContentValues values) throws Exception {

        Activity toUpdate = Converters.ContentValuesToActivity(values);
        ActivityRef.child(toUpdate.get_ID()).updateChildren(toUpdate.toMap());
        return true;
    }

    @Override
    public boolean updateUser(String id, ContentValues values) throws Exception {

        User toUpdate = Converters.ContentValuesToUser(values);
        UserRef.child(toUpdate.get_ID()).updateChildren(toUpdate.toMap());
        return true;
    }

    @Override
    public boolean isDBUpdated() {

        return localDB.isDBUpdated();
    }


    public void login(final String email,final String password,final CallBack<CPUser> callBack) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                DebugHelper.Log("Login status:" +  task.isSuccessful());

                if (callBack != null)
                    if (!task.isSuccessful()) {

                        HelperClass.runInMain(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailed(DataStatus.Failed, "Invalid username or password");
                            }
                        });
                    } else {

                        HelperClass.runInMain(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(null);
                            }
                        });
                    }

            }
        });
    }

    public void signUp(CPUser user, final CallBack<CPUser> callBack)
    {
        if(user.getUserEmail().isEmpty())
            callBack.onFailed(DataStatus.InvalidArgumment,"Invalid email");

        mAuth.createUserWithEmailAndPassword(user.getUserEmail(),user.getUserPwdHash()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    callBack.onSuccess(null);
                }else
                    callBack.onFailed(DataStatus.Failed,"Invalid user o password");

                // ...
            }
        });
    }



    public void signOut()
    {
        DebugHelper.Log("Signed out");
        mAuth.signOut();
    }

}

