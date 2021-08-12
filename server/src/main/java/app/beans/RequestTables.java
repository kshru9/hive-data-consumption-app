package app.beans;

import io.swagger.annotations.ApiModelProperty;

public class RequestTables {
    
    @ApiModelProperty(notes = "name of database from where data is to be fetched", name = "database", required = true)
    private String database;
    public String getDb(){
        return this.database;
    }

    public void setDb(String db){
        this.database = db;
    }
}
