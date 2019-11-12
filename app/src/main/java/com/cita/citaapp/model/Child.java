package com.cita.citaapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Child   {
    private String fullName, gender, dateOfBirth;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(this.fullName);
//        parcel.writeString(this.gender);
//        parcel.writeString(this.dateOfBirth);
//    }

    public Child() {
    }

//    private Child(Parcel parcel) {
//        this.fullName = parcel.readString();
//        this.gender = parcel.readString();
//        this.dateOfBirth = parcel.readString();
//    }
//
//    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
//        @Override
//        public Child createFromParcel(Parcel source) {
//            return new Child(source);
//        }
//
//        @Override
//        public Child[] newArray(int size) {
//            return new Child[size];
//        }
//    };
}
