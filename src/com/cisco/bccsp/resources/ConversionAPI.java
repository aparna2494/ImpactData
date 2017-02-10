package com.cisco.bccsp.resources;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.bson.types.BasicBSONList;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class ConversionAPI {
	private static Logger logger = Logger.getLogger(ConversionAPI.class);

	/**
	 * 
	 */
	public ConversionAPI() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String convertDBList2JSON(BasicDBList basicDBList) {

		BasicBSONList o = (BasicBSONList) basicDBList;

		return o.toString();

	}

	public static String convertDBList2JSONStatic(BasicDBList basicDBList) {

		BasicBSONList o = (BasicBSONList) basicDBList;

		return o.toString();

	}

	
	public static BasicDBList convertHashMap2JSON(
			HashMap<String, HashMap<String, BasicDBObject>> data_map) {

		BasicDBList outer_list = new BasicDBList();

		BasicDBObject obj = null;
		BasicDBList inner_list = null;

		for (String i : data_map.keySet()) {

			obj = new BasicDBObject("MappedBox", i);

			logger.debug("Box : " + i);

			inner_list = new BasicDBList();

			for (String j : data_map.get(i).keySet()) {

				logger.debug("CR _id " + j);
				logger.debug("CR doc " + data_map.get(i).get(j));

				inner_list.add(new BasicDBObject("CR Document", data_map.get(i)
						.get(j)));

			}

			obj.append("Total", inner_list.size());
			obj.append("CR List", inner_list);
			outer_list.add(obj);
		}

		return outer_list;
	}

	

}
