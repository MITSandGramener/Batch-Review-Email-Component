package com.ssl.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import com.ssl.bean.FormBean;
import com.ssl.util.ErrorLogger;
import com.ssl.util.P8Connection;
import com.ssl.util.ReadProperty;

public class SendMailNotification {

	private static final ErrorLogger logger = new ErrorLogger("SendMailNotification");

	P8Connection conn = new P8Connection();

	final String smtpAuthUserName = ReadProperty.getProperty("smtpAuthUserName");
	final String smtpAuthPassword = ReadProperty.getProperty("smtpAuthPassword");

	Authenticator authenticator = new Authenticator()
	{
		@Override
		protected PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(smtpAuthUserName, smtpAuthPassword);
		}
	};

	

	public String sendMailNotification(String fromAddress, String toAddress, String ccAddress, List<FormBean> listofRecordFiles){
		logger.debug("Entered into sendMailNotification method");
		String status = null;
		StringBuilder body = new StringBuilder();
		String subject =  null;
		int count = 0;

		java.util.Properties props = new java.util.Properties();

		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", ReadProperty.getProperty("SMTP_SERVER").trim());
		props.setProperty("mail.smtp.port", ReadProperty.getProperty("SMTP_PORT").trim());
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "false");

		Session s = Session.getInstance(props, authenticator);
		MimeMessage message = new MimeMessage(s);

		subject = "Batch Disposition Review & Disposition Record Files";

		try { 
			int docSize = listofRecordFiles.size();
			if(!listofRecordFiles.isEmpty()) {
				InternetAddress[] to_Address = InternetAddress.parse(toAddress.replaceAll(";", ","), false);
				Address address = new InternetAddress(fromAddress);

				message.setRecipients(Message.RecipientType.TO, to_Address);
				message.setFrom(address);

				InternetAddress[] cc_Address = (InternetAddress[])null;

				if (ccAddress != null) {
					cc_Address = InternetAddress.parse(ccAddress.replaceAll(";", ","), false);
					logger.debug("[sendEmail] CC Address after parse :"+cc_Address.toString());
					message.setRecipients(Message.RecipientType.CC, cc_Address);
				}

				Iterator<FormBean> listIterator = listofRecordFiles.iterator();

				while(listIterator.hasNext()) {
					FormBean recordFileBean = listIterator.next();

					if(count ==0) {						
						body.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='margin-top:10px;FONT-SIZE: 10pt; FONT-FAMILY: Verdana, Arial;'>");
						body.append("<tr><td><B> <font size='5'> Batch Review & Disposition Record Files </B></td></tr>");
						body.append("<tr><td height='30'>This report was generated to send Email Notification regarding Record Files Which are Ready for Batch Review & Disposition:</td></tr>");
						body.append("</table>");

						body.append("<table id='tbl_records' border='0' cellpadding='2' cellspacing='2'  width='100%'>" +					
								"<tr style='background-color:lightblue' style='width:100%;'><th style='background-color:lightblue' width='5%' align='center'>S No</th>" +
								"<th style='background-color:lightblue' width='15%' align='center'>Record File Name</th>" +
								"<th style='background-color:lightblue' width='20%' align='center'>Record File Identifier</th>" +
								"<th style='background-color:lightblue' width='10%' align='center'>Record File Creator</th>" +
								"<th style='background-color:lightblue' width='10%' align='center'>File Date Created</th>" +
								"<th style='background-color:lightblue' width='10%' align='center'>Record File Status</th>");
					} 
					if(docSize % 2 == 0) {
						body.append("<tr><td width='5%' align='center' id='slno'>"+(count+1)+"</td>" +
								"<td width='15%' align='left' id='RecordFileName'>"+recordFileBean.getFolderName()+"</td>" +
								"<td width='20%' align='left'  id='RcordFileIdentifier'>"+recordFileBean.getRecordFolderIdentifier()+"</td>" +
								"<td width='15%' align='center' id='RecordFileCreator'>"+recordFileBean.getCreator()+"</td>" +  		
								"<td width='10%' align='center' id='FileDateCreated'>"+getConvertedDate(recordFileBean.getDateCreated())+"</td>" + 
								"<td width='10%' align='center' id='RecordFileStatus'>"+"Record File is Ready"+"</td>");
					} else {
						body.append("<tr style='background-color:lightgrey'><td width='5%' align='center' id='slno'>"+(count+1)+"</td>" +
								"<td width='15%' align='left' id='RecordFileName'>"+recordFileBean.getFolderName()+"</td>" +
								"<td width='20%' align='left'  id='RcordFileIdentifier'>"+recordFileBean.getRecordFolderIdentifier()+"</td>" +
								"<td width='15%' align='center' id='RecordFileCreator'>"+recordFileBean.getCreator()+"</td>" +  		
								"<td width='10%' align='center' id='FileDateCreated'>"+getConvertedDate(recordFileBean.getDateCreated())+"</td>" + 
								"<td width='10%' align='center' id='RecordFileStatus'>"+"Record File is Ready"+"</td>");
					}

					//body.append("");
					docSize = docSize-1;
					count++;
				}
				
				body.append("</table>");

				body.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='margin-top:10px;FONT-SIZE: 10pt; FONT-FAMILY: Verdana, Arial;'>");
				String searchtemplatelink=ReadProperty.getProperty("SearchTemplateURL");
				body.append("<font size='3' FONT-FAMILY: Verdana, Arial; >Login to IER Application to Initiate Batch process:: "+"<a href="+searchtemplatelink+"> Click here </a>");
				body.append("</table>");

				body.append("</table> ");

				body.append("<table width='100%' border='0' cellpadding='0' cellspacing='0' style='margin-top:10px;FONT-SIZE: 10pt; FONT-FAMILY: Verdana, Arial;'>");
				body.append("<font color='red'>This message is system generated. Please do not reply");
				body.append("</table>");

				message.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
				message.setContent(body.toString(), "text/html; charset=UTF-8");
				Transport.send(message);
				status = "Success";
				System.out.println("~*~*~*~*~*~*~* Batch Review Record files list sent you mail ~*~*~*~*~*~*~*~*");
			} else {
				logger.debug("No Record list for send mail notification regarding the date of expiry ");
			}
			}
			catch (Exception e) {
				e.printStackTrace();
				status = "Exception occured :"+e.getMessage();
				logger.error("Exception Occured at getWorkflow :: "+e.getMessage());
			}

			return status;
		}

		public String getPreviousDate(int number) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, +number);
			int day = cal.get(Calendar.DATE);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);

			month = month + 1;
			String strMonth = "";
			if(month<10) {
				strMonth = "" + 0 + month ;
			}
			String dateValue = "" + year +  strMonth + day + "T000000Z";
			return dateValue;
		}

		public String getConvertedDate(Date inputDate)  throws ParseException {
			if(inputDate != null) {
				SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy"); 
				return sm.format(inputDate);			
			} 
			return "";
		}
	}
