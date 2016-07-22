package com.sholay.app.Serving;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by mohan.gupta on 21/07/16.
 */
public class ServerStarter {
    public static void main(final String[] args) throws Exception {


        Server server = new Server(9090);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/thakur");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new MainServer()), "/");
        server.start();
    }
}
