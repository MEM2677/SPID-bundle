package it.entando.pa.spid;

import it.entando.pa.spid.model.keycloak.Token;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import static it.entando.pa.spid.Constants.*;

public class RestApiOps {

  private static final Logger logger = LogManager.getLogger(RestApiOps.class);

  protected static ClientConfig createClientConfig() {
    ClientConfig config = new ClientConfig();
    config.register(new LoggingFeature(java.util.logging.Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
      java.util.logging.Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
    return config;
  }

  public static Token getAdminAccessToken(String host, String username, String password, String clientId, String clientSecret) {
    final String REST_URI
      = "http://" + host + "/auth/realms/master/protocol/openid-connect/token";
    Token token = null;
    Client client = null;

    final Form form = new Form();
    form.param("username", username);
    form.param("password", password);
    form.param("grant_type", "password");
    form.param("client_id", clientId);
    form.param("client_secret", clientSecret);
    form.param("scope", "openid");

    try {
      client = ClientBuilder.newClient(DEBUG_ENABLED ? createClientConfig(): new ClientConfig())
        .register(MultiPartFeature.class)
        .register(JacksonFeature.class);

      Response response = client
        .target(REST_URI)
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
      if (response.getStatus() == HttpStatus.SC_OK) {
        token = response.readEntity(Token.class);
      } else {
        logger.debug("Unexpected result {}", response.getStatus());
      }
//      logger.debug("URL {}", REST_URI);
//      logger.debug("response status: {}", response.getStatus());
//      logger.debug("AAT: {}",response.readEntity(String.class));
    } catch (Throwable t) {
      logger.error("error getting the admin access token", t);
    } finally {
      if (client != null) {
        client.close();
      }
    }
    return token;
  }

  public static boolean duplicateAuthFlow(String host, Token token) {
    final String REST_URI
      = "http://" + host + "/auth/admin/realms/"+ KEYCLOAK_DEFAULT_REALM + "/authentication/flows/" + KEYCLOAK_DEFAULT_AUTH_FLOW+ "/copy";
    Client client = null;
    // for a simple payload there's no need to disturb Jackson
    String payload = "{\"newName\":\""+ KEYCLOAK_NEW_AUTH_FLOW_NAME+ "\"}";
    boolean created = false;

    try {
      client = ClientBuilder.newClient(DEBUG_ENABLED ? createClientConfig(): new ClientConfig())
        .register(JacksonFeature.class);
      Response response = client
        .target(REST_URI)
        .request(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token.getAccessToken())
        .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

      if (response.getStatus() == HttpStatus.SC_CREATED) {
//        String result = response.readEntity(String.class);
        created = true;
      } else {
        logger.debug("Unexpected result {}", response.getStatus());
      }
    } catch (Throwable t) {
      logger.error("error in duplicateAuthFlow", t);
    } finally {
      if (client != null) {
        client.close();
      }
    }
    return created;
  }


  public static void another(String host, Token token) {
    final String REST_URI
      = "http://" + host + "/auth/realms/master/protocol/openid-connect/token";
    Client client = null;
    try {

    } catch (Throwable t) {

    } finally {
      if (client != null) {
        client.close();
      }
    }
  }

}
