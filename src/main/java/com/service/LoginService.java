package com.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

@Component
public class LoginService {
	
	public boolean checkLogin(String uname, String pass) {
		
			String inline = "";

			try {
				URI uri = new URI("http", null, "localhost", 8084, "/App2/users/" + uname, null, null);
				URL url = uri.toURL();
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();
				int responsecode = conn.getResponseCode();
				System.out.println("Response code is: " + responsecode);

				if (responsecode != 200)
					throw new RuntimeException("HttpResponseCode: " + responsecode);
				else {
					Scanner sc = new Scanner(url.openStream());
					while (sc.hasNext()) {
						inline += sc.nextLine();
					}
					System.out.println("\nJSON Response in String format");
					System.out.println(inline);
					sc.close();
				}

			
				JSONParser parse = new JSONParser();
				JSONObject jobj = (JSONObject) parse.parse(inline);
				 
				String password=(String) jobj.get("pass");
				if(password.equals(pass))
					return true;
				conn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		

		return false;
	}
}
