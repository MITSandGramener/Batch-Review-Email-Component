package com.ssl.util;



import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.NullEnumeration;


public class ErrorLogger {

    /**
     * The reference for Log4j's Logger class
     */

 
	Logger logger = null;
    static String modeOfLog = null;
	
	// appender 
	//is only of a single type,maintain only one instance of an appender 
	//for every logger
	static Appender appender = null;
	
	//This determines a switch
	//whether the messages shud go to log file or be redirected to console
	//true - To the Console
	//false - To the File
	static String logConsoleStatus = null;
	
	//Default a pattern and override that with the
	//value in the orbibilling.properties file
	//also the name of the file where logging messages will be written
	//to when the logConsoleStatus is set to false
	static String logPattern = null;
	static String logFileName = null;

	//This variable to read the pattern involved
    //for rolling logs i.e: Monthly/Weekly/Daily rolls of log files
    //default it to Daily and override it from the entry in orbibilling.properties
    //(Default is to Create a new Log file midnight on every day)
    //asociated pattern being (yyyy-MM-dd)
    static String logRollingPattern = null;
	
    static {
    	/*
		InputStream is = ErrorLogger.class.getClassLoader(
							).getSystemResourceAsStream("Config.properties");
		Properties dbProps = new Properties();

		try {
			dbProps.load( is );
		} catch ( Exception e ) {
			throw new RuntimeException( "Can't read the properties file. " +
					"Make sure DQ.properties is in the CLASSPATH" );

		}*/

		modeOfLog = ReadProperty.getProperty( "LOG_MODE" );
		logConsoleStatus = ReadProperty.getProperty( "LOG_CONSOLE_STATUS" );
		logPattern = ReadProperty.getProperty( "LOG_PATTERN" );
		logFileName = ReadProperty.getProperty( "LOG_FILE_NAME" );
		logRollingPattern = ReadProperty.getProperty( "LOG_ROLLING_PATTERN" );
						
	}

    /**
     * This constructor takes the module name as parameters,
     * all users will make an instance of this class by
     * passing their module name, this constructor in tern
     * calls Log4j's getLogger method py passing the
     * passed parameter, so as to make Logger instance
     * for particular module,
     */


    public ErrorLogger(String name) {

		if ( modeOfLog == null )
			throw new RuntimeException( "No property for LOG_MODE defined in Config.properties" );

        Layout layout = new PatternLayout(logPattern);
		
        //Get the index of : and set the appender
        //as the value before :
		
        String moduleName = null;
        String fileName = null;
        
        int index = name.indexOf( ':' );
        if ( index != -1 ) {
            moduleName = name.substring( 0, index );
            fileName = name.substring( index + 1 );
        } else {
            fileName = name;
        }
				
        logger = Logger.getLogger(fileName);
        
		if( modeOfLog.trim().equalsIgnoreCase("DEBUG") ) {
	        logger.setLevel(Level.DEBUG);

		} else if ( modeOfLog.trim().equalsIgnoreCase("INFO") ) {
			logger.setLevel( Level.INFO );

		} else if ( modeOfLog.trim().equalsIgnoreCase("WARN") ) {
			logger.setLevel( Level.WARN );

		} else if ( modeOfLog.trim().equalsIgnoreCase("ERROR") ) {
			logger.setLevel( Level.ERROR );

		} else if ( modeOfLog.trim().equalsIgnoreCase("FATAL") ) {
			logger.setLevel( Level.FATAL );
		}
		
	
	    if (appender == null) {
		
			if (logConsoleStatus.equalsIgnoreCase("true")) {
			
				appender = new ConsoleAppender(layout,ConsoleAppender.SYSTEM_OUT);
								
			}else if (logConsoleStatus.equalsIgnoreCase("false")){
			
				if(logFileName == null || logFileName.equals("")) 
					throw new RuntimeException( "Please set the property for LOG_FILE_NAME in Config.properties" );		
				
				try {

           			appender = new DailyRollingFileAppender(layout,logFileName,logRollingPattern+"'.out'");

				}catch(IOException ioe) {
					throw new RuntimeException(ioe.getMessage());
				}
				
			}
	    }
		
		if (logger.getAllAppenders() instanceof NullEnumeration) {
			logger.addAppender( appender );
	        logger.setAdditivity(false);
		}
	

    }	       
    
    	   
    /**
     * Add the first var content to the message in the last
     * @param message Object -
     * the message object to log.
     * @param varToAppend Object[] -
     * the variable content to add to the message.
     * @return message String - the formatted message
     */

