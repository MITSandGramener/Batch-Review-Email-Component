package com.ssl.util;


import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;



public class P8Connection 
{
	private static final ErrorLogger logger = new ErrorLogger("P8Connection");
	StringEncrypter encrypt=new StringEncrypter();
	
	

	ObjectStore objstore=null;
	/**********************************************************************************************/
	/**
	 * Connecting to process engine and acquiring a VWSession.
	 * @return VWSession for the user.
	 */
	public ObjectStore getCEObjectStore()
	{
		String Uri = ReadProperty.getProperty("URI");
		Connection conn = Factory.Connection.getConnection(Uri); 
		//String decryptpassword = encrypt.decrypt(ReadProperty.getProperty("PASSWORD"));
		String decryptedpassword = encrypt.getDeMes(ReadProperty.getProperty("USER_NAME"), ReadProperty.getProperty("PASSWORD"));
		Subject subject = UserContext.createSubject(conn, ReadProperty.getProperty("USER_NAME"),decryptedpassword,null);
		UserContext.get().pushSubject(subject);
		Domain domain = Factory.Domain.fetchInstance(conn, ReadProperty.getProperty("DOMAIN"), null);
		objstore=Factory.ObjectStore.fetchInstance(domain, ReadProperty.getProperty("OBJECTSTORE"), null);
		
		

		logger.info("In getCESession");


		logger.info("Connected To CE Succesfully " +  objstore.get_Name());



		return objstore;
	}




public static void main(String[] args) {
	
	P8Connection  p8=new P8Connection();
	p8.getCEObjectStore();
}

}

