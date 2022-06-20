package ratpack.api;

import org.pac4j.http.client.direct.HeaderClient;

import com.google.inject.Inject;

import ratpack.func.Action;
import ratpack.handler.BalanceHandler;
import ratpack.handler.LoginHandler;
import ratpack.handler.SpendHandler;
import ratpack.handler.TransactionsHandler;
import ratpack.handling.Chain;
import ratpack.pac4j.RatpackPac4j;
import ratpack.service.jwt.JWTService;

/**
 * All restful endpoints exposed by this application.
 */
public class ApiEndpoints implements Action<Chain> {
	
	@Inject
	private JWTService jwtService;
	
    @Override
    public void execute(Chain chain) throws Exception {
    	chain.all(RatpackPac4j.authenticator(jwtService.createHeaderClient()));
	
    	 chain.prefix("balance", c -> {
             c.all(RatpackPac4j.requireAuth(HeaderClient.class));
             c.get(BalanceHandler.class);
         });
    	 
    	 chain.prefix("transactions", c -> {
             c.all(RatpackPac4j.requireAuth(HeaderClient.class));
             c.get(TransactionsHandler.class);
         });
    	 
    	 chain.prefix("spend", c -> {
             c.all(RatpackPac4j.requireAuth(HeaderClient.class));
             c.post(SpendHandler.class);
         });
    	 
    	chain.post("login", LoginHandler.class);
       
    }
}