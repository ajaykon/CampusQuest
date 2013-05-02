package comms;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

public class MessageHandler implements Runnable{
	Gson gson;
	public LinkedList<Message> messages;
	public Hashtable<Message, Response> responseTable;
	public Hashtable<Message, Socket> connectionTable;

	public Semaphore messagesSem;
	public Semaphore responseSem;

	public boolean flag;
	Socket connection;
	
	public MessageHandler(Semaphore messagesSem,
					Semaphore responseSem){
		this.gson = new Gson();
		this.messages = new LinkedList<Message>();
		this.responseTable = new Hashtable<Message, Response>();
		this.connectionTable = new Hashtable<Message, Socket> ();

		this.flag = false;

		this.messagesSem = messagesSem;
		this.responseSem = responseSem;
		
		messagesSem.release();
		responseSem.release();
	}
	
	public MessageHandler(Socket connection,
						Gson gson,
						LinkedList<Message> messages,
						Hashtable<Message, Response> responseTable,
						Hashtable<Message, Socket> connectionTable,
						Semaphore messagesSem,
						Semaphore responseSem){
		this.gson = gson;
		this.messages = messages;
		this.responseTable = responseTable;
		this.connectionTable = connectionTable;
		this.connection = connection;

		this.flag = true;

		this.messagesSem = messagesSem;
		this.responseSem = responseSem;
	}

	@Override
	public void run(){	
		try	{
			if (this.flag == false){
				// This is the main thread. Set up a socket to listen on.
				ServerSocket listener = new ServerSocket(13337, 15);
				while(this.flag == false){
					Socket connection = listener.accept();
					new Thread( new MessageHandler(connection, gson, messages, responseTable, connectionTable, messagesSem, responseSem)).start();
				} 
				listener.close();

			} else {
				DataInputStream in = new DataInputStream(connection.getInputStream());
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
					
				Message message = receiveMessage(in, gson);
				
				if (message == null) System.out.println("Null message you fool");
				
				messagesSem.acquire();
				this.registerMessage(message, connection);
				messagesSem.release();
				
				while(true){
					responseSem.acquire();
					if (message == null) System.out.println("Null message you fool - 2");
					Response response = responseTable.get(message);
					if (response == null){
						responseSem.release();
						continue;
					}
					sendResponse(out, gson, response);
					messageSent(message);
					responseSem.release();
					break;
				}
				connection.close();
			}
		} catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public synchronized static Message receiveMessage(DataInputStream in, Gson gson) throws IOException {
		String inputObj = in.readUTF();
		System.out.println("received :"+inputObj);
		Type listType = new TypeToken<Message>(){}.getType();
				
		Message message = gson.fromJson(inputObj, listType);
		return message;
	}
	
	public synchronized static void sendResponse(DataOutputStream out, Gson gson, Response response) throws IOException {
		String outObj = gson.toJson(response);
		System.out.println("sending :"+outObj);
		out.writeUTF(outObj);
	}
	
	public synchronized void registerMessage(Message message, Socket connection){
		messages.add(message);
		connectionTable.put(message, connection);
	}
	
	public synchronized void messageSent(Message message){
		connectionTable.remove(message);
	}
}
