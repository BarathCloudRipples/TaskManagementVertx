package io.github.task.vertx.api;

import java.util.HashSet;
import java.util.Set;


import io.github.task.vertx.api.entity.User;
import io.github.task.vertx.api.entity.Role;
import io.github.task.vertx.api.service.UserService;
import io.github.task.vertx.api.service.RoleService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class Verticle extends AbstractVerticle {

	  @Override
	  public void start(Future<Void> future) {
	      Router router = Router.router(vertx); // <1>
	      // CORS support
	      Set<String> allowHeaders = new HashSet<>();
	      allowHeaders.add("x-requested-with");
	      allowHeaders.add("Access-Control-Allow-Origin");
	      allowHeaders.add("origin");
	      allowHeaders.add("Content-Type");
	      allowHeaders.add("accept");
	      allowHeaders.add("Authorization");
	      Set<HttpMethod> allowMethods = new HashSet<>();
	      allowMethods.add(HttpMethod.GET);
	      allowMethods.add(HttpMethod.POST);
	      allowMethods.add(HttpMethod.DELETE);
	      allowMethods.add(HttpMethod.PUT);

	      router.route().handler(CorsHandler.create("*") // <2>
	              .allowedHeaders(allowHeaders)
	              .allowedMethods(allowMethods));
	      router.route().handler(BodyHandler.create()); // <3>

	      // routes

	      router.post("/signup").handler(this::signup);
	      router.put("/forgotpassword").handler(this::passwordUpdate);
	      router.post("/login").handler(this::login);
	      
	      router.get("/user/:token").handler(this::getUser);
	      
	      router.get("/role/:token").handler(this::getRole);
	      router.post("/role").handler(this::saveRole);
	      

	      vertx.createHttpServer() // <4>
	              .requestHandler(router::accept)
	              .listen(8080, "0.0.0.0", result -> {
	                  if (result.succeeded())
	                      future.complete();
	                  else
	                      future.fail(result.cause());
	              });
	  }
	  
	  

	  UserService userService = new UserService();
	  RoleService roleService = new RoleService();


	  private void signup(RoutingContext context) {
		  	userService.signup(context, Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
		          if (ar.succeeded()) {
		              sendSuccess(context.response());
		          } else {
		              sendError(ar.cause().getMessage(), context.response());
		          }
		      });
		  }
	  

	  
	  private void login(RoutingContext context) {
		  	userService.validate(context, Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
		          if (ar.succeeded()) {
		              sendSuccess(context.response());
		          } else {
		              sendError(ar.cause().getMessage(), context.response());
		          }
		      });
		  }
	  
	  private void passwordUpdate(RoutingContext context) {
		  userService.passwordUpdate(context, Json.decodeValue(context.getBodyAsString(), User.class), ar -> {
	            if (ar.succeeded()) {
	                sendSuccess(context.response());
	                
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	    }
	  
	  private void getUser(RoutingContext context) {
		  userService.getUserDetails(context, context.request().getHeader("Authorization"), context.request().getParam("token"), ar -> {
			  if (ar.succeeded()) {
	                if (ar.result() != null){
	                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
	                } else {
	                    sendSuccess(context.response());
	                }
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	    }
	  
	  private void getRole(RoutingContext context) {
		  roleService.getRoleDetails(context, context.request().getHeader("Authorization"), context.request().getParam("token"), ar -> {
			  if (ar.succeeded()) {
	                if (ar.result() != null){
	                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
	                } else {
	                    sendSuccess(context.response());
	                }
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	    }
	  
	  private void saveRole(RoutingContext context) {
		  roleService.saveRole(context, context.request().getHeader("Authorization"), Json.decodeValue(context.getBodyAsString(), Role.class), ar -> {
	            if (ar.succeeded()) {
	                sendSuccess(context.response());
	                
	            } else {
	                sendError(ar.cause().getMessage(), context.response());
	            }
	        });
	    }
	    
	  private void sendError(String errorMessage, HttpServerResponse response) {
	      JsonObject jObj = new JsonObject();
	      jObj.put("errorMessage", errorMessage);

	      response
	              .setStatusCode(400)
	              .putHeader("content-type", "application/json; charset=utf-8")
	              .end(Json.encodePrettily(jObj));
	  }

	  private void sendSuccess(HttpServerResponse response) {
	      response
	              .setStatusCode(200)
	              .putHeader("content-type", "application/json; charset=utf-8")
	              .end();
	  }
	  
	  private void sendSuccess(String responseBody, HttpServerResponse response) {
	        response
	                .setStatusCode(200)
	                .putHeader("content-type", "application/json; charset=utf-8")
	                .end(responseBody);
	    } 
	}