/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.rest;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 *
 * @author zoonimbus
 */
public class RestServer {
    
    private Server server;
    private static final int PORT = 9090;
    
    public void start() throws Exception {
        
        System.out.println("Starting REST server");
        
        ServletHolder sh = new ServletHolder(ServletContainer.class);
        
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "br.unb.cic.bionimbus.rest.resources");

        server = new Server(PORT);
        Context context = new Context(server, "/", Context.SESSIONS);
        context.addServlet(sh, "/*");
        server.start();
        server.join();
    }
    
    public void stop() throws Exception{
        server.stop();
    }
    
    public static void main(String[] args) throws Exception {
        new Server().start();
    }
}
