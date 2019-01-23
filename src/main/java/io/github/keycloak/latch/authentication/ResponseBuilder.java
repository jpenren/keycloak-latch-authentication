package io.github.keycloak.latch.authentication;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;


/**
 * Class responsible to transform JSON response into java a object
 * @author jpenren
 */
public final class ResponseBuilder {
	protected static final int ALREADY_PAIRED_CODE = 205;
	private static Map<Class<?>, Deserializer<?>> map = new HashMap<Class<?>, ResponseBuilder.Deserializer<?>>();
	
	private ResponseBuilder() {
	}
	
	static{
		//Define available deserializers
		//Deserializer for pair response
		map.put(PairResponse.class, new Deserializer<PairResponse>() {
			
			public PairResponse parse(final JsonObject json) {
				final PairResponse response = new PairResponse();
				final String accountId = json.getJsonObject("data").getString("accountId");
				response.setAccountId(accountId);
				
				//Parse error message if exists
				boolean hasError = json.get("error")!=null;
				if(hasError){
					final int errorCode = json.getJsonObject("error").getInt("code");
					response.setAlreadyPaired(ALREADY_PAIRED_CODE==errorCode);
				}
				
				return response;
			}
		});
		
		//Deserializer for status response
		map.put(StatusResponse.class, new Deserializer<StatusResponse>() {

			public StatusResponse parse(JsonObject json) {
				final StatusResponse response = new StatusResponse();
				final JsonArray operations = json.getJsonObject("data").getJsonArray("operations");
				final JsonObject operationData = operations.getJsonObject(0);
				final String status = operationData.getString("status");
				response.setStatus(status);
				 
				//Parse Two Factor data if exists
				if(operationData.get("two_factor")!=null){
					final JsonObject twoFactor = operationData.getJsonObject("two_factor");
					final String token = twoFactor.getString("token");
					final long generated = twoFactor.getInt("generated");
					response.setToken(token);
					response.setGenerated(generated);
				}
				
				return response;
			}
		});
	}
	
	/**
	 * Parse JSON response to an object
	 * @param json response
	 * @param target class parse to
	 * @return instance of LatchResponse
	 * @throws MethodException
	 */
	public static <T extends LatchResponse> T build(final String json, Class<T> target) {
		JsonReader reader = Json.createReader(new StringReader(json));
		JsonObject el = reader.readObject();
		final boolean hasData = el.get("data")!=null;
		if(!hasData){
			//Unexpected data received
			throw new IllegalStateException("Unexpected response: " + json);
		}
		
		return deserialize(el, target);
	}
	
	/**
	 * Find deserializer to parse a JSON object
	 * @param json JSON object instance to parse
	 * @param target class parse to
	 * @return instance of LatchResponse
	 * @throws MethodException
	 */
	@SuppressWarnings("unchecked")
	private static <T extends LatchResponse> T deserialize(final JsonObject json, Class<T> target) {
		if(!map.containsKey(target)){
			throw new IllegalStateException("ResponseType not available");
		}
		
		return (T) map.get(target).parse(json);
	}
	
	/**
	 * Interface for deserializers
	 * @author jpenren
	 *
	 * @param <E> return type
	 */
	private interface Deserializer<E>{
		public E parse(final JsonObject json);
	}
	
}