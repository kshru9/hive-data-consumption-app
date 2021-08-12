// package app.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.jdbc.core.JdbcTemplate;

// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;
// import java.util.Objects;

// public class HiveService{

//     @Autowired
// 	@Qualifier("hiveJdbcTemplate")
// 	private static JdbcTemplate hiveJdbcTemplate;

//     public static String select(List<String> col, List<String> filt, long lim, String sour, String db){
//         String sql = "";
//         sql += "select" + " ";
//          for (int i = 0; i < col.size(); i+=1){
//             sql += col.get(i) + " ";
//         }

//         sql += "from" + " " + db + " ";

//         sql += "limit" + " " + Objects.toString(lim,null);

        
// 		List<Map<String, Object>> rows = hiveJdbcTemplate.queryForList(sql);
// 		Iterator<Map<String, Object>> it = rows.iterator();
// 		while (it.hasNext()) {
//             Map<String, Object> row = it.next();
//             // System.out.println(row);
// 			System.out.println(String.format("%s\t%s", row.get("medicare_demographic.id"), row.get("medicare_demographic.name")));
// 		}
        
// 		return "Done";

//     }
// }