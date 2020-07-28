package com.mycompany.rest.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.mycompany.rest.domain.Image;

@Repository
public class DogJdbcRepository {
	 @Autowired
	 DataSource dataSource;
	 
	
	 public int insert(Image dog) {
		 JdbcTemplate jdbcTmp = new JdbcTemplate(dataSource);
	 return jdbcTmp.update("insert into dog (breed, imageLink) " + "values(?, ?)",new Object[] {
             dog.getBreed(), dog.getImageLink()
         });
	 }
	 
	 public List < Image > findAll() {
		 JdbcTemplate jdbcTmp = new JdbcTemplate(dataSource);
	        return jdbcTmp.query("select distinct dog.id, breed, imageLink, count(votes) as votes from dog left join vote on dog.id=vote.imageID group by dog.id", new DogRowMapper());
	   }
	 
	 public List<Image> findById(Integer id) {
		 JdbcTemplate jdbcTmp = new JdbcTemplate(dataSource);
		 String query = "select dog.id, breed, imageLink, count(votes) as votes from dog left join vote on dog.id=vote.imageID where dog.id="+ id +" group by dog.id";
	        return jdbcTmp.query(query,  new DogRowMapper());
	    }
	 
	 public int updateRatingById(Integer id,Integer val,String ip) {
		 JdbcTemplate jdbcTmp = new JdbcTemplate(dataSource);
		 int updated = 0;
		 String query = "select * from vote where imageID="+ id + " and ipAddress="+ip.replaceAll(":", "");
		 List<Map<String, String>> vtList = jdbcTmp.query(query,  new VoteRowMapper()); 
	        if(!checkForDuplicate(vtList,ip)) {
	        	 return jdbcTmp.update("insert into vote (imageID, ipAddress, votes) " + "values(?, ?, ?)",new Object[] {
	        			 id, ip.replaceAll(":", ""), val 
	                 });
	        } else {
	        	updated = 2;
	        }
	        return updated;
	    }
	 
	 private Boolean checkForDuplicate(List<Map<String, String>> vList, String ip) {
		Boolean voted = false;
		for(Map<String, String> vMap: vList) {
			if(ip.replaceAll(":", "").equalsIgnoreCase(vMap.get("ipAddress"))) {
				voted = true;
				break;
			}
		}
		return voted;
		
	}

	class DogRowMapper implements RowMapper < Image > {
         @Override
         public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
             Image dog = new Image();
             dog.setId(rs.getInt("id"));
             dog.setBreed(rs.getString("breed"));
             dog.setImageLink(rs.getString("imageLink"));
             dog.setVotes(rs.getInt("votes"));
             return dog;
         }
     }
	 class VoteRowMapper implements RowMapper < Map<String,String> > {
         @Override
         public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
             Map<String,String> vote = new HashMap<String,String>();
             vote.put("imageID", rs.getString("imageID"));
             vote.put("ipAddress", rs.getString("ipAddress"));
             vote.put("votes", rs.getString("votes"));
             return vote;
         }
     }
}
