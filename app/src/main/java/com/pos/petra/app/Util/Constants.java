package com.pos.petra.app.Util;


import java.util.ArrayList;

/**
 * Created by Welcome on 17-06-2017.
 */

public class Constants {

   //public  static final String baseUrl = "https://freedomchoiceglobal.com/api/demo/";
    //http://tramadolcapsules.com/petraApp/transactions.php?load=transactions&locationid=5
 //   public  static final String baseUrl = "https://tramadolcapsules.com/petraApp/";
     public  static final String baseUrl = "http://petra.briskwizards.tech/";

    public  static final String transactionsUrl = baseUrl + "transactions.php";
    public  static final String adv_transactionsUrl = baseUrl + "petra-transactions.php";
    public  static final String reportUrl = baseUrl + "report.php";
    public  static final String signupUrl = baseUrl + "signup.php";
    public  static final String loginUrl = baseUrl + "login.php";
    public  static final String merchantDataUrl = baseUrl + "merchantdata.php";
    public  static final String cashierDataUrl = baseUrl + "cashierdata.php";
    public  static final String customerUrl = baseUrl + "customerdata.php?customerid=";
    public  static final String searchUrl = baseUrl + "search.php";

    public static final String[] states = new String[] {"Alabama","Alaska", "American Samoa","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","District Of Columbia","Federated States Of Micronesia","Florida","Georgia","Guam","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Marshall Islands","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Northern Mariana Islands","Ohio","Oklahoma","Oregon","Palau","Pennsylvania","Puerto Rico","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virgin Islands","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
    public static final String PAYMENT_URL = "payanywhere://payment/";
    public static final int PAYMENT_REQUEST_CODE = 123;

    public static final String REFUND_URL = "payanywhere://refund/";
    public static final String PRODUCT_IMG_URL = "https://tramadolcapsules.com/petraApp/assets//products/";
    public static final int VOID_REQUEST_CODE = 456;
    public static final int REFUND_REQUEST_CODE = 457;

    public static final String VOID_URL = "payanywhere://void/";
    public static final String GUEST = "Guest";
    public static final String PAYMETHOD_CASH = "Cash";
    public static final String PAYMETHOD_CARD = "Card";
    public static final String PAYMETHOD_EXCHANGE = "Exchange";
    public static final String MERCHANT = "Merchant";
    public static final String CUSTOMER = "Customer";
    public static final int GETLIST = 1;
    public static final int ADDITEM = 2;
    public static final int UPDATEITEM = 3;
    public static final int DELETEITEM =4;
    public static final int GETLIST_1 = 5;
    public static final int GETLIST_2 = 6;
    public static final int GETLIST_3 = 7;
    public static final int ADDSUBITEM = 8;
    public static final int UPDATESUBITEM = 9;
    public static final int DELETESUBITEM =10;

    public static final int ADDPRODITEM = 11;
    public static final int UPDATEPRODITEM = 12;
    public static final int DELETEPRODITEM =13;

    public static final int DEFAULT_TIMEOUT_MS = 10000;
    public static final int DEFAULT_MAX_RETRIES = 10;
    public static final float DEFAULT_BACKOFF_MULT = 1f;
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String VIEW = "view";
    public static final String VIEW_All= "view_all";

    public static final String VIEW_SUB = "view_sub";

    public static final String DELETE = "delete";


    public static String[] countrylist = new String[]{
            "Select Country",
            "Afghanistan", "Albania", "Algeria",
            "American Samoa",
            "Andorra",
            "Angola",
            "Anguilla",
            "Antigua and Barbuda",
            "Argentina",
            "Armenia",
            "Aruba",
            "Australia",
            "Austria",
            "Azerbaijan",
            "The Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bermuda",
            "Bhutan",
            "Bolivia",
            "Bosnia and Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina Faso",
            "Burundi",
            "Cambodia",
            "Cameroon",
            "Canada",
            "Cape Verde",
            "Cayman Islands",
            "Central African Republic",
            "Chad",
            "Chile",
            "People 's Republic of China",
            "Republic of China",
            "Christmas Island",
            "Cocos(Keeling) Islands",
            "Colombia",
            "Comoros",
            "Congo",
            "Cook Islands",
            "Costa Rica",
            "Cote d'Ivoire",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czech Republic",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Falkland Islands",
            "Faroe Islands",
            "Fiji",
            "Finland",
            "France",
            "French Polynesia",
            "Gabon",
            "The Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Gibraltar",
            "Greece",
            "Greenland",
            "Grenada",
            "Guadeloupe",
            "Guam",
            "Guatemala",
            "Guernsey",
            "Guinea",
            "Guinea - Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hong Kong",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland",
            "Israel",
            "Italy",
            "Jamaica",
            "Japan",
            "Jersey",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "North Korea",
            "South Korea",
            "Kosovo",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macau",
            "Macedonia",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Martinique",
            "Mauritania",
            "Mauritius",
            "Mayotte",
            "Mexico",
            "Micronesa",
            "Moldova",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Montserrat",
            "Morocco",
            "Mozambique",
            "Myanmar",
            "Nagorno - Karabakh",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "Netherlands Antilles",
            "New Caledonia",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Niue",
            "Norfolk Island",
            "Turkish Republic of Northern Cyprus",
            "Northern Mariana",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Palestine",
            "Panama",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Pitcairn Islands",
            "Poland",
            "Portugal",
            "Puerto Rico",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "Saint Barthelemy",
            "Saint Helena",
            "Saint Kitts and Nevis",
            "Saint Lucia",
            "Saint Martin",
            "Saint Pierre and Miquelon",
            "Saint Vincent and the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome and Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "Somaliland",
            "South Africa",
            "South Ossetia",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Svalbard",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syria",
            "Taiwan",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Timor - Leste",
            "Togo",
            "Tokelau",
            "Tonga",
            "Transnistria Pridnestrovie",
            "Trinidad and Tobago",
            "Tristan da Cunha",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Turks and Caicos Islands",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Vatican City",
            "Venezuela",
            "Vietnam",
            "British Virgin Islands",
            "Isle of Man",
            "US Virgin Islands",
            "Wallis and Futuna",
            "Western Sahara",
            "Yemen",
            "Zambia",
            "Zimbabwe"};
    public static String Payanywhere_Install_Url = "https://play.google.com/store/apps/details?id=com.nabancard.payanywheresdk&hl=en_US";
    public static String Payanywhere_package = "com.nabancard.payanywheresdk";

    public static String CategoryAPI = baseUrl +"category.php";
    public static String ProductAPI= baseUrl +"products.php";
    public static String ModifierAPI= baseUrl +"modifier.php";
    public static String DsicountAPI= baseUrl +"discounts.php";

    public static String petratransactions= baseUrl +"petra-transactions.php";
    public static String charity= baseUrl +"charity.php";


    public static ArrayList<String> discount() {
        ArrayList<String> str = new ArrayList<>();
        for (int i=10;i<51;i++){
            str.add(""+i);
        }

        return str;
    }


    //
}

