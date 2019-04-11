package com.pos.petra.app.Device;

import com.pax.dal.IMag;
import com.pax.dal.entity.TrackData;
import com.pax.dal.exceptions.MagDevException;

public class MagTester extends BaseTester {








    public static void open(IMag iMag) {
        try {
            iMag.open();
           // logTrue("open");
        } catch (MagDevException e) {
            e.printStackTrace();
         //   logErr("open", e.toString());
        }
    }

    public static void close(IMag iMag) {
        try {
            iMag.close();
          //  logTrue("close");
        } catch (MagDevException e) {
            e.printStackTrace();
         //   logErr("close", e.toString());
        }
    }

    // Reset magnetic stripe card reader, and clear buffer of magnetic stripe card.
    public static void reset(IMag iMag) {
        try {
            iMag.reset();
          //  logTrue("reSet");
        } catch (MagDevException e) {
            e.printStackTrace();
          //  logErr("reSet", e.toString());
        }
    }

    // Check whether a card is swiped
    public static boolean isSwiped(IMag iMag) {
        boolean b = false;
        try {
            b = iMag.isSwiped();
            // logTrue("isSwiped");
        } catch (MagDevException e) {
            e.printStackTrace();
          //  logErr("isSwiped", e.toString());
        }
        return b;
    }

    public static TrackData read(IMag iMag) {
        try {
            TrackData trackData = iMag.read();
          //  logTrue("read");
            return trackData;
        } catch (MagDevException e) {
            e.printStackTrace();
         //   logErr("read", e.toString());
            return null;
        }
    }

}
