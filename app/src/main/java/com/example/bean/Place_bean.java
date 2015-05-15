package com.example.bean;

import java.io.Serializable;

/**
 * Created by rd on 15/05/2015.
 */
public class Place_bean implements Serializable
{
        private String name;
        private String category;
        private String vicinity;
        private String open;
        private String str_lat;
        private String str_long;
        private String id;
        private String photo_ref;
        private String icon_url;
        public Place_bean() {
            this.name = "";
            this.vicinity = "";
            this.open = "";
            this.setCategory("");
            str_lat="";
            str_long="";
            id="";
            photo_ref="";
        }
        public void setLatitude(String latitude)
        {
            str_lat=latitude;
        }
        public String getLatitude()
        {
            return str_lat;
        }
        public void setLongitude(String lng)
    {
        str_long=lng;
    }
        public String getLongitude()
    {
        return str_long;
    }
        public void set_id(String id)
    {
        this.id=id;
    }
        public String getid()
    {
        return id;
    }
        public void setPhoto_ref(String photo_ref)
    {
        this.photo_ref=photo_ref;
    }
        public String getPhoto_ref()
    {
        if(photo_ref==null)
        {
            return  "NA";
        }
        else {
            return photo_ref;
        }
    }
        public void setIcon(String iconurl)
        {
            icon_url=iconurl;
        }
        public String getIcon_url()
    {
        return icon_url;
    }
        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setvicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public String getvicinity() {
            return vicinity;
        }

        public void setOpenNow(String open) {
            this.open = open;
        }

        public String getOpenNow() {
            return open;
        }

}
