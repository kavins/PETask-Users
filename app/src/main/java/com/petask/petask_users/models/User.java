package com.petask.petask_users.models;

import com.google.gson.annotations.SerializedName;

/**
 * POJO class which declares the detailed data of an individual User
 * It contains data like id, first name, last name and image
 */
public class User {

    private long id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * Method which combines user's first and last name and returns the full name
     * @return full name of user
     */
    public String getName() {
        return getFirstName() + " " + getLastName();
    }
}
