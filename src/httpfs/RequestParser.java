package httpfs;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RequestParser {

	ArrayList<String> fullRequest;
	String method;
	String path;
	String file;
	String host;
	int port;
	String contentLength;
	String contentType;
	StringBuilder postBody;
	
	public RequestParser(ArrayList<String> fr){
		this.fullRequest = fr;
		postBody = new StringBuilder();
	}
	
	public void parse(){
		extractHostAndPort();
		extractMethodAndPathAndFile();
		if(method.equals("POST")){
			extractPostHeadersAndBody();
		}
	}
	
	void extractMethodAndPathAndFile() {
		String[] firstLine = fullRequest.get(0).split(" ", 3);
		method = firstLine[0];
		path = firstLine[1];
		String[] extractFile = path.split("/");
		file = extractFile[extractFile.length - 1];
	}
	
	void extractHostAndPort() {
		String[] fullHost = fullRequest.get(1).split(" ", 2);
		String[] splitHost = fullHost[1].split(":", 2);
		host = splitHost[0];
		if(splitHost.length > 1)
			port = Integer.parseInt(splitHost[1]);
		else port = Httpfs.port;
	}
	
	void extractPostHeadersAndBody(){
		boolean bodyStarts = false;
		
		for(String requestLine : fullRequest){
			if(requestLine.startsWith("Content-Length")){
				String[] temp = requestLine.split(": ");
				contentLength = temp[1];
			} else if(requestLine.startsWith("Content-Type")){
				String[] temp = requestLine.split(": ");
				contentType = temp[1];
			} else if(requestLine.equals("\r\n")){
				bodyStarts = true;
			} else if(bodyStarts){
				postBody.append(requestLine);
			}
		}
	}
	
	public void DisplayParsedRequest(){
		System.out.println("Method: " + method);
		System.out.println("Path: " + path);
		System.out.println("Host: " + host);
		System.out.println("File " + file);
		System.out.println("Port " + port);
		System.out.println("Content-Length: " + contentLength);
		System.out.println("Content-Type: " + contentType);
		System.out.println("Post body: " + postBody.toString());
	}
}
