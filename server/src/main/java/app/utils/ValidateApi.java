package app.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateApi {
    
	List<String> cols=new ArrayList<String>();
	List<String> dbs=new ArrayList<String>();
	// List<String> tables=new ArrayList<String>();
	List<String> fils=new ArrayList<String>();
	List<String> dts=new ArrayList<String>();
	
	public void assignCols(List<String> ref_cols){
		cols = ref_cols;
	}


	public void AddDbs(String dbname, List<String> ref_dbs)
	{
		ref_dbs.add(dbname);
		if (dbs.isEmpty()){
			dbs.add(dbname);
		}
	}
	public ArrayList<String> ReturnDbs()
	{
		return (ArrayList<String>) dbs;
	}
	// public ArrayList<String> ReturnTables()
	// {
	// 	return (ArrayList<String>) tables;
	// }
	public ArrayList<String> ReturnCols()
	{
		return (ArrayList<String>) cols;
	}
	public ArrayList<String> ReturnDts()
	{
		return (ArrayList<String>) dts;
	}
	
	public void DisplayDbs()
	{
		for(String t:dbs)
		{
			System.out.print(t+" ");
		}
	}
	
	 //Database validation
	public boolean DbVal(String db)
	{
		if(dbs.contains(db))
		{
			 return true;
		}
		return false;
	}
	 
	 
	public void AddTables(String tname, List<String> ref_tables)
	{
		ref_tables.add(tname);
		// tables.add(tname);
	}
	
	// public void DisplayTs()
	// {
	// 		 for(String t:tables)
	// 		 {
	// 			 System.out.print(t+" ");
	// 		 }
	// }
		
		//  //Table validation
		//  public boolean TVal(String table)
		//  {
		// 	 if(tables.contains(table))
		// 	 {
		// 		 return true;
		// 	 }
		// 	 return false;
		//  }
	 
		 public void AddCols(String cname, List<String> ref_cols)
			{
				ref_cols.add(cname);
				// cols.add(cname);
			}
			 public void DisplayCols()
			 {
				 for(String t:cols)
				 {
					 System.out.print(t+" ");
				 }
			 }
			 
			 public void AddDts(String dt)
				{
					dts.add(dt);
				}
				 public void DisplayDts()
				 {
					 for(String t:dts)
					 {
						 System.out.print(t+" ");
					 }
				 }
	 
	 
				 public ArrayList<String> val(List<String> col, List<String> filt, String lim, String db, String table){
				        ArrayList<String> result=new ArrayList<String>();
				        String msg="";
				        String finalans="true";
				        Boolean filtcheck=true;
				        String lhs="", rhs="",op="";
				        ArrayList<String> operators=new ArrayList<String>();
				        operators.add(">");
				        operators.add("<");
				        operators.add(">=");
				        operators.add("<=");
				        operators.add("!=");
				        operators.add("=");
				        operators.add("<>");
				        Pattern pattern=Pattern.compile("([-_. \\w]+)([!<=>]+)([-_&#%@$,. \\w]+)");
				        Pattern pattern2=Pattern.compile("([-_&#%@$,. \\w]+)");
				        Pattern pattern3=Pattern.compile("([,.\\d]+)");
				        if (filt.size()>0) {
				            for(String s:filt)
				            {
				                Matcher matcher= pattern.matcher(s);
				                if(matcher.find())
				                {
				                    lhs=matcher.group(1).trim();
				                    op=matcher.group(2).trim();
				                    rhs=matcher.group(3).trim();
				                }
				                else {
				                    msg="Error: Invalid filter condition";
				                    filtcheck=false;
				                    break;
				                }
				                Boolean temp1 = cols.contains(lhs);
				                if(!temp1)
				                {
				                    msg="Error: Invalid column name in filter condition";
				                    filtcheck=false;
				                    break;
				                }
				                Boolean temp2 = operators.contains(op);
				                if(!temp2)
				                {
				                    msg="Error: Invalid operator in filter condition";
				                    filtcheck=false;
				                    break;
				                }
				                Boolean temp3=true;
				                if(temp1 && temp2)
				                {
				                    System.out.println("temp 1 and temp2"+temp1+" "+temp2);
				                    int index=cols.indexOf(lhs);
				                    String dtype=dts.get(index);
				                    switch(dtype) {
				                    case "string":
				                        System.out.println("In switch string");
				                        String t="";
				                        Matcher matcher2= pattern2.matcher(rhs);
				                        if(matcher2.find())
				                        {
				                            t=matcher2.group(1).trim();
				                            System.out.println("In switch string matcher");
				                        }
				                        System.out.println("t="+t+" rhs="+rhs);
				                        if(!rhs.contentEquals(t))
				                        {
				                            System.out.println("rhs is not equal");
				                            temp3=false;
				                        }
				                        break;
				                       
				                    case "int":
				                        String t2="";
				                        Matcher matcher3= pattern3.matcher(rhs);
				                        if(matcher3.find())
				                        {
				                            t2=matcher3.group(1).trim();
				                        }
				                        if(!rhs.contentEquals(t2))
				                        {
				                            temp3=false;
				                        }
				                        break;
				                    }
				                }
				                System.out.println("temp3 "+temp3);
				                if(!temp3)
				                {
				                    msg="Error: Invalid data in filter condition";
				                    filtcheck=false;
				                    break;
				                }
				            }
				           
				        }
				       
				       
				        Boolean limcheck=true;
				        if (lim!=null) {
				            Pattern pattern4=Pattern.compile("([\\d]+)");
				            String lt="";
				            Matcher matcher3= pattern3.matcher(lim);
				            if(matcher3.find())
				            {
				                lt=matcher3.group(1).trim();
				            }
				            if(!lim.contentEquals(lt))
				            {
				                limcheck=false;
				                msg="Error: Invalid limit";
				            }
				        }
				           
				        if(filtcheck && limcheck)
				        {
				            msg="Valid";
				            result.add("true");
				            result.add(msg);
				        }
				        else {
				            result.add("false");
				            result.add(msg);
				        }
				       
				        return result;
				    }
}
