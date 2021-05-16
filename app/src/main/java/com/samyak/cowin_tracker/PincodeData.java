package com.samyak.cowin_tracker;

public class PincodeData {

    private final String pincode;
    private String slot_tracking;

    public PincodeData(String pincode, String slot_tracking) {
        this.pincode = pincode;
        if (slot_tracking.equals("is_18_plus")) {
            this.slot_tracking = "18+";
        } else if (slot_tracking.equals("is_45_plus")) {
            this.slot_tracking = "45+";
        } else if (slot_tracking.equals("is_all")) {
            this.slot_tracking = "All";
        }
    }

    public String getPincode() {
        return pincode;
    }

    public String getSlot_tracking() {
        return slot_tracking;
    }

}
