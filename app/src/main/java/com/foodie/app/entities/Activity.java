package com.foodie.app.entities;


import android.content.ContentValues;
import android.net.Uri;

import com.foodie.app.backend.AppContract;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Daniel on 12/1/2016.
 */

public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static int activityID = 0;

    private int _ID;

    private String activityName;

    private String activityDescription;

    private double activityCost;

    private double activityRating;

    private int businessId;

    private byte[] activityImages; //for future use

    private String feature;

    public Activity() {
    }

    public Activity(String activityName, String activityDescription, double activityCost, double activityRating, int businessId, byte[] activityImages, String feature) throws Exception {
        set_ID(activityID+1);
        activityID++;
        setActivityName(activityName);
        setActivityDescription(activityDescription);
        setActivityCost(activityCost);
        setActivityRating(activityRating);
        setBusinessId(businessId);
        setActivityImages(activityImages);
        setFeature(feature);
    }

    public Activity(int id,String activityName, String activityDescription, double activityCost, double activityRating, int businessId, byte[] activityImages, String feature) throws Exception {
        set_ID(id);
        setActivityName(activityName);
        setActivityDescription(activityDescription);
        setActivityCost(activityCost);
        setActivityRating(activityRating);
        setBusinessId(businessId);
        setActivityImages(activityImages);
        setFeature(feature);
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) throws Exception {
        Pattern pattern =
                Pattern.compile("^(([a-zA-Z]{2,15}){1}(\\s([a-zA-Z]{2,15}))*)$");
        Matcher matcher =
                pattern.matcher(activityName);
        if (matcher.find())
            this.activityName = activityName;
        else
            throw new Exception("Activity name must contains at least 2 characters!");
    }

//    public Calendar getActivityDate() {
//        return activityDate;
//    }
//
//    public void setActivityDate(Calendar activityDate) {
//        this.activityDate = activityDate;
//    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public double getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(double activityCost) throws Exception {
        if (activityCost < 0)
            throw new Exception("Cost must be a positive number");
        this.activityCost = activityCost;
    }

    public double getActivityRating() {
        return activityRating;
    }

    public void setActivityRating(double activityRating) throws Exception {
        if (activityRating > 5 || activityRating < 1)
            throw new Exception("Rating must be between 1 to 5");
        this.activityRating = activityRating;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        //if(!isExist(businessId, TYPE.BUSINESS)
        //throw new Exception("Business does not exist");
        this.businessId = businessId;
    }

    public byte[] getActivityImages() {
        return activityImages;
    }

    public void setActivityImages(byte[] activityImages) {
        this.activityImages = activityImages;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public ContentValues toContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(AppContract.Activity.ACTIVITY_ID, this.get_ID());
        contentValues.put(AppContract.Activity.ACTIVITY_NAME, this.getActivityName());
        contentValues.put(AppContract.Activity.ACTIVITY_DESCRIPTION, this.getActivityDescription());
        contentValues.put(AppContract.Activity.ACTIVITY_COST, this.getActivityCost());
        contentValues.put(AppContract.Activity.ACTIVITY_BUSINESS_ID, this.getBusinessId());
        contentValues.put(AppContract.Activity.ACTIVITY_IMAGE, this.getActivityImages());
        contentValues.put(AppContract.Activity.ACTIVITY_RATING, this.getActivityRating());
        contentValues.put(AppContract.Activity.ACTIVITY_FEATURE, this.getFeature());


        return contentValues;
    }


    public static Uri getURI() {
        return Uri.parse("content://com.foodie.app/Activity");
    }
}
