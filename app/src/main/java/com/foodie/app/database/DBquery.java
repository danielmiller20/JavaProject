package com.foodie.app.database;

/**
 * Created by David on 25/12/2016.
 */

/**
 * This class will help to order the query in AsyncData class
 * The Class is the same as the content provider query but divided by objects
 */
public class DBquery {


    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
    private String[] login;

    public String[] getProjection() {
        return projection;
    }

    public void setProjection(String[] projection) {
        this.projection = projection;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String[] getLogin() {
        return login;
    }

    public void setLogin(String[] login) {
        this.login = login;
    }

    public DBquery() {
    }

    public DBquery(String[] projection, String selection,
                   String[] selectionArgs, String sortOrder) {

        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;
    }

    public DBquery(String[] projection, String[] selectionArgs) {
        this.projection = projection;
        this.selectionArgs = selectionArgs;
    }

    public DBquery(String[] login) {
        this.login = login;
    }
}