    public String formatMessage(Object message, Object[] varToAppend) {

       if ( varToAppend == null )
            return message.toString();
        try {
            return message.toString() + " : "+ varToAppend[0].toString() + varToAppend[1].toString();
        } catch ( Exception e ) {
            return message.toString();
        }

    }


    /**
     * Log a message object with the
     * {@link Level#DEBUG DEBUG} level.
     * @param debugMessage Object -
     * the message object to log.
     */


    public void debug(String debugMessage) {
        logger.debug( debugMessage );

    }

    /**
     * Log a message object with the
     * {@link Level#DEBUG DEBUG} level.
     * @param debugMessage Object -
     * the message object to log.
     * @param varToAppend Object[] -
     * the variable content to add to the message.
     */


    public void debugMf(Object debugMessage, Object[] varToAppend) {

        String formattedMsg = formatMessage( debugMessage, varToAppend );
        logger.debug( formattedMsg );

    }


    /**
     * Log a message object with the
     * {@link Level#INFO INFO} level.
     * @param infoMessage Object -
     * the message object to log.
     */
	 
    public void info(Object infoMessage) {
        logger.info( infoMessage );
    }


    /**
     * Log a message object with the
     * {@link Level#INFO INFO} level.
     * @param infoMessage Object -
     * the message object to log.
     * @param varToAppend Object[] -
     * the variable content to add to the message.
     */


    public void infoMf(Object infoMessage, Object[] varToAppend) {

        String formattedMsg = formatMessage( infoMessage, varToAppend );
        logger.info( formattedMsg );
    }

    /**
     * Log a message object with the
     * {@link Level#WARN WARN} level.
     * @param warnMessage Object -
     * the message object to log.
     */


    public void warn(Object warnMessage) {

        logger.warn( warnMessage );
    }


   /**
     * Log a message object with the
     * {@link Level#WARN WARN} level.
     * @param warnMessage Object -
     * the message object to log.
     * @param varToAppend Object[] -
     * the variable content to add to the message.
     */


    public void warnMf(Object warnMessage, Object[] varToAppend) {

        String formattedMsg = formatMessage( warnMessage, varToAppend );
        logger.warn( formattedMsg );
    }


   /**
     * Log a message object with the
     * {@link Level#ERROR ERROR} level.
     * @param errorMessage Object -
     * the message object to log.
     * @param e Exception -
     * the exception object to have stack trace.
     */


    public void error(String errorMessage) {
        logger.error( errorMessage );
    }

    /**
     * Log a message object with the
     * {@link Level#ERROR ERROR} level.
     * @param errorMessage Object -
     * the message object to log.
     * @param e Exception -
     * the exception object to have stack trace.
     * @param varToAppend Object[] -
     * the variable content to add to the message.
     */


    public void errorMf(Object errorMessage, Exception e, Object[] varToAppend) {
        String formattedMsg = formatMessage( errorMessage, varToAppend );
        logger.error( formattedMsg, e );

    }

    /**
     * Log a message object with the
     * {@link Level#FATAL FATAL} level.
     * @param fatalMessage Object -
     * the message object to log.
     * @param e Exception -
     * the exception object to have stack trace.
     */

    public void fatal(Object fatalMessage) {
        logger.fatal( fatalMessage );

    }
    
     public void fatal(Object fatalMessage,Exception e) {
        logger.fatal( fatalMessage ,e);

    }

    /**
     * Log a message object with the
     * {@link Level#FATAL FATAL} level.
     * @param fatalMessage Object -
     * the message object to log.
     * @param e Exception -
     * the exception object to have stack trace.
     * @param varToAppend Object[] -
     * the variable content to add to the message.
     */

    public void fatalMf(Object fatalMessage, Exception e, Object[] varToAppend) {
        String formattedMsg = formatMessage( fatalMessage, varToAppend );
        logger.fatal( formattedMsg, e );
    }

    public static void main(String args[]) {
	
        ErrorLogger errorLogger = new ErrorLogger( "Amar:Test" );
        errorLogger.debug( "Hello Amar" );

    }


}