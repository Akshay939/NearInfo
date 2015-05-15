package com.example.lib;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Handle_special_char
{

	public Handle_special_char()
	{
		// TODO Auto-generated constructor stub
	}

	public String remove_special_character(String str_special)
	{
		str_special = str_special.replace("&", "#and;");
		str_special = str_special.replace("<", "#lte;");
		str_special = str_special.replace(">", "#gte;");
		str_special = str_special.replace("'", "#apose;");
		str_special = str_special.replace(",", "#acom;");
		str_special = str_special.replace("\"", "#quote;");
	    	
	    return str_special;
	}// end of remove special character 
	public String add_special_character(String content)
	{

			content = content.replace("#and;", "&");
			content = content.replace("#lte;", "<");
		    content = content.replace("#gte;", ">");
			content = content.replace("#apose;", "'");
		    content = content.replace("#acom;", ",");
		    content = content.replace("#quote;", "\"");
			return content;
	      
	}
	
}// end of type conversion
