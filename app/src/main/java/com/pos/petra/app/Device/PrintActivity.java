package com.pos.petra.app.Device;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.pax.dal.IDAL;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pos.petra.app.Payment.AdvPaymentActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import static com.pax.dal.entity.EFontTypeAscii.FONT_16_24;
import static com.pax.dal.entity.EFontTypeAscii.FONT_8_16;
import static com.pax.dal.entity.EFontTypeAscii.FONT_8_32;
import static com.pax.dal.entity.EFontTypeExtCode.FONT_16_16;


public class PrintActivity  {

    String str ="What is Lorem Ipsum?\n" +
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public  static  IDAL idal;
    private final SharedPreferences common_mypref;
    private Thread pthread;
    private TextView textView;
    public static IccDectedThread iccDectedThread;
 //   static MagReadThread magReadThread;
    private Context ctx;
    private String strrrrrr="";

    public PrintActivity(Activity ctx) {
       this.ctx=ctx;
        try {
            idal= NeptuneLiteUser.getInstance().getDal(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == idal) {
            Toast.makeText(ctx, "error occurred,DAL is null.", Toast.LENGTH_LONG).show();
        }

        common_mypref = ctx.getSharedPreferences(
                "user", 0);

    }

    public void icReader(View view) {

        if (iccDectedThread == null) {
            iccDectedThread = new IccDectedThread();
            iccDectedThread.start();
        }else{
            iccDectedThread.start();
        }
    }


   /* public void magreader(View view) {
        textView.setText("swipe a Card please");
        if (magReadThread == null) {
            magReadThread = new MagReadThread();
            MagTester.getInstance().open();
            MagTester.getInstance().reset();
            magReadThread.start();
        }
        else{
            MagTester.getInstance().open();
            MagTester.getInstance().reset();
            magReadThread.start();
        }
    }*/

    public void printer(final ArrayList<String> voucharString, final ArrayList<String> voucharProdcutString) {
        (pthread= new Thread(new Runnable() {
            public void run() {
                printVoucher(voucharString,voucharProdcutString);//Done


            }
        })).start();
    }




    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    textView.setText(msg.obj.toString());
                    break;

                default:
                    break;
            }
        };
    };




    public class IccDectedThread extends Thread {
        private boolean b;

        @Override
        public void run() {
            super.run();
            String resString = "";
            IccTester.getInstance().light(true);
            while (!Thread.interrupted()) {
                b = IccTester.getInstance().detect((byte) 0);
                if (b) {
                    resString = "card Detected";
                    byte[] res = IccTester.getInstance().init((byte) 0);
                    if (res == null) {
                        Log.i("Test", "init ic card,but no response");
                        return;
                    }
                    resString += ("\ninit response：" + Convert.getInstance().bcdToStr(res));
                    IccTester.getInstance().autoResp((byte) 0, true);// 设置iccIsoCommand函数是否自动发送GET RESPONSE指令。

                    IApdu apdu = Packer.getInstance().getApdu();
                    IApdu.IApduReq apduReq = apdu.createReq((byte) 0x00, (byte) 0xa4, (byte) 0x04, (byte) 0x00,
                            "1PAY.SYS.DDF01".getBytes(), (byte) 0);
                    byte[] req = apduReq.pack();
                     // byte[] req =
                     //getObject.getIGLs().getConvert().strToBcd("00A404000E315041592E5359532E444446303100",
                     //Convert.EPaddingPosition.PADDING_LEFT);
                     // new TestLog().logTrue("apduReq"+getObject.getIGLs().getConvert().bcdToStr(apdu.pack(apduReq)));
                    byte[] isoRes = IccTester.getInstance().isoCommand((byte) 0, req);

                    if (isoRes != null) {
                        IApdu.IApduResp apduResp = apdu.unpack(isoRes);
                        String isoStr = null;
                        try {
                            isoStr = "isocommand response:" + " Data:" + new String(apduResp.getData(), "iso8859-1")
                                    + " Status:" + apduResp.getStatus() + " StatusString:" + apduResp.getStatusString();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        resString += ("\n" + isoStr);
                    }

                    IccTester.getInstance().close((byte) 0);
                    IccTester.getInstance().light(false);
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = resString;
                    handler.sendMessage(message);
                    SystemClock.sleep(2000);

                    break;

                }
                else
                    {
                    resString ="Card not detected";
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = resString;
                    handler.sendMessage(message);
                    SystemClock.sleep(2000);
                }
            }
        }

    }





/*
    @Override
    public void onDestroy() {
        if (iccDectedThread != null) {
            iccDectedThread.interrupt();
            iccDectedThread = null;
            IccTester.getInstance().light(false);
        }

        if (magReadThread != null) {
            MagTester.getInstance().close();
            magReadThread.interrupt();
            magReadThread = null;
        }
        super.onDestroy();
    }*/



     /// Create Vouceher Standard for German
    private void printVoucher(final ArrayList<String> voucharString, final ArrayList<String> voucharProdcutString) {
        try {

            bigText("PETRA");
            normalText( "" );
            normalText( "" );
            try {
                InputStream inputStream = ctx.getAssets().open("app_icon.png");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                printBitmap(bitmap);
                bitmap.recycle();
            }catch (IOException e){
                e.printStackTrace();
            }
            normalText( "FreedomChoiceGlobal.com" );
            normalText( "" );
            normalText( "" );

          /*  Bitmap bimp=getBarcode(json_voucherData.getString("BarcodeData"));
            if(null!=bimp){
                printBitmap(bimp);
            }*/
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy");
            String formattedDate = df.format(c);

            normalText("Transaction No.: "+voucharString.get(0));
            normalText("Date: " + formattedDate);
            normalText("________________________________________________");
            boldText("Item                   Qty    Rate        Amount");
            boldText("FOR OFFICIAL USE AT EXPORT FROM EU              ");
            normalText("________________________________________________");

            for(int i= 0; i< voucharProdcutString.size();i++){
                normalText(voucharProdcutString.get(i));
            }
            normalText("________________________________________________");

            for(int i= 1; i< voucharString.size()-1;i++){
                normalText(voucharString.get(i));
            }
            normalText("________________________________________________");
            boldText(voucharString.get(voucharString.size()-1));
            normalText("");
            normalText("");

            normalText(" We hope to see you again soon!");





/*
            //Der Verkauf erfolgt im Namen und auf Rechnung der
                                //Lorem Ipsum is simply dummy text of the printing

            normalText("");
            normalText("Date: " + json_voucherData.getString("VoucherDate")+ "Time: "+
                    json_voucherData.getJSONObject("Invoices").getString("TransactionTime"));
            normalText("------------------------------------------");

            normalText("");

            boldText("NO REFUND WITHOUT ORIGINAL INVOICE");
            //normalText("No cash refund in store");

            normalText("");

            normalText("Retailer signature: ______________________");
            normalText("");
            normalText("Form no.: "+json_voucherData.getString("VoucherNumber"));
            // normalText("Invoice no.: "+json_invoiceData.getString("Number"));
            normalText("");
            bigText("Customer details and declaration");

            JSONObject json_name = new JSONObject();
            json_name = json_voucherData.getJSONObject("CustomerData").getJSONObject("Name");
            if (json_name.getString("FirstName")!=null && json_name.getString("LastName")!=null) {
                normalText("Full Name: " + json_name.getString("FirstName")+" "
                        + json_name.getString("LastName"));
            }
            else{
                normalText("Full Name: ______________________");
            }
            normalText("");
            if(json_voucherData.getJSONObject("CustomerData").getJSONObject("Address").getString("AddressLineOne")!=null) {
                normalText("Address: " + json_voucherData.getJSONObject("CustomerData").getJSONObject("Address")
                        .getString("AddressLineOne"));
            }
            else{
                normalText("Address: ________________________");
                normalText("_________________________________");
                normalText("_________________________________");
            }
            normalText("");
            if (json_voucherData.getJSONObject("CustomerData").getJSONObject("Address").getString("AddressLineTwo")!=null) {
                normalText("City: " + json_voucherData.getJSONObject("CustomerData").getJSONObject("Address")
                        .getString("AddressLineTwo"));
            }
            else {
                normalText("City: ___________________________");
            }
            normalText("");
            if (json_voucherData.getJSONObject("CustomerData").getJSONObject("Address").getString("AddressLineFour")!=null) {
                normalText("Postcode: " + json_voucherData.getJSONObject("CustomerData").getJSONObject("Address")
                        .getString("AddressLineFour"));
            }
            else {
                normalText("Postcode: ________________________");
            }
            normalText("");
            if (json_voucherData.getJSONObject("CustomerData").getString("CountryOfOrigin")!=null){
                normalText("Country: "+ json_voucherData.getJSONObject("CustomerData").getString("CountryOfOrigin"));
            }
            else {
                normalText("Country: _________________________");
            }
            normalText("");
            if (json_voucherData.getJSONObject("CustomerData").getString("PassportNumber")!=null) {
                normalText("Passport: " + json_voucherData.getJSONObject("CustomerData").getString("PassportNumber"));
            }
            else {
                normalText("Passport: ________________________");
            }
            normalText("");
            if (json_voucherData.getJSONObject("CustomerData").getString("Email")!=null) {
                normalText("Email: " + json_voucherData.getJSONObject("CustomerData").getString("Email"));
            }
            else {
                normalText("Email: ___________________________");
            }
            normalText("");
            normalText("1. Refund to your mobile Premier Pass [   ]  \n" +
                    "Print your 15 digit Premier Pass number\n" +
                    "\n" +
                    "_ _ _ _  /  _ _ _ _  /  _ _ _ _  / _ _ _\n" +
                    "\n" +
                    "2. Credit card refund [   ]\n" +
                    "PRINT CREDIT CARD NUMBER BELOW\n" +
                    "_________  _________  _________  _________\n" +
                    "\n" +
                    "3. Cash refund [   ]\n");

          
            normalText("");
          
            try {
                InputStream inputStream = ctx.getAssets().open("alipay_icon.jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                printBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
            normalText("");
            smallText("Der Verkauf erfolgt im Namen und auf Rechnung der Premier Tax Free GmbH. " +
                    "I understand that the credit card nominated to credit the VAT is the same card used" +
                    " for the payment of the goods described above. Delete if not applicable. This form " +
                    " and all rights  deriving from it is Premier Tax Free’s sole property and can only be" +
                    " refunded by PTF or authorised partner companies. By returning this form, I agree to PTF’s " +
                    "terms and  conditions. Please visit www.premiertaxfree.com for full terms and conditions." +
                    " By ticking below I confirm that I would like PTF to contact me with information about " +
                    "goods and services which you feel may be of interest to me. [  ] I declare that I am not " +
                    "a resident in the EU and that I am exporting all of the goods (listed) on this form" +
                    "from the EU. PTF may use your information for  research purposes to help improve the quality of " +
                    "our service. PTF may be required to share information with our subsidiaries, government" +
                    " departments and/or tax & customs authorities for the purpose of fraud prevention, legal & regulator requirements. PTF may" +
                    " need to transfer your information outside the EEA to provide our service. Such processing " +
                    "will be in compliance with Irish Data Protection legislation. You have the right to request a " +
                    "copy of information we hold about you(for which PTF may charge you a fee)and to have" +
                    " inaccuracies corrected.\n\n ");


           
            normalText("Customer signature: ______________________________\n\n\n");
          
            boldText("FOR OFFICIAL USE AT EXPORT FROM EU              ");
            normalText("");
            smallText("Die in der Originalrechnung bezeichneten " +
                    "Gegenstaende wurden mit Ausnahme der in " +
                            "Nr. ___ \n" +
                            "bezeichneten Gegenstaende zur Ausfuhr" +
                            "abgefertigt. Die Angaben ueber den Namen " +
                            "und die Anschrift des Abnehmers stimmen " +
                            "mit den Eintragungen in dem vorgelegten" +
                            "Reisepass oder sonstigen Grenzueber-" +
                            "trittspapieren ueberein." +
                            "The products specified on the original " +
                            "invoice, except those listed under " +
                            "no. ___ \n" +
                            "have been cleared for export. Identity and" +
                            "address of foreign buyer are identical to " +
                            "those on passport or travel documents." +
                            "\n" +
                            "Kann die Abfertigung zur Ausfuhr fuer " +
                            "keinen Gegenstand bestaetigt werden," +
                            "erteilt die Grenzzollstelle auch keine " +
                            "Abnehmerbestaetigung(dies gilt auch fuer" +
                            "die Ersatzbestaetigung durch eine amtliche " +
                            "Stelle).");
          

            normalText("");
            normalText("");
            normalText("Bemerkung/remarks:                                ");
            normalText("");
            normalText("");
            normalText("Signature: _______________________________");
            normalText("(Export Officer)");
            normalText("");
            normalText("Zollamtliche Bestaetigung / Customs stamp:");
            normalText("");
            normalText("");
            normalText("");
            normalText("");
            normalText("");
            normalText("");
            normalText("___________________________________________       ");
            normalText("In Ausnahmefaellen:\n" +
                    "Bestaetigung einer amtlichen Stelle der\n" +
                    "Bundesrepublik Deutschland im \n" +
                    "Bestimmungsland, unter Einhaltung der \n" +
                    "Dreimonatsfrist + Unterschrift.\n" +
                    "www.premiertaxfree.com");

            normalText("");
            normalText("");
            halfCut();///////////////////////////////////////////////
            bigText("CUSTOMER TEAROFF COPY");
            normalText("");
            try {
                InputStream inputStream = ctx.getAssets().open("launcher_icon.png");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                printBitmap(bitmap);
                bitmap.recycle();
            }catch (IOException e){
                e.printStackTrace();
            }
            printBitmap(bimp);
            normalText(""       +json_voucherData.getString("VoucherNumber"));

            normalText("Date: " + json_voucherData.getString("VoucherDate")+ " "+
                    json_voucherData.getJSONObject("Invoices").getString("TransactionTime"));
            normalText("-------------------------------------------");*/
            boldText("");
            fullCut();
        }catch (Exception e){
            e.printStackTrace();
        }

        restart();
    }















    private Bitmap getBarcode(String str) {

        try {
            return  encodeAsBitmap(str, BarcodeFormat.CODE_128, 600, 150);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String smallText(String str) {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().fontSet((EFontTypeAscii) FONT_8_16,
                (EFontTypeExtCode) FONT_16_16);
        PrinterTester.getInstance().setGray(1);
        PrinterTester.getInstance().step(Integer.parseInt("0"));
        PrinterTester.getInstance().printStr(str, null);
        return PrinterTester.getInstance().start();

    }
    private String normalText(String str) {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().fontSet((EFontTypeAscii) FONT_8_32,
                (EFontTypeExtCode) FONT_16_16);
        PrinterTester.getInstance().setGray(1);
        PrinterTester.getInstance().step(Integer.parseInt("0"));
        PrinterTester.getInstance().printStr(str, null);
        return PrinterTester.getInstance().start();
    }

    private String boldText(String str) {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().fontSet((EFontTypeAscii) FONT_8_32,
                (EFontTypeExtCode) FONT_16_16);
        PrinterTester.getInstance().setGray(3);
        PrinterTester.getInstance().step(Integer.parseInt("0"));
        PrinterTester.getInstance().printStr(str, null);
        return PrinterTester.getInstance().start();
    }
    private String bigText(String str) {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().fontSet((EFontTypeAscii) FONT_16_24,
                (EFontTypeExtCode) FONT_16_16);
        PrinterTester.getInstance().setGray(3);
        PrinterTester.getInstance().step(Integer.parseInt("0"));
        PrinterTester.getInstance().printStr(str, null);
        return PrinterTester.getInstance().start();
    }



    private String smallBoldText(String str) {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().fontSet((EFontTypeAscii) FONT_8_16,
                (EFontTypeExtCode) FONT_16_16);
        PrinterTester.getInstance().setGray(3);
        PrinterTester.getInstance().step(Integer.parseInt("0"));
        PrinterTester.getInstance().printStr(str, null);
        return PrinterTester.getInstance().start();

    }

    private String halfCut() {
        normalText("");
        normalText("");
        normalText("");
        normalText("");
        normalText("");
        String res = PrinterTester.getInstance().cutPaper(1);
        normalText("");
        normalText("");

            return res;
    }
    private String fullCut() {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().fontSet((EFontTypeAscii) FONT_8_32,
                (EFontTypeExtCode) FONT_16_16);
        PrinterTester.getInstance().setGray(1);
        PrinterTester.getInstance().step(Integer.parseInt("150"));
        PrinterTester.getInstance().printStr(str, null);
        normalText("");
        normalText("\n\n\n            ----\n\n\n");
        normalText("");
        normalText("\n\n");
        normalText("");
        String res = PrinterTester.getInstance().cutPaper(0);
        normalText("");
        normalText("\n\n");
        normalText("");

        return res;

    }

    private String printBitmap(Bitmap bitmap) {
        PrinterTester.getInstance().init();
        PrinterTester.getInstance().step(Integer.parseInt("0"));
        PrinterTester.getInstance().printBitmap(bitmap);
        return PrinterTester.getInstance().start();
    }
    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private void restart() {
        ((AppCompatActivity)ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((AdvPaymentActivity)ctx).finishPaymentActivity();

            }
        });
    }
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }



}
