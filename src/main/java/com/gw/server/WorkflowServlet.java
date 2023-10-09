package com.gw.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.apache.tomcat.websocket.WsSession;

/**
 * 
 * This class is used for monitoring workflow execution
 * 
 * @author JensenSun
 *
 */
//ws://localhost:8080/Geoweaver/jupyter-socket/api/kernels/884447f1-bac6-4913-be86-99da11b2a78a/channels?session_id=42b8261488884e869213604975141d8c
@ServerEndpoint(value = "/workflow-socket")
public class WorkflowServlet {
	
	Logger logger = Logger.getLogger(WorkflowServlet.class);
	
	// private Session wsSession;

	static Map<String, Session> peers = new HashMap();
	
//    private HttpSession httpSession;
	
	@OnOpen
    public void open(Session session, EndpointConfig config) {
		
		try {
			logger.debug("Workflow-Socket websocket channel openned");
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
    }

	public void registerSession(Session session, String token){
		WsSession wss = (WsSession) session;
		logger.debug("Web Socket Session ID:" + wss.getHttpSessionId());
		peers.put(token, session);
	}

    @OnError
    public void error(final Session session, final Throwable throwable) throws Throwable {
        
    	logger.error("websocket channel error" + throwable.getLocalizedMessage());
    	
    	throw throwable;
    	
    }

    @OnMessage
    public void echo(String message, Session session) {
        
    	try {
    		
			logger.debug("Received message: " + message);


			if(message.indexOf("token:")!=-1){

				message = message.substring(6);

				this.registerSession(session, message);

			}
			
			session.getBasicRemote().sendText("Session_Status:Active");
        	
    	}catch(Exception e) {
    		
    		e.printStackTrace();
    		
    	}
    	
    }

    @OnClose
    public void close(final Session session) {
    	
		try {
			
    		logger.info("Channel closed.");

			WsSession wss = (WsSession) session;
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
    	
    }

	public static javax.websocket.Session findSessionByToken(String token) {
    	javax.websocket.Session se = null;
        if (peers.containsKey(token)) {
        	se = peers.get(token);
        }
        return se;
    }

}
