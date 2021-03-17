package io.hyonsok.bknd;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;    
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@RestController
public class BkndLunchAdminApplication {

	@Autowired
	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;


	@RequestMapping("/saveNewPjt")
	public void saveNewPjt(@RequestBody Project pjt) throws SQLException {	
		Connection connection = dataSource.getConnection();
		   
		
		try {  
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("select * from public.projects where name='"
					     +pjt.getName()+"'");

			String sql = "";		 
			if(!rs.next()){
				sql = "INSERT INTO public.projects "
						+ "(name, useyn)"  
						+ "VALUES('"+pjt.getName()
						+"', 'Y')";
				stmt.executeUpdate(sql);		
			}else{
				stmt.executeUpdate("delete from public.projects where name='"
									+pjt.getName()+"'");		
			}  
				
			System.out.println("------>"+sql);
			
		}finally {
			connection.close();
		}
	}
	
	@RequestMapping("/saveNewUser")  
	public void saveNewUser(@RequestBody PjtUser user) throws SQLException  { 
		Connection connection = dataSource.getConnection();
		 
		try {  
			
			System.out.println("pjtId: "+user.getPjtId());

			Statement stmt = connection.createStatement();

			String qry = "select * from public.users where projectid = '"+user.getPjtId()
						+"' and name='"+user.getName()+"'";
			ResultSet rs = stmt.executeQuery(qry);  
			   
			System.out.println(qry); 
   
			if(!rs.next()){
				String sql = "INSERT INTO public.users "
							+ "(name,projectid, useyn)"
							+ "VALUES('"+user.getName()
							+ "',(select id from projects where name='"+user.getPjtName()
							+"'), 'Y')";  
				System.out.println("------>"+sql);
				stmt.executeUpdate(sql);	
			}else{  
				String sql = "delete from public.users where projectid='"
				             +user.getPjtId()
				             +"' and name='"+user.getName()+"'";
				System.out.println("------->"+sql); 
				stmt.executeUpdate(sql);
			}
			
		}finally { 
			connection.close();
		}
		
	}
	
	@RequestMapping("/saveNewRestaurant")
	public void saveNewRestaurant(@RequestBody PjtRestaurant restaurant) throws SQLException {
		Connection connection = dataSource.getConnection();
		 
		try {  
			
			Statement stmt = connection.createStatement();

			String qry = "select * from public.restaurants where projectid = '"
						+restaurant.getPjtId()
						+"' and name='"+restaurant.getName()+"'";
			ResultSet rs = stmt.executeQuery(qry);  
			  
			if(!rs.next()){
				String sql = "INSERT INTO public.restaurants "
							+ "(name,projectid, useyn)"  
							+ "VALUES('"+restaurant.getName()
							+ "',(select id from projects where name='"+restaurant.getPjtName()
							+"'), 'Y')";
				System.out.println("------>"+sql);
				stmt.executeUpdate(sql);	
			}else{   
				stmt.executeUpdate("delete from public.restaurants where projectid ='"
									+ restaurant.getPjtId()
								    +"' and name='"
					+restaurant.getName()+"'");  
			}
			
		}finally { 
			connection.close();
		}
	}
	

	@RequestMapping("/listFoodHistory")
	@ResponseBody
	public List<FoodHistory> listFoodHistory(@RequestParam String dateInput,
															  @RequestParam String pjtName) throws SQLException{

		Connection conn = dataSource.getConnection(); 

		try{
			Statement stmt = conn.createStatement();
			String sql = "select b.date,"
								+"b.menu,"
								+"b.name,"
								+"b.division as division_no,"
								+"case when b.division ='3' then 'Dinner'"
								+"     when b.division ='1' then 'Breakfast'"
								+"     when b.division ='4' then 'Snack'"
								+"else 'Lunch' end as division " 
						+" from projects a,"  
							+" menu_selected b"
						+" where a.id =b.projectid "
						+" and a.name = '"+pjtName+"'"
						+" and b.date = '"+dateInput+"' "
								+ "order by name,division_no, menu";
			
			System.out.println("---->"+sql);
			  
			ResultSet rs = stmt.executeQuery(sql);
			
			List<FoodHistory> foodHistories = new ArrayList();
			
			while(rs.next()) {
				FoodHistory foodHistory = new FoodHistory();
				foodHistory.setDate(rs.getString("date"));
				foodHistory.setMenu(rs.getString("menu"));
				foodHistory.setName(rs.getString("name"));
				foodHistory.setDivision(rs.getString("division"));
				   
				foodHistories.add(foodHistory);
				
			}
			
			return foodHistories;
			
		}finally{  
			conn.close(); 
		}
	}

	@RequestMapping("/")
	@ResponseBody
	public List<Project> listPjtUsers()
			throws SQLException, JsonGenerationException, JsonMappingException, IOException {

		Connection connection = dataSource.getConnection();
		try {

			Statement statement = connection.createStatement();
			String sql = "SELECT id, name, created, updated, "
							+ "description, useyn " 
							+ "FROM projects";
			 
			ResultSet rs = statement.executeQuery(sql);

			List<Project> users = new ArrayList<>();

			while (rs.next()) {
				Project project = new Project();
				project.setCreated(rs.getString("created"));
				project.setDescription(rs.getString("description"));
				project.setId(rs.getString("id"));
				project.setName(rs.getString("name"));
				project.setUpdated(rs.getString("updated"));
				project.setUseyn(rs.getString("useyn"));

				users.add(project);

			}

			return users;

		} finally {
			connection.close();
		}

	}

	public static void main(String[] args) {

		SpringApplication.run(BkndLunchAdminApplication.class, args);
	}

}
