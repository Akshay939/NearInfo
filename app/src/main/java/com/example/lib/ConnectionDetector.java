package com.example.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector 
{
	Context this_context;
   public ConnectionDetector(Context context) 
   {
	// TODO Auto-generated constructor stub
	   this_context=context;
   }// end of this context;
   public boolean isConnectingToInternet()
   {
       ConnectivityManager connectivity = (ConnectivityManager)this_context.getSystemService(Context.CONNECTIVITY_SERVICE);
         if (connectivity != null)
         {
             NetworkInfo[] info = connectivity.getAllNetworkInfo();
             if (info != null)
                 for (int i = 0; i < info.length; i++)
                     if (info[i].getState() == NetworkInfo.State.CONNECTED)
                     {
                         return true;
                     }

         }
         return false;
   }
}// end of connection detector
