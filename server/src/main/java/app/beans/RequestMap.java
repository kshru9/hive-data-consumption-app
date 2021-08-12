package app.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class RequestMap {
    public Map<UUID, GetResponse> mp = new HashMap<UUID, GetResponse>();

    public RequestMap(){}
    public RequestMap(UUID uuid, GetResponse req){
        setRequest(uuid, req);
    }

    public void setRequest(UUID key, GetResponse value){   
        mp.put(key, value);
    }

    public void display(UUID uuid) {
    	GetResponse test = mp.get(uuid);
        Request req = test.getReq();
    	System.out.println(req.getLimit());
    	System.out.println(req.getDb());
    	System.out.println(req.getTable());
    }
    public GetResponse getRequest(UUID key){
        return mp.get(key);
    }

    private ArrayList<String> reqToString(GetResponse getres){
    	ArrayList<String> reply=new ArrayList<String>();
        String ans = "";
        String res = getres.getRes();
        String f=getres.getFile();
        	ans += "response" + ":" + res + "\n";
        	reply.add(res);
        	reply.add(ans);
        	reply.add(f);
        return reply;
    }

    public String toStringId(UUID key){
        String ans = "";
        GetResponse req = getRequest(key);
        ArrayList<String> reply=reqToString(req);
        if(reply.get(0).contentEquals("Valid")) {
        	ans += "uuid" + ":" + key.toString() + "\n" +reply.get(1) + "\n" + "file location: "+reply.get(2);
        }
        else {
        	ans += reply.get(1) + "\n";
        }
        
        return ans;
    }

    public JSONObject toJsonId(UUID key) {
        GetResponse req = getRequest(key);

        JSONObject json = new JSONObject();
        if (req.getRes().contains("Valid")){
            try {
                json.put("uuid", key);
                json.put("response", req.getRes());
                json.put("file location", req.getFile());
            } catch( JSONException e) {
                return null;
            }
        } else {
            try {
                json.put("uuid", key);
                json.put("response", req.getRes());
                json.put("file location", "None");
            } catch( JSONException e) {
                return null;
            }
        }
	
        return json;
    }

    public String printAll(){
        String ans = "";
        for (Map.Entry<UUID, GetResponse> entry : mp.entrySet()) {
            UUID key = entry.getKey();
            String out = toStringId(key);
            System.out.println(out);
            ans += out + "\n";
        }
        return ans;
    }
}
