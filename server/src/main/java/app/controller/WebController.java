package app.controller;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.beans.GetResponse;
import app.utils.ValidateInstance;

import app.beans.Request;
import app.beans.RequestCols;
import app.beans.RequestTables;
import app.utils.GlobalMap;
import io.swagger.annotations.ApiOperation;

@ApiOperation(value = "/api", tags = "Rest Apis controllers")
@RestController
public class WebController {

    // public RequestMap mp = new RequestMap();

    
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

	@Autowired
	@Qualifier("hiveJdbcTemplate")
	private JdbcTemplate hiveJdbcTemplate;
    
    @ApiOperation(value = "Home page")
    @GetMapping("/api")
    public String home(){
        return "up and running";
    }

    // ValidateApi v=new ValidateApi();
    
    @ApiOperation(value = "Form submission")
    @PostMapping("/api/save")
    public String save(@RequestBody Request req){
        final List<String> col;
        final List<String> filt;
        final String lim;
        final String table;
        final String db;
        final UUID uuid;

        col = req.getColumns();
        filt = req.getFilters();
        lim = req.getLimit();
        db = req.getDb();
        table = req.getTable();
        uuid = req.getUuid();
        
        
        ArrayList<String> val = ValidateInstance.v.val(col, filt, lim, db, table);

        // setting all in my response which would be my value in <key,value> in hashmap
        GetResponse res = new GetResponse();
        res.setReq(req);
        res.setRes(val.get(1));
        String filepath="C:\\Users\\skatpara\\Downloads\\Files\\"+uuid+".csv";
        res.setFile(filepath);
        
        GlobalMap.mp.setRequest(uuid, res);
        
        GlobalMap.mp.display(uuid);
        JSONObject ans = GlobalMap.mp.toJsonId(uuid);
        logger.info("post api returning: " + ans.toString());
        return ans.toString();
    }
    
    @GetMapping("api/status/{uuid}")
    public String status(@PathVariable UUID uuid){
        JSONObject ret = new JSONObject();
        if (GlobalMap.mp.mp.containsKey(uuid)) {
            ret= GlobalMap.mp.toJsonId(uuid);
        } 
        else {
            try{
                ret.put("response","Invalid UUID");
                ret.put("file location", "");
                ret.put("uuid", uuid);
            } 
            catch ( JSONException e) {}
        }
        return ret.toString();
    }

    @GetMapping("api/map/")
    public String map(){
        String ret = GlobalMap.mp.printAll();
        return ret;
    }

    @GetMapping("/api/getdbs")
	public ArrayList<String> selectdbs() {
    	String sql = "show databases";
		List<Map<String, Object>> rows = hiveJdbcTemplate.queryForList(sql);
		Iterator<Map<String, Object>> it = rows.iterator();
		List<String> dbs = new ArrayList<String>();
		while (it.hasNext()) {
            Map<String, Object> row = it.next();
            ValidateInstance.v.AddDbs((String)row.get("database_name"),dbs);
			// logger.info(String.format("%s\t%s", row.get("medicare_demographic.id"), row.get("medicare_demographic.name")));  
        
		}
        
		return (ArrayList<String>) dbs;
	}
    
    @GetMapping("/api/gettables/{db}")
	public ArrayList<String> selecttables(@PathVariable String db) {
    	List<Map<String, Object>> rows2 = hiveJdbcTemplate.queryForList("show tables in "+db);
		Iterator<Map<String, Object>> it2 = rows2.iterator();
		List<String> tables = new ArrayList<String>();
		while (it2.hasNext()) {
            Map<String, Object> row2 = it2.next();
            ValidateInstance.v.AddTables((String)row2.get("tab_name"),tables);
			// logger.info(String.format("%s\t%s", row2.get("medicare_demographic.id"), row2.get("medicare_demographic.name")));
        
		}
		return (ArrayList<String>)tables;
	}
    
    @ApiOperation(value = "Table retrieval")
    @PostMapping("/api/getTables")
    public ArrayList<String> getTables(@RequestBody RequestTables req){
        final String dbname;
        dbname = req.getDb();
        List<Map<String, Object>> rows2 = hiveJdbcTemplate.queryForList("show tables in "+dbname);
		Iterator<Map<String, Object>> it2 = rows2.iterator();
		List<String> tables = new ArrayList<String>();
		while (it2.hasNext()) {
            Map<String, Object> row2 = it2.next();
            ValidateInstance.v.AddTables((String)row2.get("tab_name"),tables);
			// logger.info(String.format("%s\t%s", row2.get("medicare_demographic.id"), row2.get("medicare_demographic.name")));
        
		}
        
		return (ArrayList<String>) tables;
	}
    
    
    @ApiOperation(value = "Form submission")
    @PostMapping("/api/getcolumns")
    public ArrayList<String> getcolumns(@RequestBody RequestCols req){
        final String dbname;
        final String tablename;
        dbname = req.getDb();
        tablename = req.getTable();
        List<Map<String, Object>> rows3 = hiveJdbcTemplate.queryForList("describe "+dbname+"."+tablename);
		Iterator<Map<String, Object>> it3 = rows3.iterator();
		List<String> cols = new ArrayList<String>();
		while (it3.hasNext()) {
            Map<String, Object> row3 = it3.next();
            ValidateInstance.v.AddCols((String)row3.get("col_name"),cols);
            ValidateInstance.v.AddDts((String)row3.get("data_type"));
		}

        ValidateInstance.v.assignCols(cols);
		return (ArrayList<String>)cols;
	}
    
