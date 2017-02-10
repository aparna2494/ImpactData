package com.cisco.bccsp.db.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.cisco.bccsp.db.util.DBUtilCiscoOpsMongo;
import com.cisco.bccsp.model.Edge;
import com.cisco.bccsp.model.EdgeModel;
import com.cisco.bccsp.model.Flow;
import com.cisco.bccsp.model.KPIModel;
import com.cisco.bccsp.model.Node;
import com.cisco.bccsp.model.NodeModel;
import com.cisco.bccsp.model.PositionModel;
import com.cisco.bccsp.model.ThresholdModel;
import com.mongodb.*;
import com.mongodb.client.*;

public class FlowDetailsDAO {
	private static Logger logger = Logger.getLogger(FlowDetailsDAO.class.getName());
	static Map<String, String> successmap= new HashMap<String, String>();
	public static Map<String, String> saveflowdetails(Flow flow,String collection_name) {
		
		MongoCollection coll=null;
		try
		{
			
		  coll = DBUtilCiscoOpsMongo.getCollectionByName(collection_name);
			
			BasicDBList nodes_lst=new BasicDBList();
			List<Node> node_model=flow.getNode();
			
			Iterator itr_nodes=node_model.iterator();
			while(itr_nodes.hasNext())
			{
				
				BasicDBObject nodes=new BasicDBObject();
				Node node=(Node) itr_nodes.next();
				logger.info(node.getLocked());
				
				nodes.append("group",node.getGroup());
				nodes.append("classes", node.getClasses());
				nodes.append("grabbable", node.getGrabbable());
				nodes.append("locked", node.getLocked());
				nodes.append("node_fixed",true);
				nodes.append("removed", node.getRemoved());
				nodes.append("selectable", node.getSelectable());
				nodes.append("selected", node.getSelected());
				PositionModel position=node.getPosition();
				BasicDBObject position_doc=new BasicDBObject().append("x", position.getX()).append("y", position.getY());
				nodes.append("position",position_doc);
				NodeModel node_data=node.getData();
				BasicDBObject data_doc=new BasicDBObject();
				data_doc.append("component_name", node_data.getComponent_name());
				data_doc.append("component_code", node_data.getComponent_code());
				
				data_doc.append("id", node_data.getId());
				data_doc.append("masterid",node_data.getMasterid());
				logger.info(node_data.getTrack_id());
				data_doc.append("track_id", node_data.getTrack_id());
				data_doc.append("track_name",node_data.getTrack_name());
				data_doc.append("weight",node_data.getWeight());
				List<KPIModel> kpi_model_lst=node_data.getKpi_data();
				BasicDBList kpi_model_dblst=new BasicDBList();
				Iterator<KPIModel> itr_kpi_model_lst=kpi_model_lst.iterator();
				while(itr_kpi_model_lst.hasNext())
				{
					KPIModel kpimodel=itr_kpi_model_lst.next();
					BasicDBObject kpimodel_db=new BasicDBObject();
					kpimodel_db.append("database", kpimodel.getDatabase());
					kpimodel_db.append("kpi_id", kpimodel.getKpi_id());
					kpimodel_db.append("kpi_name", kpimodel.getKpi_name());
					kpimodel_db.append("kpi_status", kpimodel.getKpi_status());
					kpimodel_db.append("query", kpimodel.getQuery());
					kpimodel_db.append("skms_id", kpimodel.getSkms_id());
					ThresholdModel threshold=kpimodel.getThreshold();
					BasicDBObject threshold_doc=new BasicDBObject().append("Critical", threshold.getCritical()).append("High", threshold.getHigh()).append("Medium", threshold.getMedium());
					kpimodel_db.append("threshold", threshold_doc);
					kpi_model_dblst.add(kpimodel_db);
				}
				data_doc.append("kpi_data", kpi_model_dblst);
				nodes.append("data", data_doc);
				nodes_lst.add(nodes);
			}
			List<EdgeModel>edge_lst=flow.getEdge();
			BasicDBList edge_lst_db=new BasicDBList();
			Iterator<EdgeModel>edge_itr= edge_lst.iterator();
			while(edge_itr.hasNext())
			{
				EdgeModel edge=edge_itr.next();
				BasicDBObject edge_db=new BasicDBObject();
				edge_db.append("edge_fixed", true);
				edge_db.append("id", edge.getData().getId());
				edge_db.append("source",edge.getData().getSource());
				edge_db.append("target", edge.getData().getTarget());
				edge_lst_db.add(new BasicDBObject().append("data", edge_db));
			}
			
			coll.drop();
			coll.insertOne(new Document().append("nodes", nodes_lst).append("edges", edge_lst_db));
			if(collection_name.equalsIgnoreCase("bp_flow_neo_data"))
			{
				MongoCollection coll_stage=DBUtilCiscoOpsMongo.getCollectionByName("bp_flow_neo_data_stage");
				coll_stage.drop();
				coll_stage.insertOne(new Document().append("nodes", nodes_lst).append("edges", edge_lst_db));
				
			}
		}
		catch(Exception e)
		{
			successmap.put("result", "failed");
			logger.error("error"+e);
		}
		return successmap;
		
	}

}
