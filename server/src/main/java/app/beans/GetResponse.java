package app.beans;

public class GetResponse{
    private Request req;
    private String res;
    private String file="";
    
    public GetResponse(){}

    GetResponse(Request req, String res, String file){
    	setReq(req);
        setRes(res);
        setFile(file);
        
    }

    public Request getReq(){
        return this.req;
    }
    
    public String getRes(){
        return this.res;
    }
    
    public String getFile(){
        return this.file;
    }
    
    public void setReq(Request r){
        req = r;
    }

    public void setRes(String r){
        res = r;
    }
    
    public void setFile(String f){
        file = f;
    }
    
}