    // @Component
    // class TaskSchedule {
    	
    //     private  final Logger logger = LoggerFactory.getLogger(TaskSchedule.class);

    //     private  final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        
    //     @Scheduled(fixedRate = 30000, initialDelay = 60000)
    //     public void scheduleTaskWithFixedRate() {
    //         logger.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );

    //         // mp.printAll();
    //         //testing
    //         UUID uuid = UUID.randomUUID();
    //         for (Map.Entry<UUID,GetResponse> entry : mp.mp.entrySet()){
    //             GetResponse temp = entry.getValue();
    //             if (temp.getRes() == "Valid"){
    //                 uuid = entry.getKey();
    //                 break;
    //             }
    //         }   
            
    //         System.out.print("got the uuid from hashmap");
    //         System.out.println(uuid);

    //         if (mp.mp.containsKey(uuid)){
    //             GetResponse g = mp.mp.get(uuid);
    //             if(g.res.contentEquals("Valid"))
    //             {
                    
    //                 g.res = "Started and Running";
    //                 System.out.println("If Valid: First Response: "+g.res);
    //                 String finalcol="";
    //                 for(String s:g.col)
    //                 {
    //                     finalcol+=s+", ";
    //                 }
    //                 finalcol=finalcol.substring(0,finalcol.length()-2);
    //                 String finalfilt="";
    //                 for(String f:g.filt)
    //                 {
    //                     Pattern fpattern=Pattern.compile("([-_. \\w]+)([!<=>]+)([-_&#%@$,. \\w]+)");
    //                     Matcher fmatcher= fpattern.matcher(f);
    //                     String l="", o="", r="";
    //                     if(fmatcher.find())
    //                     {
    //                         l=fmatcher.group(1).trim();
    //                         o=fmatcher.group(2).trim();
    //                         r=fmatcher.group(3).trim();
    //                     }
    //                     finalfilt+=l+o;
    //                     //ValidateApi v=new ValidateApi();
    //                     int index=v.ReturnCols().indexOf(l);
    //                     String type=v.ReturnDts().get(index);
    //                     switch(type)
    //                     {
    //                         case "string":
    //                             finalfilt+="\'"+r+"\'";
    //                             System.out.println(r);
    //                             break;
    //                         case "int":
    //                             finalfilt+=r;
    //                             break;
    //                     }
    //                     finalfilt+=" and ";
    //                 }
    //                 finalfilt=finalfilt.substring(0,finalfilt.length()-5);
    //             List<Map<String, Object>> rowsq = hiveJdbcTemplate.queryForList("select "+finalcol+" from "+g.db+"."+g.table+" where "+finalfilt+" limit "+g.lim);
    //             Iterator<Map<String, Object>> itf = rowsq.iterator();
    //             int counter=0;
    //             try {
    //             File myObj=new File(g.file);
    //             FileWriter mw=new FileWriter(g.file);
    //      		// FileOutputStream ofs=new FileOutputStream(myObj);
    //      		// ObjectOutputStream oos=new ObjectOutputStream(ofs);
    //             for(String s:g.col) {
    //                 //System.out.print(s+",");
    //                 mw.write(s+",");
    //             }
    //                 mw.write("\n");
    //                 // System.out.println();
    //             while (itf.hasNext()) {
    //                 Map<String, Object> row = itf.next();
    //                 for(String s:g.col) {
    //                     String rowbody=(String)row.get(s)+",";
    //                     // System.out.print((String)row.get(s)+",");
    //                     mw.write(rowbody);
    //                 }
    //                     mw.write("\n");
    //                     //System.out.println();
    //                     // oos.writeObject((String)row.get("s")+" ");}
    //                     // oos.writeChars("\n");
                        
    //                     // System.out.println(row);
    //                     counter++;
    //             }
    //             if(counter==0)
    //             {
    //                 mw.write("There are no records corresponding to user request");
    //                 System.out.println("There are no records corresponding to user request");
    //             }
    //      		// oos.close();
    //      		// ofs.close();
    //                 mw.close();  }    
    //             catch(Exception e)
    //             {
    //                 System.out.println(e.getMessage());
    //             }
    //             g.res = "Completed";
    //             System.out.println("If Completed: Response: "+g.res);
    //             } 
    //             else if (g.res.contentEquals("Completed")) {
    //                 g.res = "Completed";
    //                 System.out.println("If Completed: Response: "+g.res);
    //             }
    //             //If not valid
    //             else {
    //                 g.res = "Failed  " + g.res;
    //                 System.out.println("If Failed: Response: "+g.res);
    //             }
                
    //         } else {
    //             System.out.println("UUID doesnt exist in map");
    //         }
    //     }
    // }
}