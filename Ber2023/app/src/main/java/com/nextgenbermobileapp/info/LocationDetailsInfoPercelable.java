package com.nextgenbermobileapp.info;

import android.os.Parcel;
import android.os.Parcelable;



public class LocationDetailsInfoPercelable implements Parcelable{

	private LocationInfo loc_info;
	public String Name;
	
    public LocationDetailsInfoPercelable(LocationInfo loc_info) {
        super();
        this.loc_info = loc_info;
        this.Name=Name;
    }
    
    public LocationDetailsInfoPercelable() {
        super();
        
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
    
    /*private LocationDetailsInfoPercelable(Parcel in) {
        laptop = new Laptop();
        laptop.setId(in.readInt());
        laptop.setBrand(in.readString());
        laptop.setPrice(in.readDouble());
        laptop.setImageBitmap((Bitmap) in.readParcelable(Bitmap.class
                .getClassLoader()));
    }
    
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
	
	
	public Laptop getLaptop() {
        return laptop;
    }
 
    
     * you can use hashCode() here.
     
    public int describeContents() {
        return 0;
    }
 
    
     * Actual object Serialization/flattening happens here. You need to
     * individually Parcel each property of your object.
     
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(laptop.getId());
        parcel.writeString(laptop.getBrand());
        parcel.writeDouble(laptop.getPrice());
        
        parcel.writeParcelable(laptop.getImageBitmap(),flags);
    }
 
    
     * Parcelable interface must also have a static field called CREATOR,
     * which is an object implementing the Parcelable.Creator interface.
     * Used to un-marshal or de-serialize object from Parcel.
     
    public static final Parcelable.Creator<LocationDetailsInfoPercelable> CREATOR =
            new Parcelable.Creator<LocationDetailsInfoPercelable>() {
        public LocationDetailsInfoPercelable createFromParcel(Parcel in) {
            return new LocationDetailsInfoPercelable(in);
        }
 
        public LocationDetailsInfoPercelable[] newArray(int size) {
            return new LocationDetailsInfoPercelable[size];
        }
    };
	*/
}
