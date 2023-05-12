package com.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.entity.Author;


@Component
public class AuthorService {

	public List<Author> retrieveAuthors() {

		String inline = "";

		try {
			URI uri = new URI("http", null, "localhost", 8084, "/App2/author", null, null);
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
			JSONArray jsonarr = (JSONArray) parse.parse(inline);

			List<Author> allAuthor = new LinkedList<Author>();
			for (int i = 0; i < jsonarr.size(); i++) {
				JSONObject jsonobj = (JSONObject) jsonarr.get(i);

				String name = (String) jsonobj.get("name");

				Author author = new Author();
				
				author.setName(name);

				allAuthor.add(author);

			}

			return allAuthor;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
