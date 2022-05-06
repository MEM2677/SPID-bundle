package it.entando.pa.spid;

import it.entando.pa.spid.model.KCToken;
import it.entando.pa.spid.model.KCCredentials;
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
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;


public class RestApiOps {

  private static final Logger logger = LogManager.getLogger(RestApiOps.class);
  private static final boolean DEBUG_ENABLED = false;

  protected static ClientConfig createClientConfig() {
    ClientConfig config = new ClientConfig();
    config.register(new LoggingFeature(java.util.logging.Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
      java.util.logging.Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
    return config;
  }

  public static KCToken getAdminAccessToken(String host, KCCredentials kcc) {
    final String REST_URI
      = "http://" + host + "/auth/realms/master/protocol/openid-connect/token";
    KCToken token = null;

    try {
      final Client client = ClientBuilder.newClient(DEBUG_ENABLED ? createClientConfig(): new ClientConfig())
        .register(MultiPartFeature.class)
        .register(JacksonFeature.class);
      WebTarget webTarget = client.target(REST_URI);

      // these are fixed, so overwrite incoming value
      kcc.setGrantType("password");
      kcc.setScope("openid");

      final Form form = new Form();
      form.param("username", kcc.getUsername());
      form.param("password", kcc.getPassword());
      form.param("grant_type", kcc.getGrantType());
      form.param("client_id", kcc.getClientId());
      form.param("client_secret", kcc.getClientSecret());
      form.param("scope", kcc.getScope());

      Response response = webTarget
        .request(MediaType.APPLICATION_JSON)
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
      if (response.getStatus() == HttpStatus.SC_OK) {
        token = response.readEntity(KCToken.class);
      } else {
        logger.debug("Unexpected result {}", response.getStatus());
      }
//      logger.debug("URL {}", REST_URI);
//      logger.debug("response status: {}", response.getStatus());
//      logger.debug("AAT: {}",response.readEntity(String.class));
    } catch (Throwable t) {
      logger.error("error getting the admin access token", t);
    }
    return token;
  }

}
