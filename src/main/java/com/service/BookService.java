package com.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import com.entity.Book;

@Component
public class BookService {

	public List<Book> retrieveBooks() {

		String inline = "";

		try {
			URI uri = new URI("http", null, "localhost", 8084, "/App2/book", null, null);
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

			List<Book> allBook = new LinkedList<Book>();
			for (int i = 0; i < jsonarr.size(); i++) {
				JSONObject jsonobj = (JSONObject) jsonarr.get(i);

				Long code = (Long) jsonobj.get("bookCode");
				String bookname = (String) jsonobj.get("bookName");
				String author = (String) jsonobj.get("author");
				String addedon = (String) jsonobj.get("addedOn");

				Book book = new Book();
				int bookcode = code.intValue();
				book.setAddedOn(addedon);
				book.setAuthor(author);
				book.setBookCode(bookcode);
				book.setBookName(bookname);

				allBook.add(book);

			}

			return allBook;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void deleteBook(int bookcode) {


		try {
			URI uri = new URI("http", null, "localhost", 8084, "/App2/book/" + bookcode, null, null);
			URL url = uri.toURL();	
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.connect();
			int responsecode = conn.getResponseCode();
			System.out.println("Response code is: " + responsecode);

			if (responsecode != 200)
				throw new RuntimeException("HttpResponseCode: " + responsecode);
			
		}

		catch (Exception e) {
			e.printStackTrace();

		}
		
	}
	public void saveBook(Book book,String method) {

		String json = "{\"bookCode\":"+book.getBookCode()+",\"bookName\":\""+book.getBookName()+"\",\"author\":\""+book.getAuthor()+"\",\"addedOn\":\""+book.getAddedOn()+"\"}";
		try {
		URI uri = new URI("http", null, "localhost", 8084, "/App2/book", null, null);
		URL url = uri.toURL();
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod(method);
		http.setDoOutput(true);
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("Content-Type", "application/json");
		byte[] out = json.getBytes(StandardCharsets.UTF_8);



		OutputStream stream = http.getOutputStream();
		stream.write(out);



		System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
		http.disconnect();
		System.out.println("Created JASON:"+json);
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}


		}
}
