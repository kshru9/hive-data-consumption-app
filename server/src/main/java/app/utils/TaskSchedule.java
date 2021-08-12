package app.utils;

 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

 

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import app.beans.GetResponse;
import app.beans.Request;

 

import java.util.*;

 

import java.util.regex.Matcher;
import java.util.regex.Pattern;

 

@Async
@Component
class TaskSchedule {

 

    @Autowired
    @Qualifier("hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;
        
    private  final Logger logger = LoggerFactory.getLogger(TaskSchedule.class);

 

    private  final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

 

    // ValidateApi v=new ValidateApi();
        
    @Scheduled(fixedRate = 10000, initialDelay = 60000)
    public void scheduleTaskWithFixedRate() {
        logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );

 

        //testing
        UUID uuid = UUID.randomUUID();
        for (Map.Entry<UUID,GetResponse> entry : GlobalMap.mp.mp.entrySet()){
            GetResponse temp = entry.getValue();
            if (temp.getRes() == "Valid"){
                uuid = entry.getKey();
                break;
            }
        }   
            
        logger.info("got the uuid from hashmap" + uuid);

 

        if (GlobalMap.mp.mp.containsKey(uuid)){
            GetResponse getres = GlobalMap.mp.mp.get(uuid);
            Request g = getres.getReq();
            if(getres.getRes().contentEquals("Valid"))
            {
                    
                getres.setRes("Started and Running");
                logger.info("If Valid: First Response: "+getres.getRes());
                //if limit is null
                if (g.limit==null || g.limit.length()==0) {
                    g.limit = "10";
                }
                
                String finalcol="";
                // if cols are empty
                if(g.getColumns().size()==0 || g.getColumns()==null) {
                    finalcol = "*";
                } else {
                    for(String s:g.getColumns())
                    {
                        finalcol+=s+", ";
                    }
                    logger.info(finalcol);
                    finalcol=finalcol.substring(0,finalcol.length()-2);
                }
                
                String finalfilt="";
                //if filter is empty
                if (g.getFilters().size()==0 || g.getFilters()==null) {
                    //logger.info(g.getFilters().get(0));
                    
                    logger.info(finalfilt);
                    logger.info("select "+finalcol+" from "+g.getDb()+"."+g.getTable()+" limit "+g.limit);
                    StringBuilder SELECT_SQL =new StringBuilder("select ? from ?.? limit ?");
                    int count=0;
                    StringBuilder finalsql=SELECT_SQL;
                    for(int q=0;q<(SELECT_SQL.length()+finalcol.length()+g.getDb().length()+g.getTable().length()+g.limit.length())-1;q++)
                    {
                    	if(SELECT_SQL.charAt(q)=='?')
                    	{
                    		if(count==0)
                    		{
                    			int index=finalsql.indexOf("?");
                    			SELECT_SQL.replace(index, index+1, finalcol);
                    		}
                    		else if(count==1)
                    		{
                    			int index=finalsql.indexOf("?");
                    			SELECT_SQL.replace(index, index+1, g.getDb());
                    		}
                    		else if(count==2)
                    		{
                    			int index=finalsql.indexOf("?");
                    			SELECT_SQL.replace(index, index+1, g.getTable());
                    		}
                    		else if(count==3)
                    		{
                    			int index=finalsql.indexOf("?");
                    			SELECT_SQL.replace(index, index+1, g.limit);
                    			break;
                    		}
                    		count++;
                    	}
                    }
//                    SELECT_SQL.replace(SELECT_SQL.length()-1, SELECT_SQL.length(), dbname);
//                    List<Map<String, Object>> rowsq = hiveJdbcTemplate.queryForList("select "+finalcol+" from "+g.getDb()+"."+g.getTable()+" limit "+g.limit);
                    List<Map<String, Object>> rowsq = hiveJdbcTemplate.queryForList(finalsql.toString());
                    Iterator<Map<String, Object>> itf = rowsq.iterator();
                    int counter=0;
                    try {
                        FileWriter mw=new FileWriter(getres.getFile());
                        // FileOutputStream ofs=new FileOutputStream(myObj);
                        // ObjectOutputStream oos=new ObjectOutputStream(ofs);
                        for(String s:g.getColumns()) {
                            
                            mw.write(s+",");
                        }
                        mw.write("\n");
                            
                        while (itf.hasNext()) {
                            Map<String, Object> row = itf.next();
                            for(String s:g.getColumns()) {
                                String rowbody=(String)row.get(s)+",";
                                // System.out.print((String)row.get(s)+",");
                                mw.write(rowbody);
                            }
                                mw.write("\n");
                                // System.out.println();
                                // oos.writeObject((String)row.get("s")+" ");}
                                // oos.writeChars("\n");
                                    
                                // System.out.println(row);
                                counter++;
                        }
                        if(counter==0)
                        {
                            mw.write("There are no records corresponding to user request");
                            logger.info("There are no records corresponding to user request");
                        }
                        // oos.close();
                        // ofs.close();
                        mw.close();  
                        getres.setRes("Completed");
                        logger.info("If Completed: Response: "+getres.getRes());
                    }    
                    catch(Exception e)
                    {
                        getres.setRes("Failed: Error writing to file");
                        logger.error(e.getMessage());
                    }
                }
              //if filter is not empty
                else {
                    logger.info(g.getFilters().get(0));
                    for(String f:g.getFilters())
                    {
                        Pattern fpattern=Pattern.compile("([-_. \\w]+)([!<=>]+)([-_&#%@$,. \\w]+)");
                        Matcher fmatcher= fpattern.matcher(f);
                        String l="", o="", r="";
                        if(fmatcher.find())
                        {
                            l=fmatcher.group(1).trim();
                            o=fmatcher.group(2).trim();
                            r=fmatcher.group(3).trim();
                        }
                        finalfilt+=l+o;
                        // ValidateApi v=new ValidateApi();
                        int index=ValidateInstance.v.ReturnCols().indexOf(l);
                        String type=ValidateInstance.v.ReturnDts().get(index);
                        switch(type)
                        {
                            case "string":
                                finalfilt+="\'"+r+"\'";
                                System.out.println(r);
                                break;
                            case "int":
                                finalfilt+=r;
                                break;
                        }
                        finalfilt+=" and ";
                    }
                    finalfilt = finalfilt.substring(0,finalfilt.length()-5);
                    logger.info(finalfilt);
                    logger.info("select "+finalcol+" from "+g.getDb()+"."+g.getTable()+" where "+finalfilt+" limit "+g.limit);
                    StringBuilder SELECT_SQL =new StringBuilder("select ? from ?.? where ? limit ?");
                    int count=0;
                    StringBuilder finalsql=SELECT_SQL;
                    for(int q=0;q<SELECT_SQL.length();q++)
                    {
                    	if(SELECT_SQL.charAt(q)=='?')
                    	{
                    		if(count==0)
                    		{
                    			finalsql.replace(q, q+1, finalcol);
                    		}
                    		else if(count==1)
                    		{
                    			int index=finalsql.indexOf("?");
                    			finalsql.replace(index, index+1, g.getDb());
                    		}
                    		else if(count==2)
                    		{
                    			int index=finalsql.indexOf("?");
                    			finalsql.replace(index, index+1, g.getTable());
                    		}
                    		else if(count==3)
                    		{
                    			int index=finalsql.indexOf("?");
                    			finalsql.replace(index, index+1, finalfilt);
                    		}
                    		else if(count==4)
                    		{
                    			int index=finalsql.indexOf("?");
                    			finalsql.replace(index, index+1, g.limit);
                    			break;
                    		}
                    		count++;
                    	}
                    }
//                    List<Map<String, Object>> rowsq = hiveJdbcTemplate.queryForList("select "+finalcol+" from "+g.getDb()+"."+g.getTable()+" where "+finalfilt+" limit "+g.limit);
                    List<Map<String, Object>> rowsq = hiveJdbcTemplate.queryForList(finalsql.toString());
                    Iterator<Map<String, Object>> itf = rowsq.iterator();
                    int counter=0;
                    try {
                        FileWriter mw=new FileWriter(getres.getFile());
                        // FileOutputStream ofs=new FileOutputStream(myObj);
                        // ObjectOutputStream oos=new ObjectOutputStream(ofs);
                        for(String s:g.getColumns()) {
                           
                            mw.write(s+",");
                        }
                        mw.write("\n");
                           
                        while (itf.hasNext()) {
                            Map<String, Object> row = itf.next();
                            for(String s:g.getColumns()) {
                                String rowbody=(String)row.get(s)+",";
                                // System.out.print((String)row.get(s)+",");
                                mw.write(rowbody);
                            }
                                mw.write("\n");
                                // System.out.println();
                                // oos.writeObject((String)row.get("s")+" ");}
                                // oos.writeChars("\n");
                                   
                                // System.out.println(row);
                                counter++;
                        }
                        if(counter==0)
                        {
                            mw.write("There are no records corresponding to user request");
                            logger.info("There are no records corresponding to user request");
                        }
                        // oos.close();
                        // ofs.close();
                        mw.close();
                        getres.setRes("Completed");
                        logger.info("If Completed: Response: "+getres.getRes());
                    }   
                    catch(Exception e)
                    {
                        getres.setRes("Failed: Error writing to file");
                        logger.error(e.getMessage());
                    }
                }
            }
            else if (getres.getRes().contentEquals("Completed")) {
                getres.setRes("Completed");
                logger.info("If Completed: Response: "+getres.getRes());
            }
            //If not valid
            else {
                getres.setRes("Failed " + getres.getRes());
                logger.info("If Failed: Response: "+getres.getRes());
            }
               
        } else {
            logger.info("UUID doesnt exist in map");
        }
    }
}