/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.rest.resources;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
public class MainResource {
    
    @DefaultValue("")
    @QueryParam("username")
    private String username;
   
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String get() {
        return "Hello world!";
    }
        
    @GET
    @Path("jobs")
    @Produces(MediaType.TEXT_PLAIN)
    public String status(){
        return "Status de todos os jobs " + (!username.equals("")? username : "");
    }
    
    @GET
    @Path("jobs/{id}/status")
    @Produces(MediaType.TEXT_PLAIN)
    public String status(@PathParam("id") int id){
        return String.format("status do job %s", id);
    }
    
//    @Path("/users")
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String users(MultiValueMap<String,String> formParams){
//       return "Post"; 
//    }
    
//    @GET
//    @Path("users")
//    @Produces(MediaType.APPLICATION_JSON)
//    public UsuarioExemplo getUsuario(){
//        return new UsuarioExemplo("Ed");
//    }
//   
//    
//    @POST
//    @Path("submit2")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    @Produces(MediaType.TEXT_PLAIN)
//    public String submit(@FormDataParam("file") InputStream file
//                        ,@FormDataParam("file") FormDataContentDisposition fileInfo
//                        ,@FormDataParam("name") String name
//                        ,@FormDataParam("description") String description) throws IOException{
//        
//        System.out.println(name);
//        System.out.println(description);
//
//        if (file != null){
//            System.out.println(fileInfo.getFileName());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
//            String line;
//            
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        }
//                
//        return "OK";
//    }
}
