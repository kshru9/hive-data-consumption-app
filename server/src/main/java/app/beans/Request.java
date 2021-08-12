package app.beans;

import java.util.List;
import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;

public class Request {
    @ApiModelProperty(notes = "id of the request", name = "id", required = false, value = "12345")
    public long id;
    
    @ApiModelProperty(notes = "uuid", name = "uuid", required = false, value = "dac1d6d2-57d6-49a2-9a5f-648d439fa1b7")
    public UUID uuid = UUID.randomUUID();
    
    @ApiModelProperty(notes = "name of columns to be read", name = "columns", required = true, value = "['name', 'age']")
    public List<String> columns;
    
    @ApiModelProperty(notes = "name of filters to be applied", name = "filters", required = true, value = "['name', 'age']")
    public List<String> filters;
    
    @ApiModelProperty(notes = "number of entries to be fetch", name = "limit", required = false, value = "100")
    public String limit;
    
    @ApiModelProperty(notes = "name of db from where data is to be fetched", name = "db", required = true)
    public String db;
    
    @ApiModelProperty(notes = "name of table from where data is to be fetched", name = "table", required = true)
    public String table;

    public long getId(){
        return this.id;
    }
    
    public void setId(long id){
        this.id = id;
    }
    
    public List<String> getColumns(){
        return this.columns;
    }
    
    public void setColumns(List<String> columns){
        this.columns = columns;
    }
    
    public List<String> getFilters(){
        return this.filters;
    }
    
    public void setFilters(List<String> filters){
        this.filters = filters;
    }
    
    public String getLimit(){
        return this.limit;
    }

    public void setLimit(String limit){
        this.limit = limit;
    }

    public String getTable(){
        return this.table;
    }

    public void setTable(String table){
        this.table = table;
    }
    
    public String getDb(){
        return this.db;
    }

    public void setDb(String db){
        this.db = db;
    }

    public UUID getUuid(){
        return this.uuid;
    }

    public void setUuid(UUID uuid){
        this.uuid = uuid;
    }
}
