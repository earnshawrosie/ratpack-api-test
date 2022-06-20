package ratpack.api;

import com.google.inject.AbstractModule;

import ratpack.handler.BalanceHandler;
import ratpack.handler.LoginHandler;
import ratpack.handler.SpendHandler;
import ratpack.handler.TransactionsHandler;

public class ApiModule extends AbstractModule {

	    @Override
	    protected void configure() {
	        bind(ApiEndpoints.class);

	        bind(LoginHandler.class);
	        bind(BalanceHandler.class);
	        bind(SpendHandler.class);
	        bind(TransactionsHandler.class);
	    }
	}

