package com.ssl.email.launch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.ssl.bean.FormBean;
import com.ssl.model.SendMailNotification;
import com.ssl.util.ErrorLogger;
import com.ssl.util.P8Connection;
import com.ssl.util.ReadProperty;


public class EmailNotificationAutoLanch
{
	
	private static final ErrorLogger logger = new ErrorLogger("EmailNotificationAutoLanch");
	 public void emailAutoLaunch()
	 {
		 System.out.println("~*~*~*~*~*~*~* Email execution is started ~*~*~*~*~*~*~*~*");
		 P8Connection p8Connection = new P8Connection();
		 ObjectStore ceObjectStore = p8Connection.getCEObjectStore();
		 System.out.println("~*~*~*~*~*~*~* Get Object Store ~*~*~*~*~*~*~*~*");
		
		 SendMailNotification sendMail = new SendMailNotification();
		 ReadProperty readProperty=new ReadProperty();
		 
		 String mySQLString = "SELECT [This], [FolderName], [RecordFolderIdentifier], [Creator], [DateCreated], [CurrentPhaseExecutionStatus], [Id] FROM [RecordFolder] WHERE [CurrentPhaseAction] = Object('{57773143-35F8-4569-9802-7491F0681FE7}') AND [CurrentPhaseExecutionStatus] = 1 OPTIONS(TIMELIMIT 180)";
		      
		 SearchSQL sqlObject = new SearchSQL(mySQLString);
	    
		 // Executes the content search.
		 SearchScope searchScope = new SearchScope(ceObjectStore);            
		 RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, null, null, new Boolean(true));
		 RepositoryRow repos=null;
		 Iterator iterator = rowSet.iterator();
		 List<FormBean> listofRecordFiles= new ArrayList<FormBean>();
	    
		    while (iterator.hasNext())
		    {
		    	 FormBean formBean = new FormBean();
				 repos = (RepositoryRow) iterator.next();
				 Properties properties = repos.getProperties();
				 formBean.setFolderName(properties.getStringValue("FolderName"));
				
				 logger.info("*******FolderName*******"+properties.getStringValue("FolderName"));
				 formBean.setRecordFolderIdentifier(properties.getStringValue("RecordFolderIdentifier"));
				 formBean.setCreator(properties.getStringValue("Creator"));
				 Date dateTimeValue = properties.getDateTimeValue("DateCreated");
				
				 formBean.setDateCreated(dateTimeValue);
				 formBean.setCurrentPhaseExecutionStatus(properties.getInteger32Value("CurrentPhaseExecutionStatus"));
				 listofRecordFiles.add(formBean);
		    }
	    
		    logger.info("listofRecordFiles.size():: "+listofRecordFiles.size());
		    
		    if(listofRecordFiles.size()!=0)
		    {
			    Iterator<FormBean> listIterator = listofRecordFiles.iterator();
	
				while(listIterator.hasNext()) {
					FormBean recordFileBean = listIterator.next();
					logger.info("File Name:: "+recordFileBean.getFolderName());
					
				}
				
				sendMail.sendMailNotification(readProperty.getProperty("fromMail"), readProperty.getProperty("toMail"), readProperty.getProperty("ccMail"), listofRecordFiles);	    
		    }
		    
		    else
		    {
		    	System.out.println("No Record file are Ready for Batch Dispositon Review");
		    }
		   
	 }
	 public static void main(String[] args) {
		 EmailNotificationAutoLanch autoLanch = new EmailNotificationAutoLanch();
		 autoLanch.emailAutoLaunch();
	}
}

