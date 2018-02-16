package com.ssl.bean;

import java.io.Serializable;
import java.util.Date;

public class FormBean implements Serializable{
	
	private String FolderName,RecordFolderIdentifier,Creator,Id;
	private int CurrentPhaseExecutionStatus; 
	private Date DateCreated;
	
	private String mailFrom = "";
	public String getMailFrom() {
		return mailFrom;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailCC() {
		return mailCC;
	}
	public void setMailCC(String mailCC) {
		this.mailCC = mailCC;
	}
	
	
	public String getFolderName() {
		return FolderName;
	}

	public void setFolderName(String folderName) {
		FolderName = folderName;
	}

	public String getRecordFolderIdentifier() {
		return RecordFolderIdentifier;
	}

	public void setRecordFolderIdentifier(String recordFolderIdentifier) {
		RecordFolderIdentifier = recordFolderIdentifier;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public Date getDateCreated() {
		return DateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		DateCreated = dateCreated;
	}

	public int getCurrentPhaseExecutionStatus() {
		return CurrentPhaseExecutionStatus;
	}

	public void setCurrentPhaseExecutionStatus(int currentPhaseExecutionStatus) {
		CurrentPhaseExecutionStatus = currentPhaseExecutionStatus;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	private String mailTo   = "";
	private String mailCC   = "";
	private String participant = "";
	private String status = null;
	
	
}
