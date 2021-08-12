package app.beans;

import io.swagger.annotations.ApiModelProperty;

public class RequestCols {
	    @ApiModelProperty(notes = "name of table from where data is to be fetched", name = "table", required = true)
	    private String table;
	    
	    @ApiModelProperty(notes = "name of database from where data is to be fetched", name = "database", required = true)
	    private String database;

	    public String getTable(){
	        return this.table;
	    }

	    public void setTable(String table){
	        this.table = table;
	    }
	    
	    public String getDb(){
	        return this.database;
	    }

	    public void setDb(String db){
	        this.database = db;
	    }
}
