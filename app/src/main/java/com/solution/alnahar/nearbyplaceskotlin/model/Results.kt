package com.solution.alnahar.nearbyplaceskotlin.model

class Results {

   // public Geometry geometry { get; set; }
    //public string icon { get; set; }
  //  public string id { get; set; }
   // public string name { get; set; }
   // public OpeningHours opening_hours { get; set; }
   // public List<Photo> photos { get; set; }
   // public string place_id { get; set; }
    //public double rating { get; set; }
    //public string reference { get; set; }
   // public string scope { get; set; }
   // public List<string> types { get; set; }
   // public string vicinity { get; set; }


    var geometry:Geometry?=null
    var icon:String?=null
    var  name:String?=null
    var photos:ArrayList<Photo>?=null
    var id:String?=null
    var place_id:String?=null
    var price_level:Int=0;
    var rating:Double=0.0
    var reference:String?=null
    var scope:String?=null
    var types:ArrayList<String>?=null
    var vicinity:String?=null
    var opening_hours:OpeningHours?=null


    //// detail here


    //    public List<AddressComponent> address_components { get; set; }
//    public string adr_address { get; set; }
//    public string formatted_address { get; set; }
//    public string formatted_phone_number { get; set; }
//    public Geometry geometry { get; set; }
//    public string icon { get; set; }
//    public string id { get; set; }
//    public string international_phone_number { get; set; }
//    public string name { get; set; }
//    public OpeningHours opening_hours { get; set; }
//    public List<Photo> photos { get; set; }
//    public string place_id { get; set; }
//    public double rating { get; set; }
//    public string reference { get; set; }
//    public List<Review> reviews { get; set; }
//    public string scope { get; set; }
//    public List<string> types { get; set; }
//    public string url { get; set; }
//    public int utc_offset { get; set; }
//    public string vicinity { get; set; }
//    public string website { get; set; }

    var address_components:ArrayList<AddressComponent>?=null
    var adr_address:String?=null
    var formatted_address:String?=null
    var formatted_phone_number:String?=null
    var international_phone_number:String?=null
    var reviews:ArrayList<Review>?=null
    var url:String?=null
    var utc_offset:Int?=0
    var website:String?=null














}